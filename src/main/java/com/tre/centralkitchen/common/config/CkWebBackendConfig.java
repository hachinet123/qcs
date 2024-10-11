package com.tre.centralkitchen.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ck-web-backend")
public class CkWebBackendConfig {

    private String name;

    private String version;

    private String copyrightYear;

    private boolean demoEnabled;

    private boolean cacheLazy;

    @Getter
    private static boolean addressEnabled;

    public void setAddressEnabled(boolean addressEnabled) {
        CkWebBackendConfig.addressEnabled = addressEnabled;
    }

}
