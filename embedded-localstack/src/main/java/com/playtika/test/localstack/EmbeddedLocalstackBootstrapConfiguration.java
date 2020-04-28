package com.playtika.test.localstack;

import java.util.LinkedHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static com.playtika.test.localstack.LocalstackProperties.BEAN_NAME_EMBEDDED_LOCALSTACK;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Slf4j
@Configuration
@Order(HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "embedded.localstack.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LocalstackProperties.class)
public class EmbeddedLocalstackBootstrapConfiguration {
    @ConditionalOnMissingBean(name = BEAN_NAME_EMBEDDED_LOCALSTACK)
    @Bean(name = BEAN_NAME_EMBEDDED_LOCALSTACK, destroyMethod = "stop")
    public LocalStackContainer localStack(ConfigurableEnvironment environment,
                                             LocalstackProperties properties) {
        log.info("Starting Localstack server. Docker image: {}", properties.version);

        LocalStackContainer localStackContainer = new LocalStackContainer(properties.version);
        localStackContainer.start();
        registerElasticSearchEnvironment(localStackContainer, environment, properties);
        return localStackContainer;
    }

    private void registerElasticSearchEnvironment(LocalStackContainer localStack,
                                                  ConfigurableEnvironment environment,
                                                  LocalstackProperties properties) {
        //Integer httpPort = localStack.getMappedPort(properties.httpPort);
        String host = localStack.getContainerIpAddress();

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("embedded.elasticsearch.host", host);
        //map.put("embedded.elasticsearch.httpPort", httpPort);

        log.info("Started Localstack. Connection details: {}", map);

        MapPropertySource propertySource = new MapPropertySource("embeddedLocalstackInfo", map);
        environment.getPropertySources().addFirst(propertySource);
    }
}
