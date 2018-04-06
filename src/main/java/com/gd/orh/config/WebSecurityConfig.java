package com.gd.orh.config;

import com.gd.orh.security.JwtAuthenticationEntryPoint;
import com.gd.orh.security.JwtAuthorizationTokenFilter;
import com.gd.orh.security.JwtTokenUtil;
import com.gd.orh.security.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.route.authentication.login}")
    private String loginPath;

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoderBean());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // we don't need CSRF because our token is invulnerable
            .csrf().disable()

            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

            // don't create session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .authorizeRequests()

            // Un-secure H2 Database
            .antMatchers("/console/**/**").permitAll()

            .antMatchers("/").permitAll()
            .antMatchers("/api/**/**").permitAll()
            .antMatchers("/auth/**").permitAll()
            .anyRequest().authenticated();

        // Custom JWT based security filter
        JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(), jwtTokenUtil, tokenHeader);
        http
            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        // http.headers().cacheControl();

        // 使h2控制台在spring security下显示
        // http.headers().frameOptions().disable();

        // disable page caching
        http
            .headers()
            .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
            .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        web
            .ignoring()
            .antMatchers(
                    HttpMethod.POST,
                    loginPath
            )

            // allow anonymous resource requests
            .and()
            .ignoring()
            .antMatchers(
                    HttpMethod.GET,
                    "/",
                    "/api/**/**",
                    "/*.html",
                    "/favicon.ico",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js"
            )

            // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
            .and()
            .ignoring()
            .antMatchers("/console/**/**");
    }
}
