package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.PassportService;
import com.mycompany.myapp.domain.Passport;
import com.mycompany.myapp.repository.PassportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Passport.
 */
@Service
@Transactional
public class PassportServiceImpl implements PassportService{

    private final Logger log = LoggerFactory.getLogger(PassportServiceImpl.class);
    
    @Inject
    private PassportRepository passportRepository;

    /**
     * Save a passport.
     *
     * @param passport the entity to save
     * @return the persisted entity
     */
    public Passport save(Passport passport) {
        log.debug("Request to save Passport : {}", passport);
        Passport result = passportRepository.save(passport);
        return result;
    }

    /**
     *  Get all the passports.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Passport> findAll(Pageable pageable) {
        log.debug("Request to get all Passports");
        Page<Passport> result = passportRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one passport by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Passport findOne(Long id) {
        log.debug("Request to get Passport : {}", id);
        Passport passport = passportRepository.findOne(id);
        return passport;
    }

    /**
     *  Delete the  passport by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Passport : {}", id);
        passportRepository.delete(id);
    }
}
