package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.DriverService;
import com.mycompany.myapp.domain.Driver;
import com.mycompany.myapp.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Driver.
 */
@Service
@Transactional
public class DriverServiceImpl implements DriverService{

    private final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);
    
    @Inject
    private DriverRepository driverRepository;

    /**
     * Save a driver.
     *
     * @param driver the entity to save
     * @return the persisted entity
     */
    public Driver save(Driver driver) {
        log.debug("Request to save Driver : {}", driver);
        Driver result = driverRepository.save(driver);
        return result;
    }

    /**
     *  Get all the drivers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Driver> findAll(Pageable pageable) {
        log.debug("Request to get all Drivers");
        Page<Driver> result = driverRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one driver by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Driver findOne(Long id) {
        log.debug("Request to get Driver : {}", id);
        Driver driver = driverRepository.findOne(id);
        return driver;
    }

    /**
     *  Delete the  driver by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Driver : {}", id);
        driverRepository.delete(id);
    }
}
