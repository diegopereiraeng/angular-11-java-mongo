package com.bezkoder.spring.data.mongodb.model;

import com.bezkoder.spring.data.mongodb.SpringBootDataMongodbApplication;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import io.harness.cf.client.api.*;
import io.harness.cf.client.dto.Target;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public
class FeatureFlagsService {


    private static final Logger log = LoggerFactory.getLogger(SpringBootDataMongodbApplication.class);

    public CfClient featureFlagService;

    Dotenv dotenv = Dotenv.load();

    public String sdkDefault = dotenv.get("SDK_KEY");

    // Constructors

    public FeatureFlagsService() {
        final FileMapStore fileStore = new FileMapStore(dotenv.get("FF_ENV"));
        CfClient ffClient = new CfClient(this.sdkDefault, BaseConfig.builder().store(fileStore).build());
        this.featureFlagService = ffClient;
        Target target = Target.builder().name("User1").identifier("user1@example.com").build();
        String result = this.featureFlagService.stringVariation("Menu_Version", target, "v5");
        System.out.println("Service Flag Value: "+result);
    }

    public FeatureFlagsService(String Dimension) {
        final FileMapStore fileStore = new FileMapStore(dotenv.get("FF_ENV")+"_"+Dimension);
        CfClient ffClient = new CfClient(this.sdkDefault, BaseConfig.builder().store(fileStore).build());
        this.featureFlagService = ffClient;
        Target target = Target.builder().name("User1").identifier("user1@example.com").build();
        String result = this.featureFlagService.stringVariation("Menu_Version", target, "v5");
        System.out.println("Service Flag Value: "+result);
    }

    public FeatureFlagsService(String SDKKey, String Dimension) {
        final FileMapStore fileStore = new FileMapStore(dotenv.get("FF_ENV")+"_"+Dimension);
        CfClient cfClient = new CfClient(SDKKey, BaseConfig.builder().store(fileStore).build());
        this.featureFlagService = cfClient;
    }

    // Methods

    public boolean boolCheck(String flag,Target target, Boolean defaultValue) {

        boolean result = this.featureFlagService.boolVariation(flag, target, defaultValue);
        System.out.println(flag+" Flag Value: "+result);
        return result;
    }

    public String stringCheck(String flag,Target target, String defaultValue) {

        String result = this.featureFlagService.stringVariation(flag, target, defaultValue);
        System.out.println(flag+" Flag Value: "+result);
        return result;
    }

    public JsonObject jsonCheck(String flag,Target target, JsonObject defaultValue) {

        JsonObject result = this.featureFlagService.jsonVariation(flag, target, defaultValue);
        System.out.println(flag+" Flag Value: "+result);
        return result;
    }

    public double numberCheck(String flag,Target target, double defaultValue) {

        double result = this.featureFlagService.numberVariation(flag, target, defaultValue);
        System.out.println(flag+" Flag Value: "+result);
        return result;
    }


    public boolean doSomething(Target target) {

        String result = this.featureFlagService.stringVariation("Menu_Version", target, "v5");
        System.out.println("Service Flag Value: "+result);
        if (featureFlagService.boolVariation("Dark_Mode", target, false)) {
            log.info("FF Success");
            return true;
        } else {
            System.out.println("FF False");
            return false;
        }
    }
}