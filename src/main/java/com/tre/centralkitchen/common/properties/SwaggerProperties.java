package com.tre.centralkitchen.common.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private Boolean enabled;

    private String pathMapping;

    private String title;

    private String description;

    private String version;

    private Contact contact;

    private List<Groups> groups;

    @Data
    @NoArgsConstructor
    public static class Contact {

        private String name;

        private String url;

        private String email;

    }

    @Data
    @NoArgsConstructor
    public static class Groups {

        private String name;

        private String basePackage;

    }

}
