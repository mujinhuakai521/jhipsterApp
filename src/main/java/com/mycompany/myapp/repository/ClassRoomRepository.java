package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ClassRoom;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClassRoom entity.
 */
@SuppressWarnings("unused")
public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {

}
