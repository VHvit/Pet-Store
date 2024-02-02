package com.example.config;

import com.example.mapping.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfig {

    @Bean
    public PetMapping petMapping() {
        return new PetMappingImpl();
    }

    @Bean
    public StoreMapping storeMapping() {
        return new StoreMappingImpl();
    }

    @Bean
    public UserMapping userMapping() {
        return new UserMappingImpl();
    }
}

