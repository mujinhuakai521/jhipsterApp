package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CitizenService;
import com.mycompany.myapp.domain.Citizen;
import com.mycompany.myapp.repository.CitizenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Citizen.
 */
@Service
@Transactional
public class CitizenServiceImpl implements CitizenService{

    private final Logger log = LoggerFactory.getLogger(CitizenServiceImpl.class);
    
    @Inject
    private CitizenRepository citizenRepository;

    /**
     * Save a citizen.
     *
     * @param citizen the entity to save
     * @return the persisted entity
     */
    public Citizen save(Citizen citizen) {
        log.debug("Request to save Citizen : {}", citizen);
        Citizen result = citizenRepository.save(citizen);
        return result;
    }

    /**
     *  Get all the citizens.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Citizen> findAll(Pageable pageable) {
        log.debug("Request to get all Citizens");
        Page<Citizen> result = citizenRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one citizen by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Citizen findOne(Long id) {
        log.debug("Request to get Citizen : {}", id);
        Citizen citizen = citizenRepository.findOne(id);
        return citizen;
    }

    /**
     *  Delete the  citizen by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Citizen : {}", id);
        citizenRepository.delete(id);
    }
}
