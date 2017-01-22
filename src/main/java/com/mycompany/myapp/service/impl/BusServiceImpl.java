package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BusService;
import com.mycompany.myapp.domain.Bus;
import com.mycompany.myapp.repository.BusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Bus.
 */
@Service
@Transactional
public class BusServiceImpl implements BusService{

    private final Logger log = LoggerFactory.getLogger(BusServiceImpl.class);
    
    @Inject
    private BusRepository busRepository;

    /**
     * Save a bus.
     *
     * @param bus the entity to save
     * @return the persisted entity
     */
    public Bus save(Bus bus) {
        log.debug("Request to save Bus : {}", bus);
        Bus result = busRepository.save(bus);
        return result;
    }

    /**
     *  Get all the buses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Bus> findAll(Pageable pageable) {
        log.debug("Request to get all Buses");
        Page<Bus> result = busRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one bus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Bus findOne(Long id) {
        log.debug("Request to get Bus : {}", id);
        Bus bus = busRepository.findOne(id);
        return bus;
    }

    /**
     *  Delete the  bus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bus : {}", id);
        busRepository.delete(id);
    }
}
