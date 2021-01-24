package com.albert.pai.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Entity – adnotacja informuje, że klasa reprezentuje encję, czyli będzie odwzorowana na rekord tabeli
//w bazie danych;
@Entity
@Table(name = "types")
public class Types {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idtypes;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2,100}",
            message="Give correct value")
    private String type;

    public Types() {
    }
    //constructor
    public Types(String Type) {
        this.type = Type;
    }

    public Integer getIdtypes() {
        return idtypes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
