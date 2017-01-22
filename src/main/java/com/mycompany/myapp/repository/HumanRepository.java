package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Human;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Human entity.
 */
@SuppressWarnings("unused")
public interface HumanRepository extends JpaRepository<Human,Long> {

}
