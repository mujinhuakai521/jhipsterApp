package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Leader;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Leader entity.
 */
@SuppressWarnings("unused")
public interface LeaderRepository extends JpaRepository<Leader,Long> {

}
