package com.playtika.test.localstack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DisableLocalStackTest.TestConfiguration.class,
        properties = "embedded.localstack.enabled=false")
public class DisableLocalStackTest {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Test
    public void contextLoads() {
        String[] containers = beanFactory.getBeanNamesForType(LocalStackContainer.class);
        assertThat(containers).isEmpty();
    }

    @EnableAutoConfiguration
    @Configuration
    static class TestConfiguration {
    }
}
