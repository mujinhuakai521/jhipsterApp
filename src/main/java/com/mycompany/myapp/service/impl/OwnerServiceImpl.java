package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.OwnerService;
import com.mycompany.myapp.domain.Owner;
import com.mycompany.myapp.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Owner.
 */
@Service
@Transactional
public class OwnerServiceImpl implements OwnerService{

    private final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);
    
    @Inject
    private OwnerRepository ownerRepository;

    /**
     * Save a owner.
     *
     * @param owner the entity to save
     * @return the persisted entity
     */
    public Owner save(Owner owner) {
        log.debug("Request to save Owner : {}", owner);
        Owner result = ownerRepository.save(owner);
        return result;
    }

    /**
     *  Get all the owners.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Owner> findAll(Pageable pageable) {
        log.debug("Request to get all Owners");
        Page<Owner> result = ownerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one owner by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Owner findOne(Long id) {
        log.debug("Request to get Owner : {}", id);
        Owner owner = ownerRepository.findOne(id);
        return owner;
    }

    /**
     *  Delete the  owner by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Owner : {}", id);
        ownerRepository.delete(id);
    }
}
