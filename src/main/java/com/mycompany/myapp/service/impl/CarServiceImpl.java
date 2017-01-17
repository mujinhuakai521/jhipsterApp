package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CarService;
import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Car.
 */
@Service
@Transactional
public class CarServiceImpl implements CarService{

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);
    
    @Inject
    private CarRepository carRepository;

    /**
     * Save a car.
     *
     * @param car the entity to save
     * @return the persisted entity
     */
    public Car save(Car car) {
        log.debug("Request to save Car : {}", car);
        Car result = carRepository.save(car);
        return result;
    }

    /**
     *  Get all the cars.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Car> findAll(Pageable pageable) {
        log.debug("Request to get all Cars");
        Page<Car> result = carRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one car by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Car findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        Car car = carRepository.findOne(id);
        return car;
    }

    /**
     *  Delete the  car by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.delete(id);
    }
}
