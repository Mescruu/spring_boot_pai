package com.albert.pai.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration //adnotacja configutation ponieważ jest to klasa konfiguracyjna
@EnableWebSecurity //Aby wyłączyć domyślną konfigurację zabezpieczeń aplikacji internetowej,można dodać bean z @EnableWebSecurity

public class Security extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserAuthenticationDetails userAuthenticationDetails;
    @Override

    //funkcja ustawia klasę odpowiadającą za uwierzytelnianie użytkowników
    //naszej aplikacji na podstawie danych znajdujących się w bazie danych.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthenticationDetails);
        auth.authenticationProvider(authenticationProvider());
    }

    //Metody oznaczone adnotacją @Bean pozwalają na utworzenie instancji klasy, która może
    //zostać później wstrzykiwana i wykorzystywana w różnych miejscach naszej aplikacji.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userAuthenticationDetails);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] staticResources  =  { //dodanie zaufanych lokalizacji plikow
                "/styles/**",
                "/images/**",
                "/fonts/**",
                "/js/**",
        };


        http.httpBasic().disable() //wyłącza podstawowe, proste uwierzytelnienie użytkowników (httpBasic);
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll() //ustawia autoryzację użytkowników i pozwala określić, w jaki sposób adresy URL
                                                               // będą interpretowane przez środowisko - w aplikacji dajemy dostęp do strony
                                                               // rejestracji “/register” wszystkim użytkownikom (permitAll);
                .antMatchers("/").permitAll() //dodanie kolejnego wyjatku do stron nie potrzebujacych logowania
                .antMatchers(staticResources).permitAll() //dodanie zaufanych plikow (inaczej nie wyswietla sie zdjecia)
                .anyRequest().authenticated()
                .and()
                .authorizeRequests()
                .and()
                .formLogin()  //ustawia podstawowe informacje dotyczące formularza logowania i jego parametrów;
                .loginPage("/login")
                .usernameParameter("login")
                .passwordParameter("passwd")
                .defaultSuccessUrl("/profile", true) //ustawia podstawową stronę domową, na którą przekierowany jest użytkownik po
                                                                                //pomyślnym logowaniu (/profile);
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")   //konfiguruje adres url, pod którym użytkownicy mogą wylogować się z systemu
                                        //i wyczyścić sesję http
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
        ;
    }

    @Configuration
    public class WebConfiguration extends WebMvcConfigurationSupport {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry){
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static")
                    .addResourceLocations("classpath:/static/images")
                    .addResourceLocations("classpath:/static/images/types");
        }
    }

}