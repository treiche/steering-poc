package de.zalando.steering;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
@ConfigurationProperties(prefix = "zalando.security.matchers")
@EnableOAuth2Sso
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);

    /**
     * This will be wired by Spring with values of property "zalando.security.matchers.unprotected" from the
     * properties.yml files because of the @ConfigurationProperties.
     */
    private List<String> unprotected = new ArrayList<String>();

    public List<String> getUnprotected() {
        return this.unprotected;
    }

    @PostConstruct
    public void init() {
        LOG.info("unprotectedMatcherPatternList=[{}]", getUnprotected());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(getUnprotected().toArray(new String[getUnprotected().size()])).permitAll()
            .anyRequest().authenticated().and().csrf().disable();
    }

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
