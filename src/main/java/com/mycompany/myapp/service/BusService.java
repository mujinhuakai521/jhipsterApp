package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Bus.
 */
public interface BusService {

    /**
     * Save a bus.
     *
     * @param bus the entity to save
     * @return the persisted entity
     */
    Bus save(Bus bus);

    /**
     *  Get all the buses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Bus> findAll(Pageable pageable);

    /**
     *  Get the "id" bus.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Bus findOne(Long id);

    /**
     *  Delete the "id" bus.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
