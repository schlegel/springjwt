package com.github.schlegel.springjwt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;


@SpringBootApplication
@ComponentScan(basePackages = {"com.github.schlegel.springjwt"})
public class Application extends SpringBootServletInitializer {

    private static Log logger = LogFactory.getLog(Application.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws Throwable {
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext context = app.run(args);

        logger.info("Activated Spring Profiles: " + Arrays.toString(context.getEnvironment().getActiveProfiles()));
        logger.info("Connected to database: " + context.getEnvironment().getProperty("spring.datasource.url"));
    }

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
