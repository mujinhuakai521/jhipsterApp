package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bus entity.
 */
@SuppressWarnings("unused")
public interface BusRepository extends JpaRepository<Bus,Long> {

}
