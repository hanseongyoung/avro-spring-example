package syhan.avro.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;

@SpringBootApplication
@EnableSchemaRegistryClient
public class SchemaRegistryClientApplication {
    //
    public static void main(String[] args) {
        //
        SpringApplication.run(SchemaRegistryClientApplication.class, args);
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
