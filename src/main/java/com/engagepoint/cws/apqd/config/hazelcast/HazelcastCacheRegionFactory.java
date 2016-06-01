package com.engagepoint.cws.apqd.config.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.hibernate.HazelcastTimestamper;
import com.hazelcast.hibernate.local.CleanupService;
import com.hazelcast.hibernate.local.LocalRegionCache;
import com.hazelcast.hibernate.local.TimestampsRegionCache;
import com.hazelcast.hibernate.region.*;
import com.engagepoint.cws.apqd.config.CacheConfiguration;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HazelcastCacheRegionFactory implements RegionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastCacheRegionFactory.class);

    private transient HazelcastInstance hazelcastInstance;

    private transient CleanupService cleanupService;

    public HazelcastCacheRegionFactory() {
        super();
        hazelcastInstance = CacheConfiguration.getHazelcastInstance();
    }

    /**
     * @return true - for a large cluster, unnecessary puts will most likely slow things down.
     */
    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public final QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) {
        return new HazelcastQueryResultsRegion(hazelcastInstance, regionName, properties);
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata) {
        return new HazelcastNaturalIdRegion(hazelcastInstance, regionName, properties, metadata);
    }

    @Override
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties,
                                                  CacheDataDescription metadata) {

        HazelcastCollectionRegion<LocalRegionCache> region = new HazelcastCollectionRegion<>(hazelcastInstance,
                regionName, properties, metadata, new LocalRegionCache(regionName, hazelcastInstance, metadata));

        cleanupService.registerCache(region.getCache());
        return region;
    }

    @Override
    public EntityRegion buildEntityRegion(String regionName, Properties properties,
                                          CacheDataDescription metadata) {

        HazelcastEntityRegion<LocalRegionCache> region = new HazelcastEntityRegion<>(hazelcastInstance,
                regionName, properties, metadata, new LocalRegionCache(regionName, hazelcastInstance, metadata));

        cleanupService.registerCache(region.getCache());
        return region;
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) {
        return new HazelcastTimestampsRegion<>(hazelcastInstance, regionName, properties,
                new TimestampsRegionCache(regionName, hazelcastInstance));
    }

    @Override
    public void start(Settings settings, Properties properties) {
        // Do nothing the hazelcast hazelcastInstance is injected
        LOG.info("Starting up {}", getClass().getSimpleName());

        if (hazelcastInstance == null) {
            throw new IllegalArgumentException("Hazelcast hazelcastInstance must not be null");
        }
        cleanupService = new CleanupService(hazelcastInstance.getName());
    }

    @Override
    public void stop() {
        // Do nothing the hazelcast instance is managed globally
        LOG.info("Shutting down {}", getClass().getSimpleName());
        cleanupService.stop();
    }

    @Override
    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

    @Override
    public long nextTimestamp() {
        return HazelcastTimestamper.nextTimestamp(hazelcastInstance);
    }
}
