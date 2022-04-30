package com.example.demo.configs;

import com.example.demo.filters.CustomAuthenticationFilter;
import com.example.demo.filters.CustomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@ComponentScan("com.example.demo.configs")
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;
    private final BasicAuthenticationEntryPoint basicAuthenticationEntryPoint;
    public static final String LOGIN_PATH="/login";
    public static final String REGISTER_PATH="/customers/register";
    public static final String TOKEN_REFRESH_PATH="/customers/refreshToken";
    public static final String IS_PAY_METHOD_EXISTS_PATH="/payMethods/{id}/isExists"; //  404/200
    public static final String ADDRESSES_FOR_CUSTOMER="/customers/{id}/addresses";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomFilter customFilter=new CustomFilter(authenticationManager());
        customFilter.setFilterProcessesUrl(LOGIN_PATH);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(REGISTER_PATH, LOGIN_PATH,TOKEN_REFRESH_PATH, IS_PAY_METHOD_EXISTS_PATH, ADDRESSES_FOR_CUSTOMER).permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customFilter);
        http.addFilterBefore(new CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.httpBasic().authenticationEntryPoint(basicAuthenticationEntryPoint);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
