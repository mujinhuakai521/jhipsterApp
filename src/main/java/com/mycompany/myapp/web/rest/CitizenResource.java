package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Citizen;
import com.mycompany.myapp.service.CitizenService;
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

/**
 * REST controller for managing Citizen.
 */
@RestController
@RequestMapping("/api")
public class CitizenResource {

    private final Logger log = LoggerFactory.getLogger(CitizenResource.class);
        
    @Inject
    private CitizenService citizenService;

    /**
     * POST  /citizens : Create a new citizen.
     *
     * @param citizen the citizen to create
     * @return the ResponseEntity with status 201 (Created) and with body the new citizen, or with status 400 (Bad Request) if the citizen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/citizens")
    @Timed
    public ResponseEntity<Citizen> createCitizen(@RequestBody Citizen citizen) throws URISyntaxException {
        log.debug("REST request to save Citizen : {}", citizen);
        if (citizen.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("citizen", "idexists", "A new citizen cannot already have an ID")).body(null);
        }
        Citizen result = citizenService.save(citizen);
        return ResponseEntity.created(new URI("/api/citizens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("citizen", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /citizens : Updates an existing citizen.
     *
     * @param citizen the citizen to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated citizen,
     * or with status 400 (Bad Request) if the citizen is not valid,
     * or with status 500 (Internal Server Error) if the citizen couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/citizens")
    @Timed
    public ResponseEntity<Citizen> updateCitizen(@RequestBody Citizen citizen) throws URISyntaxException {
        log.debug("REST request to update Citizen : {}", citizen);
        if (citizen.getId() == null) {
            return createCitizen(citizen);
        }
        Citizen result = citizenService.save(citizen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("citizen", citizen.getId().toString()))
            .body(result);
    }

    /**
     * GET  /citizens : get all the citizens.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of citizens in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/citizens")
    @Timed
    public ResponseEntity<List<Citizen>> getAllCitizens(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Citizens");
        Page<Citizen> page = citizenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/citizens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /citizens/:id : get the "id" citizen.
     *
     * @param id the id of the citizen to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the citizen, or with status 404 (Not Found)
     */
    @GetMapping("/citizens/{id}")
    @Timed
    public ResponseEntity<Citizen> getCitizen(@PathVariable Long id) {
        log.debug("REST request to get Citizen : {}", id);
        Citizen citizen = citizenService.findOne(id);
        return Optional.ofNullable(citizen)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /citizens/:id : delete the "id" citizen.
     *
     * @param id the id of the citizen to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/citizens/{id}")
    @Timed
    public ResponseEntity<Void> deleteCitizen(@PathVariable Long id) {
        log.debug("REST request to delete Citizen : {}", id);
        citizenService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("citizen", id.toString())).build();
    }

}
