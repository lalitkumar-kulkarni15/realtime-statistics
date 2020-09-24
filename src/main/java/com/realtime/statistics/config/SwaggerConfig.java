package com.realtime.statistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Realtime-Statistics-API")
                .description("This API has 3 endpoints which persists the instrument transactions to the data store and returns the aggregated statistics of the last 60 seconds. " +
                        "• The first one is called every time we receive a tick. It is also the sole input of this rest API.\n" +
                        "• The second one returns the statistics based on the ticks of all instruments of the last 60 seconds\n" +
                        "(sliding time interval)\n" +
                        "• The third one returns the statistics based on the ticks of one instrument of the last 60 seconds\n" +
                        "(sliding time interval).")
                .contact(new Contact("Lalit Kulkarni","https://www.linkedin.com/in/lalitkumar-kulkarni-42159532/","lalitkulkarniofficial@gmail.com"))
                .build();

    }

    @Bean
    public Docket swaggerImplementation(){
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.realtime.statistics.controller"))
                .build().apiInfo(apiInfo());
    }
}
