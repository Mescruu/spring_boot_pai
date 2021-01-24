package com.albert.pai.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Entity – adnotacja informuje, że klasa reprezentuje encję, czyli będzie odwzorowana na rekord tabeli
//w bazie danych;
@Entity
@Table(name = "Ratings")
public class Ratings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ratingid;

    @NotNull
    private Integer rating;

    @NotNull
    private Integer userid;

    @NotNull
    private Integer recipeid;

    public Ratings(){
    }

    public Ratings(Integer rating,Integer userid, Integer recipeid) {
        this.rating = rating;
        this.userid = userid;
        this.recipeid = recipeid;
    }

    //gettery
    public Integer getRatingid() {
        return ratingid;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getUserid() {
        return userid;
    }

    public Integer getRecipeid() {
        return recipeid;
    }


    //settery

    public void setRatingid(Integer ratingid) {
        this.ratingid = ratingid;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setRecipeid(Integer recipeid) {
        this.recipeid = recipeid;
    }
}
