package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Passport;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Passport entity.
 */
@SuppressWarnings("unused")
public interface PassportRepository extends JpaRepository<Passport,Long> {

}
