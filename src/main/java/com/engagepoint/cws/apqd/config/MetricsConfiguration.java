package com.engagepoint.cws.apqd.config;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.*;
import com.engagepoint.cws.apqd.config.zabbix.ActiveChecksRequest;
import com.engagepoint.cws.apqd.config.zabbix.ApqdZabbixResponse;
import com.engagepoint.cws.apqd.config.zabbix.ApqdZabbixSender;
import com.engagepoint.cws.apqd.config.zabbix.ZabbixResponseType;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import fr.ippon.spark.metrics.SparkReporter;
import io.github.hengyunabc.metrics.ZabbixReporter;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics(proxyTargetClass = true)
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    private static final String APPLICATION_INSTANCE_PREFIX = "chhs-apqd";

    private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
    private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
    private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
    private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
    private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsConfiguration.class);

    private MetricRegistry metricRegistry = new MetricRegistry();

    private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Override
    @Bean
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    @Override
    @Bean
    public HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

    @PostConstruct
    public void init() {
        LOGGER.debug("Registering JVM gauges");
        metricRegistry.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
        metricRegistry.register(PROP_METRIC_REG_JVM_BUFFERS, new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        if (jHipsterProperties.getMetrics().getJmx().isEnabled()) {
            LOGGER.debug("Initializing Metrics JMX reporting");
            JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
            jmxReporter.start();
        }
    }

    @Configuration
    @ConditionalOnClass(Graphite.class)
    @Profile("!" + Constants.SPRING_PROFILE_FAST)
    public static class GraphiteRegistry {

        private static final Logger LOGGER = LoggerFactory.getLogger(GraphiteRegistry.class);

        @Inject
        private MetricRegistry metricRegistry;

        @Inject
        private JHipsterProperties jHipsterProperties;

        @PostConstruct
        @SuppressWarnings("squid:UnusedPrivateMethod")
        private void init() {
            if (jHipsterProperties.getMetrics().getGraphite().isEnabled()) {
                LOGGER.info("Initializing Metrics Graphite reporting");
                String graphiteHost = jHipsterProperties.getMetrics().getGraphite().getHost();
                Integer graphitePort = jHipsterProperties.getMetrics().getGraphite().getPort();
                String graphitePrefix = jHipsterProperties.getMetrics().getGraphite().getPrefix();
                Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
                GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .prefixedWith(graphitePrefix)
                    .build(graphite);
                graphiteReporter.start(1, TimeUnit.MINUTES);
            }
        }
    }

    @Configuration
    @ConditionalOnClass(SparkReporter.class)
    @Profile("!" + Constants.SPRING_PROFILE_FAST)
    public static class SparkRegistry {

        private static final Logger LOGGER = LoggerFactory.getLogger(SparkRegistry.class);

        @Inject
        private MetricRegistry metricRegistry;

        @Inject
        private JHipsterProperties jHipsterProperties;

        @PostConstruct
        @SuppressWarnings("squid:UnusedPrivateMethod")
        private void init() {
            if (jHipsterProperties.getMetrics().getSpark().isEnabled()) {
                LOGGER.info("Initializing Metrics Spark reporting");
                String sparkHost = jHipsterProperties.getMetrics().getSpark().getHost();
                Integer sparkPort = jHipsterProperties.getMetrics().getSpark().getPort();
                SparkReporter sparkReporter = SparkReporter.forRegistry(metricRegistry)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build(sparkHost, sparkPort);
                sparkReporter.start(1, TimeUnit.MINUTES);
            }
        }
    }

    @Configuration
    @ConditionalOnClass(SparkReporter.class)
    @Profile("!" + Constants.SPRING_PROFILE_FAST)
    public static class ZabbixRegistry {

        private static final Logger LOGGER = LoggerFactory.getLogger(ZabbixRegistry.class);

        @Inject
        private MetricRegistry metricRegistry;

        @Inject
        private JHipsterProperties jHipsterProperties;

        @Inject
        private Environment env;

        @PostConstruct
        @SuppressWarnings("squid:UnusedPrivateMethod")
        private void init() {

            if (!jHipsterProperties.getMetrics().getZabbix().isEnabled()) {
                return;
            }

            LOGGER.info("Initializing Metrics Zabbix reporting");

            try {
                confugureApplicationInstanceZabbixHost(jHipsterProperties.getMetrics().getZabbix());
            } catch (IOException e) {
                LOGGER.error("Error at configuring application instance host on Zabbix ", e);
            }
        }

        private void confugureApplicationInstanceZabbixHost(JHipsterProperties.Metrics.Zabbix zabbixProperties) throws IOException {

            String zabbixHost = zabbixProperties.getHost();
            Integer zabbixPort = zabbixProperties.getPort();
            Integer periodSec = zabbixProperties.getPeriodSec();
            String hostMetadata = zabbixProperties.getHostMetadata();
            int connectionTimeout = zabbixProperties.getConnectionTimeoutSec() * 1000;
            int socketTimeout = zabbixProperties.getSocketTimeoutSec() * 1000;

            String appInstanceId = buildApplicationInstanceId();
            LOGGER.info("'{}' will be used as application host value on Zabbix", appInstanceId);

            boolean hostIsConfigured = false;

            ApqdZabbixSender apqdZabbixSender =
                new ApqdZabbixSender(zabbixHost, zabbixPort, connectionTimeout, socketTimeout);

            ActiveChecksRequest activeChecksRequest = new ActiveChecksRequest();
            activeChecksRequest.setHost(appInstanceId);
            activeChecksRequest.setHost_metadata(hostMetadata);

            ApqdZabbixResponse zabbixRespone = apqdZabbixSender.send(activeChecksRequest);

            if(zabbixRespone.getType() == ZabbixResponseType.SUCCESS){
                hostIsConfigured = true;
                LOGGER.info("Zabbix host '{}' was already configured", appInstanceId);
            }else if(zabbixRespone.getType() == ZabbixResponseType.FAILED){
                //send another request to check if host was successfully configured as a result of the first one
                ApqdZabbixResponse zabbixRespone2 = apqdZabbixSender.send(activeChecksRequest);

                if(zabbixRespone2.getType() == ZabbixResponseType.SUCCESS) {
                    hostIsConfigured = true;
                    LOGGER.info("Zabbix host '{}' is successfully configured", appInstanceId);
                }
            }

            if(hostIsConfigured) {
                ZabbixSender zabbixSender = new ZabbixSender(zabbixHost, zabbixPort, connectionTimeout, socketTimeout);

                ZabbixReporter zabbixReporter = ZabbixReporter.forRegistry(metricRegistry)
                    .hostName(appInstanceId).build(zabbixSender);

                zabbixReporter.start(periodSec, TimeUnit.SECONDS);
                LOGGER.info("Zabbix reporter is successfully started", appInstanceId);
            }else{
                LOGGER.error("Zabbix host '{}' is not successfully configured", appInstanceId);
            }
        }

        private String buildApplicationInstanceId(){

            StringJoiner joiner = new StringJoiner("-");
            joiner.add(APPLICATION_INSTANCE_PREFIX);

            try {
                InetAddress hostAddress = InetAddress.getLocalHost();
                String hostIpAddress = hostAddress.getHostName();
                joiner.add(hostIpAddress);

                String severPort = env.getProperty("server.port");
                joiner.add(severPort);

            } catch (UnknownHostException e) {
                LOGGER.error("Host name error, '{}' will be used as host name on Zabbix",
                    APPLICATION_INSTANCE_PREFIX, e);
            }

            return joiner.toString();
        }
    }
}
