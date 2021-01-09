package com.lal.demo.config;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@Configuration
public class ResilienceConfig {

    @Bean
    public RetryRegistry retryRegistry(){
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(6)
                .intervalFunction(IntervalFunction.ofExponentialRandomBackoff(1000L,2,0.6))
                .retryOnException(throwable -> throwable instanceof RestClientException && !(throwable instanceof HttpClientErrorException))
                .build();
        return RetryRegistry.of(retryConfig);
    }

    @Bean
    public Retry retry(RetryRegistry retryRegistry){
        return retryRegistry.retry("retry");
    }
}
