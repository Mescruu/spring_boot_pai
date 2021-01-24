package com.albert.pai.dao;

import com.albert.pai.entity.Ratings;
import com.albert.pai.entity.Recipes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ratingDao extends CrudRepository<Ratings, Integer> {
    public Ratings findByRatingid(Integer ratingid);

    public Ratings findByUseridAndRecipeid(Integer userid, Integer recipeid);

    @Query("Select AVG(r.rating) from Ratings r WHERE r.recipeid = :recipe")
    public   Float FindAverageRatingOfRecipe(Integer recipe);

}