package com.ems.ems.config.swagger_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfiguration {

    @Bean
    public OpenAPI swaggerApi(){
        return new OpenAPI()
                .info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .title("Employee Management System")
                .version("1.0")
                .description("API documentation for Employee Management System using SpringDoc & Swagger UI")
                .contact(new Contact()
                        .name("Kartik")
                        .email("kartikpatel7892@gmail.com")
                        .url("http://localhost:8080/swagger/index.html")
                );
    }

}
