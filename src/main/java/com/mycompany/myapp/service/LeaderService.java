package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Leader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Leader.
 */
public interface LeaderService {

    /**
     * Save a leader.
     *
     * @param leader the entity to save
     * @return the persisted entity
     */
    Leader save(Leader leader);

    /**
     *  Get all the leaders.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Leader> findAll(Pageable pageable);

    /**
     *  Get the "id" leader.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Leader findOne(Long id);

    /**
     *  Delete the "id" leader.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
