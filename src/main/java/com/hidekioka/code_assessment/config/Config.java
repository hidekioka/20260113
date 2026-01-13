package com.hidekioka.code_assessment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Config {

    @Bean
    public RestClient externalCurrencyExchangeAPI() {
        return RestClient.builder().baseUrl("https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/").build();
    }
}