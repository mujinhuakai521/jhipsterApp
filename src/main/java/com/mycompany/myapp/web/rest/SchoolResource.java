package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.School;
import com.mycompany.myapp.service.SchoolService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing School.
 */
@RestController
@RequestMapping("/api")
public class SchoolResource {

    private final Logger log = LoggerFactory.getLogger(SchoolResource.class);
        
    @Inject
    private SchoolService schoolService;

    /**
     * POST  /schools : Create a new school.
     *
     * @param school the school to create
     * @return the ResponseEntity with status 201 (Created) and with body the new school, or with status 400 (Bad Request) if the school has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schools")
    @Timed
    public ResponseEntity<School> createSchool(@RequestBody School school) throws URISyntaxException {
        log.debug("REST request to save School : {}", school);
        if (school.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("school", "idexists", "A new school cannot already have an ID")).body(null);
        }
        School result = schoolService.save(school);
        return ResponseEntity.created(new URI("/api/schools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("school", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schools : Updates an existing school.
     *
     * @param school the school to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated school,
     * or with status 400 (Bad Request) if the school is not valid,
     * or with status 500 (Internal Server Error) if the school couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schools")
    @Timed
    public ResponseEntity<School> updateSchool(@RequestBody School school) throws URISyntaxException {
        log.debug("REST request to update School : {}", school);
        if (school.getId() == null) {
            return createSchool(school);
        }
        School result = schoolService.save(school);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("school", school.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schools : get all the schools.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of schools in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schools")
    @Timed
    public ResponseEntity<List<School>> getAllSchools(@ApiParam Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("leader-is-null".equals(filter)) {
            log.debug("REST request to get all Schools where leader is null");
            return new ResponseEntity<>(schoolService.findAllWhereLeaderIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Schools");
        Page<School> page = schoolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /schools/:id : get the "id" school.
     *
     * @param id the id of the school to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the school, or with status 404 (Not Found)
     */
    @GetMapping("/schools/{id}")
    @Timed
    public ResponseEntity<School> getSchool(@PathVariable Long id) {
        log.debug("REST request to get School : {}", id);
        School school = schoolService.findOne(id);
        return Optional.ofNullable(school)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /schools/:id : delete the "id" school.
     *
     * @param id the id of the school to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schools/{id}")
    @Timed
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        log.debug("REST request to delete School : {}", id);
        schoolService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("school", id.toString())).build();
    }

}
