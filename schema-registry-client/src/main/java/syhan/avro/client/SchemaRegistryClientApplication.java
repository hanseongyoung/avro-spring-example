package syhan.avro.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSchemaRegistryClient
@EnableSwagger2
public class SchemaRegistryClientApplication {
    //
    public static void main(String[] args) {
        //
        SpringApplication.run(SchemaRegistryClientApplication.class, args);
    }

    private static String basePackage = "syhan.avro.client";
    private static String title = "schema-registry-client";
    private static String description = "Schema Registry Client.";
    private static String termsOfServiceUrl = "Syhan Avro";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(termsOfServiceUrl)
                .build();
    }

    // optional, Default is 'DefaultSchemaRegistryClient'.
    /*
    @Configuration
    static class ConfluentSchemaRegistryConfiguration {
        @Bean
        public SchemaRegistryClient schemaRegistryClient(@Value("${spring.cloud.stream.schemaRegistryClient.endpoint}") String endpoint){
            ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
            client.setEndpoint(endpoint);
            return client;
        }
    }
    */
}
