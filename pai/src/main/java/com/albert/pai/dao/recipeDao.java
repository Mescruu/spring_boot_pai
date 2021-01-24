package com.albert.pai.dao;

import com.albert.pai.entity.Recipes;
import com.albert.pai.entity.Types;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collections;
import java.util.List;

public interface recipeDao extends CrudRepository<Recipes, Integer> {
    public Recipes findByIdrecipe(Integer idrecipe);

    //@Query("Select r from Recipes r WHERE r.type = :type")
    //public   List<Recipes> findByType(String type);

    public List<Recipes> findByType(String type);
    public List<Recipes> findByUserid(Integer userid);

    //zapytanie wlasne ze zemienna wykon
    @Query("Select MAX(r.idrecipe) from Recipes r")
    public Integer findLastId();

}