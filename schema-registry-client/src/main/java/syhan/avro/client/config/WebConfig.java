package syhan.avro.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    //
    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //
        logger.debug("add AvroHttpMessageConverter...............");
        converters.add(new AvroHttpMessageConverter());
    }
}
