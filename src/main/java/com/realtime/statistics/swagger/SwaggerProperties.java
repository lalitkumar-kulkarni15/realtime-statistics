package com.realtime.statistics.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
	
    private String title;

    private String description;

    private String version;

    private String name;

    private String profileLink;

    private String liscence;

    private String basePackage;

    private String email;

}
