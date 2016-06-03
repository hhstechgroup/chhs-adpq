package com.engagepoint.cws.apqd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
@Profile(Constants.SPRING_PROFILE_CLOUD)
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

    @Bean
    @SuppressWarnings("squid:S1172")
    public DataSource dataSource(CacheManager cacheManager) {
        LOGGER.info("Configuring JDBC datasource from a cloud provider");
        return connectionFactory().dataSource();
    }
}
