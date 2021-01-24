package com.albert.pai.dao;

import com.albert.pai.entity.Types;
import org.springframework.data.repository.CrudRepository;

public interface typesDao extends CrudRepository<Types, Integer> {
    public Types findByIdtypes(Integer idtypes);

}