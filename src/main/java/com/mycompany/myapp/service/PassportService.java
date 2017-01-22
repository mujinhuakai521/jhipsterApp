package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Passport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Passport.
 */
public interface PassportService {

    /**
     * Save a passport.
     *
     * @param passport the entity to save
     * @return the persisted entity
     */
    Passport save(Passport passport);

    /**
     *  Get all the passports.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Passport> findAll(Pageable pageable);

    /**
     *  Get the "id" passport.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Passport findOne(Long id);

    /**
     *  Delete the "id" passport.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
