package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BikeService;
import com.mycompany.myapp.domain.Bike;
import com.mycompany.myapp.repository.BikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Bike.
 */
@Service
@Transactional
public class BikeServiceImpl implements BikeService{

    private final Logger log = LoggerFactory.getLogger(BikeServiceImpl.class);
    
    @Inject
    private BikeRepository bikeRepository;

    /**
     * Save a bike.
     *
     * @param bike the entity to save
     * @return the persisted entity
     */
    public Bike save(Bike bike) {
        log.debug("Request to save Bike : {}", bike);
        Bike result = bikeRepository.save(bike);
        return result;
    }

    /**
     *  Get all the bikes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Bike> findAll(Pageable pageable) {
        log.debug("Request to get all Bikes");
        Page<Bike> result = bikeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one bike by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Bike findOne(Long id) {
        log.debug("Request to get Bike : {}", id);
        Bike bike = bikeRepository.findOneWithEagerRelationships(id);
        return bike;
    }

    /**
     *  Delete the  bike by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bike : {}", id);
        bikeRepository.delete(id);
    }
}
