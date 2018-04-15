package syhan.avro.client.config;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class DispatcherServletConfig {
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration() {
        //
        return new ServletRegistrationBean<>(dispatcherServlet());
    }
    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet();
    }

    private static class LoggableDispatcherServlet extends DispatcherServlet {
        @Override
        protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
            //
            HandlerExecutionChain handlerChain = getHandler(request);
            HandlerMethod handler = (HandlerMethod) handlerChain.getHandler();

            //org.springframework.web.method.HandlerMethod
            //public syhan.avro.client.rest.User syhan.avro.client.rest.UserResource.saveUser(syhan.avro.client.rest.UserSdo)
            //beanType:syhan.avro.client.rest.UserResource
            //method:saveUser
            //methodParameters:[Lorg.springframework.core.MethodParameter;@198faae7
            //returnType:method 'saveUser' parameter -1
            System.out.println(handler.getClass().getName());
            System.out.println(handler.toString());
            System.out.println("beanType:"+handler.getBeanType().getName());
            System.out.println("method:"+handler.getMethod().getName());
            System.out.println("methodParameters:"+handler.getMethodParameters());
            System.out.println("returnType:"+handler.getReturnType());

            super.doDispatch(request, response);
        }
    }
}
