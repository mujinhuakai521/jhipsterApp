package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.LeaderService;
import com.mycompany.myapp.domain.Leader;
import com.mycompany.myapp.repository.LeaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Leader.
 */
@Service
@Transactional
public class LeaderServiceImpl implements LeaderService{

    private final Logger log = LoggerFactory.getLogger(LeaderServiceImpl.class);
    
    @Inject
    private LeaderRepository leaderRepository;

    /**
     * Save a leader.
     *
     * @param leader the entity to save
     * @return the persisted entity
     */
    public Leader save(Leader leader) {
        log.debug("Request to save Leader : {}", leader);
        Leader result = leaderRepository.save(leader);
        return result;
    }

    /**
     *  Get all the leaders.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Leader> findAll(Pageable pageable) {
        log.debug("Request to get all Leaders");
        Page<Leader> result = leaderRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one leader by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Leader findOne(Long id) {
        log.debug("Request to get Leader : {}", id);
        Leader leader = leaderRepository.findOne(id);
        return leader;
    }

    /**
     *  Delete the  leader by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Leader : {}", id);
        leaderRepository.delete(id);
    }
}
