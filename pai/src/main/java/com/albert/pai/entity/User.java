package com.albert.pai.entity;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Entity – adnotacja informuje, że klasa reprezentuje encję, czyli będzie odwzorowana na rekord tabeli
//w bazie danych;
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userid;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2,30}",
            message="Podaj poprawnie imie")
    private String name;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2,30}",
            message="Podaj poprawnie nazwisko")
    private String surname;

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "[a-zA-Z0-9]{2,30}",
            message="Podaj poprawnie unikalny login")
    private String login;

    @NotEmpty
    (
            message = "Hasło nie może być puste!"
    )
    private String password;
    public User() {
    }
    public User(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    //getters and setters

    public Integer getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}