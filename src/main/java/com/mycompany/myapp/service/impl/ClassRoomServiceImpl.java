package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ClassRoomService;
import com.mycompany.myapp.domain.ClassRoom;
import com.mycompany.myapp.repository.ClassRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ClassRoom.
 */
@Service
@Transactional
public class ClassRoomServiceImpl implements ClassRoomService{

    private final Logger log = LoggerFactory.getLogger(ClassRoomServiceImpl.class);
    
    @Inject
    private ClassRoomRepository classRoomRepository;

    /**
     * Save a classRoom.
     *
     * @param classRoom the entity to save
     * @return the persisted entity
     */
    public ClassRoom save(ClassRoom classRoom) {
        log.debug("Request to save ClassRoom : {}", classRoom);
        ClassRoom result = classRoomRepository.save(classRoom);
        return result;
    }

    /**
     *  Get all the classRooms.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ClassRoom> findAll(Pageable pageable) {
        log.debug("Request to get all ClassRooms");
        Page<ClassRoom> result = classRoomRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one classRoom by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ClassRoom findOne(Long id) {
        log.debug("Request to get ClassRoom : {}", id);
        ClassRoom classRoom = classRoomRepository.findOne(id);
        return classRoom;
    }

    /**
     *  Delete the  classRoom by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ClassRoom : {}", id);
        classRoomRepository.delete(id);
    }
}
