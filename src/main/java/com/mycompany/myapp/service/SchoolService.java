package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing School.
 */
public interface SchoolService {

    /**
     * Save a school.
     *
     * @param school the entity to save
     * @return the persisted entity
     */
    School save(School school);

    /**
     *  Get all the schools.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<School> findAll(Pageable pageable);
    /**
     *  Get all the SchoolDTO where Leader is null.
     *
     *  @return the list of entities
     */
    List<School> findAllWhereLeaderIsNull();

    /**
     *  Get the "id" school.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    School findOne(Long id);

    /**
     *  Delete the "id" school.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
