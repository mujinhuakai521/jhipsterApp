package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SchoolService;
import com.mycompany.myapp.domain.School;
import com.mycompany.myapp.repository.SchoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing School.
 */
@Service
@Transactional
public class SchoolServiceImpl implements SchoolService{

    private final Logger log = LoggerFactory.getLogger(SchoolServiceImpl.class);
    
    @Inject
    private SchoolRepository schoolRepository;

    /**
     * Save a school.
     *
     * @param school the entity to save
     * @return the persisted entity
     */
    public School save(School school) {
        log.debug("Request to save School : {}", school);
        School result = schoolRepository.save(school);
        return result;
    }

    /**
     *  Get all the schools.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<School> findAll(Pageable pageable) {
        log.debug("Request to get all Schools");
        Page<School> result = schoolRepository.findAll(pageable);
        return result;
    }


    /**
     *  get all the schools where Leader is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<School> findAllWhereLeaderIsNull() {
        log.debug("Request to get all schools where Leader is null");
        return StreamSupport
            .stream(schoolRepository.findAll().spliterator(), false)
            .filter(school -> school.getLeader() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one school by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public School findOne(Long id) {
        log.debug("Request to get School : {}", id);
        School school = schoolRepository.findOne(id);
        return school;
    }

    /**
     *  Delete the  school by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete School : {}", id);
        schoolRepository.delete(id);
    }
}
