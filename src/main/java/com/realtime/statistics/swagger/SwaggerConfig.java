package com.realtime.statistics.swagger;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Autowired
	SwaggerProperties swaggerProperties;

	private static final String BLANK = "";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
				.paths(PathSelectors.any()).build().apiInfo(metaInfo());
	}

	private ApiInfo metaInfo() {

		return new ApiInfo(swaggerProperties.getTitle(), swaggerProperties.getDescription(),
				swaggerProperties.getVersion(), BLANK,
				new Contact(swaggerProperties.getName(), swaggerProperties.getProfileLink(),
						swaggerProperties.getEmail()),
				swaggerProperties.getLiscence(), BLANK, new ArrayList<VendorExtension>());
	}
}
