package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Citizen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Citizen.
 */
public interface CitizenService {

    /**
     * Save a citizen.
     *
     * @param citizen the entity to save
     * @return the persisted entity
     */
    Citizen save(Citizen citizen);

    /**
     *  Get all the citizens.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Citizen> findAll(Pageable pageable);

    /**
     *  Get the "id" citizen.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Citizen findOne(Long id);

    /**
     *  Delete the "id" citizen.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
