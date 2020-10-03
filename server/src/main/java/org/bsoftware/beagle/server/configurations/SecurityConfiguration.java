package org.bsoftware.beagle.server.configurations;

import org.bsoftware.beagle.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SecurityConfiguration used for web security configuration
 *
 * @author Rudolf Barbu
 * @version 1.0.0
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    /**
     * Autowired UserService object
     * Used for getting user information
     */
    private final UserService userService;

    /**
     * Autowired BCryptPasswordEncoder object
     * Used for password encryption
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Configures authentication builder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
    {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Configures authentication builder
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/user").hasAuthority("ROLE_ANONYMOUS")
                .antMatchers(HttpMethod.PUT,"/api/user").hasAuthority("ROLE_ANONYMOUS")
                .anyRequest()
                .authenticated();

        httpSecurity
                .csrf()
                .disable();
    }

    /**
     * @return AuthenticationManager object as bean
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager() throws Exception
    {
        return super.authenticationManager();
    }

    /**
     * Used for autowiring necessary objects
     *
     * @param userService autowired UserService object
     * @param bCryptPasswordEncoder autowired BCryptPasswordEncoder object
     */
    @Autowired
    public SecurityConfiguration(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}