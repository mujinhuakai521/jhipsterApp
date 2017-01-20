package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bike;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Bike entity.
 */
@SuppressWarnings("unused")
public interface BikeRepository extends JpaRepository<Bike,Long> {

    @Query("select distinct bike from Bike bike left join fetch bike.drivers")
    List<Bike> findAllWithEagerRelationships();

    @Query("select bike from Bike bike left join fetch bike.drivers where bike.id =:id")
    Bike findOneWithEagerRelationships(@Param("id") Long id);

}
