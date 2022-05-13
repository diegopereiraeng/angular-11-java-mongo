package com.bezkoder.spring.data.mongodb.model;

import org.springframework.stereotype.Component;

public class FeatureFlags {
}
@Component
class Service {

    private final FeatureFlags featureFlagService;

    public Service(FeatureFlags featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    public int doSomething() {
        if (FeatureFlags.isNewServiceEnabled()) {
            return "new value";
        } else {
            return "old value";
        }
    }
}