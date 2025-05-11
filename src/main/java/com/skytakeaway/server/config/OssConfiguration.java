package com.skytakeaway.server.config;

import com.skytakeaway.common.properties.OssProperties;
import com.skytakeaway.common.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    // Creating ossUtil
    @Bean
    @ConditionalOnMissingBean
    public OssUtil ossUtil(OssProperties ossProperties){
        return new OssUtil(ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName());
    }
}
