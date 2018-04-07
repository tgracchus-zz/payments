package com.example.payments.configuration;

import com.example.payments.v0.controllers.PaymentsController;
import com.example.payments.v0.dto.Payment;
import com.mongodb.Mongo;
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.example.payments.repository")
@EnableSwagger2
public class PaymentsConfiguration {

    @Bean(destroyMethod = "close")
    public Mongo mongo() throws IOException {
        return new EmbeddedMongoBuilder()
                .version("2.4.5")
                .bindIp("127.0.0.1")
                .port(27017)
                .build();
    }

    //https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(PaymentsController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build();
    }
}
