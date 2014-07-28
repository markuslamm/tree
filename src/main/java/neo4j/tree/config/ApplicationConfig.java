/**
 * 
 */
package neo4j.tree.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Main application configuration
 * 
 * @author Markus Lamm
 */
@Configuration
@ComponentScan({ "neo4j.tree.event", "neo4j.tree.service" })
@Import(Neo4jConfig.class)
public class ApplicationConfig
{
    /**
     * JSR-303 validation

     * @return validator factory
     */
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return new LocalValidatorFactoryBean();
    }

    /**
     * Message source for validation message interpolation
     * 
     * @return message source instance
     */
    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:messages/messages");
        bean.setDefaultEncoding("UTF-8");
        bean.setUseCodeAsDefaultMessage(false);
        return bean;
    }
}
