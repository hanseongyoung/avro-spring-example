package syhan.avro.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import syhan.avro.client.avro.SchemaManager;

import java.io.IOException;
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

    // for avro message convert
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //
        logger.debug("add AvroHttpMessageConverter...............");
        converters.add(new AvroHttpMessageConverter(schemaManager));
    }

    // for swagger-ui
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //
        logger.debug("add ResourceHandlers...............");
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludePayload(true);
        return loggingFilter;
    }


//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        //
//        registry.addInterceptor(new MessageLoggingInterceptor());
//    }
}
