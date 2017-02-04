package org.duzer.webapp.config;

import org.duzer.webapp.book.dao.BookDAO;
import org.duzer.webapp.book.dao.impl.JdbcBookDAO;
import org.duzer.webapp.user.dao.UserDAO;
import org.duzer.webapp.user.dao.impl.JdbcUserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@ComponentScan(basePackages="org.duzer.webapp")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    final static Logger logger = LoggerFactory.getLogger(MvcConfiguration.class);

    @Bean
    public ViewResolver getViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        logger.debug("Initialized ViewResolver.");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        logger.debug("Initialized /resources dir.");
    }

/*    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("");
        dataSource.setPassword("");

        return dataSource;
    } */

    @Bean
    public DataSource getDataSource() {

        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.H2) //.H2 or .DERBY
                .addScript("classpath:schema-h2.sql")
                .addScript("classpath:data-h2.sql")
                .build();
        logger.debug("Initialized DataSource");
        return db;
    }

    @Bean
    public UserDAO getUserDAO() {
        return new JdbcUserDAO(getDataSource());
    }

    @Bean
    public BookDAO getBookDAO() {
        return new JdbcBookDAO(getDataSource());
    }

}