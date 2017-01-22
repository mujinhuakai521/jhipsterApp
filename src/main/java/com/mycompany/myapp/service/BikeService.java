package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Bike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Bike.
 */
public interface BikeService {

    /**
     * Save a bike.
     *
     * @param bike the entity to save
     * @return the persisted entity
     */
    Bike save(Bike bike);

    /**
     *  Get all the bikes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Bike> findAll(Pageable pageable);

    /**
     *  Get the "id" bike.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Bike findOne(Long id);

    /**
     *  Delete the "id" bike.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
