package com.playtika.test.localstack;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.playtika.test.common.properties.CommonContainerProperties;

@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties("embedded.localstack")
public class LocalstackProperties extends CommonContainerProperties {
    public static final String BEAN_NAME_EMBEDDED_LOCALSTACK = "embeddedLocalstack";
    String version = "0.10.8";
}
