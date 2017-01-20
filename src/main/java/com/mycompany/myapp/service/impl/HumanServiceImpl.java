package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.HumanService;
import com.mycompany.myapp.domain.Human;
import com.mycompany.myapp.repository.HumanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Human.
 */
@Service
@Transactional
public class HumanServiceImpl implements HumanService{

    private final Logger log = LoggerFactory.getLogger(HumanServiceImpl.class);
    
    @Inject
    private HumanRepository humanRepository;

    /**
     * Save a human.
     *
     * @param human the entity to save
     * @return the persisted entity
     */
    public Human save(Human human) {
        log.debug("Request to save Human : {}", human);
        Human result = humanRepository.save(human);
        return result;
    }

    /**
     *  Get all the humans.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Human> findAll(Pageable pageable) {
        log.debug("Request to get all Humans");
        Page<Human> result = humanRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one human by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Human findOne(Long id) {
        log.debug("Request to get Human : {}", id);
        Human human = humanRepository.findOne(id);
        return human;
    }

    /**
     *  Delete the  human by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Human : {}", id);
        humanRepository.delete(id);
    }
}
