package syhan.avro.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import syhan.avro.client.avro.SchemaManager;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    //
    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);

    private SchemaManager schemaManager;

    @Autowired
    public WebConfig(SchemaRegistryClient schemaRegistryClient) {
        //
        this.schemaManager = new SchemaManager(schemaRegistryClient);
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //
        logger.debug("add AvroHttpMessageConverter...............");
        converters.add(new AvroHttpMessageConverter(schemaManager));
    }
}
