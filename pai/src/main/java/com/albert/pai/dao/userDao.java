package com.albert.pai.dao;

import com.albert.pai.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface userDao extends CrudRepository<User, Integer> {
    public User findByLogin(String login);

//• @Query – jest stosowana do utworzenia zapytania za pomocą
//języka JPA Query Language oraz połączenia zapytania
//bezpośrednio z metodą repozytorium interfejsu;

    /*
    //zapytanie wlasne ze zemienna wykon
    @Query("Select c from Country c WHERE c.Continent = :continent")
    public List<Country> findByContinent(String continent);

    //wykorzystujac zapytania wbudowane
    @Query("Select c from Country c WHERE c.Population < :population")
    public   List<Country> findByPopulation(int population);



    public   List<Country> findBySurfaceareaBetween(String min, String max);


    @Query("Select c from Country c WHERE c.surfacearea < :max and c.surfacearea > :min")
    public   List<Country> findByArea(String min, String max);
*/

}