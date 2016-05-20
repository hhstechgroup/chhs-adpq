package com.engagepoint.cws.apqd.config;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.*;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import fr.ippon.spark.metrics.SparkReporter;
import io.github.hengyunabc.metrics.ZabbixReporter;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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

    private static final String INTAKE_INSTANCE_PREFIX = "cws-intake";

    private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
    private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
    private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
    private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
    private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

    private final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);

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
        log.debug("Registering JVM gauges");
        metricRegistry.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
        metricRegistry.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
        metricRegistry.register(PROP_METRIC_REG_JVM_BUFFERS, new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        if (jHipsterProperties.getMetrics().getJmx().isEnabled()) {
            log.debug("Initializing Metrics JMX reporting");
            JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
            jmxReporter.start();
        }
    }

    @Configuration
    @ConditionalOnClass(Graphite.class)
    @Profile("!" + Constants.SPRING_PROFILE_FAST)
    public static class GraphiteRegistry {

        private final Logger log = LoggerFactory.getLogger(GraphiteRegistry.class);

        @Inject
        private MetricRegistry metricRegistry;

        @Inject
        private JHipsterProperties jHipsterProperties;

        @PostConstruct
        private void init() {
            if (jHipsterProperties.getMetrics().getGraphite().isEnabled()) {
                log.info("Initializing Metrics Graphite reporting");
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

        private final Logger log = LoggerFactory.getLogger(SparkRegistry.class);

        @Inject
        private MetricRegistry metricRegistry;

        @Inject
        private JHipsterProperties jHipsterProperties;

        @PostConstruct
        private void init() {
            if (jHipsterProperties.getMetrics().getSpark().isEnabled()) {
                log.info("Initializing Metrics Spark reporting");
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

        private final Logger log = LoggerFactory.getLogger(ZabbixRegistry.class);

        @Inject
        private MetricRegistry metricRegistry;

        @Inject
        private JHipsterProperties jHipsterProperties;

        @Inject
        private Environment env;

        @PostConstruct
        private void init() {
            if (jHipsterProperties.getMetrics().getZabbix().isEnabled()) {
                log.info("Initializing Metrics Zabbix reporting");
                String zabbixHost = jHipsterProperties.getMetrics().getZabbix().getHost();
                Integer zabbixPort = jHipsterProperties.getMetrics().getZabbix().getPort();
                Integer periodSec = jHipsterProperties.getMetrics().getZabbix().getPeriodSec();
                String intakeInstanceName = jHipsterProperties.getMetrics().getZabbix().getIntakeInstanceName();

                if(StringUtils.isEmpty(intakeInstanceName)){
                    intakeInstanceName = buildIntakeInstanceName();
                }

                ZabbixSender zabbixSender = new ZabbixSender(zabbixHost, zabbixPort);
                ZabbixReporter zabbixReporter = ZabbixReporter.forRegistry(metricRegistry)
                    .hostName(intakeInstanceName).build(zabbixSender);

                zabbixReporter.start(periodSec, TimeUnit.SECONDS);
            }
        }

        private String buildIntakeInstanceName(){

            StringJoiner joiner = new StringJoiner("-");
            joiner.add(INTAKE_INSTANCE_PREFIX);

            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                String ipAddress = inetAddress.getHostAddress();
                String severPort = env.getProperty("server.port");
                joiner.add(ipAddress);
                joiner.add(severPort);
            } catch (UnknownHostException e) {
                log.error("get hostName error!", e);
            }

            return joiner.toString();
        }
    }
}
