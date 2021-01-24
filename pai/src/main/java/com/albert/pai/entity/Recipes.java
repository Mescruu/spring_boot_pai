package com.albert.pai.entity;

//https://airtable.com/universe/expHZcS7kWEyq5gUH/recipe-database?explore=true


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Entity – adnotacja informuje, że klasa reprezentuje encję, czyli będzie odwzorowana na rekord tabeli
//w bazie danych;
@Entity
@Table(name = "recipes")
public class Recipes {

    @Id
    private Integer idrecipe;

    public void setIdrecipe(Integer idrecipe) {
        this.idrecipe = idrecipe;
    }

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*${2,20}",
            message="Only letters, numbers and space - 2 - 20 characters")
    private String name;



    private String difficulty;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*${2,1000}",
            message="Only letters, numbers and space - 2 - 1000 characters")
    private String notes;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*${2,30}",
            message="Only letters, numbers and space - 2 - 30 characters")
    private String type;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*${2,30}",
            message="Only letters, numbers and space - 2 - 30 characters")
    private String preptime;

    private String photo;

    @NotNull
    private String ingredients;

    @NotNull
    private Integer userid;

    public Recipes() {
    }

    //constructor
    public Recipes(String name, String difficulty, String Notes, String Type, String PrepTime, String Photo, String Ingredients, Integer userid) {
        this.name = name;
        this.difficulty = difficulty;

        this.notes = Notes;
        this.type = Type;
        this.preptime = PrepTime;
        this.photo = Photo;
        this.ingredients = Ingredients;
        this.userid = userid;

    }
    public Recipes(Integer userid){
        this.userid = userid;
    }

    //Getters
    public Integer getIdrecipe() {
        return idrecipe;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getNotes() {
        return notes;
    }

    public String getType() {
        return type;
    }

    public String getPreptime() {
        return preptime;
    }

    public String getPhoto() {
        return photo;
    }

    public String getIngredients() {
        return ingredients;
    }
    public Integer getUserid(){
        return userid;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(String difficulty) {
        difficulty = difficulty;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPreptime(String preptime) {
        this.preptime = preptime;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

}
