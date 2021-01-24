package com.albert.pai;

import com.albert.pai.dao.userDao;
import com.albert.pai.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.CacheControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PaiApplication {

    @Autowired
    private userDao dao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(PaiApplication.class, args);
    }


    //W metodzie init() tworzymy pierwszego użytkownika za pomocą obiektu dao i obiektu
    //passwordEncoder. Pierwszy użytkownik posiada dane logowania: login: admin, hasło:
    //passwd.
    @PostConstruct
    public void init() {
        //jezeli nie ma zadnego uzytkownika to dodaje
        if (dao.count() == 0) {

            dao.save(new User("admin", "admin",
                    "admin", passwordEncoder.encode("passwd")));
        }
    }


}
