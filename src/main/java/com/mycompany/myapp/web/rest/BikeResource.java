package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Bike;
import com.mycompany.myapp.service.BikeService;
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
 * REST controller for managing Bike.
 */
@RestController
@RequestMapping("/api")
public class BikeResource {

    private final Logger log = LoggerFactory.getLogger(BikeResource.class);
        
    @Inject
    private BikeService bikeService;

    /**
     * POST  /bikes : Create a new bike.
     *
     * @param bike the bike to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bike, or with status 400 (Bad Request) if the bike has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bikes")
    @Timed
    public ResponseEntity<Bike> createBike(@RequestBody Bike bike) throws URISyntaxException {
        log.debug("REST request to save Bike : {}", bike);
        if (bike.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bike", "idexists", "A new bike cannot already have an ID")).body(null);
        }
        Bike result = bikeService.save(bike);
        return ResponseEntity.created(new URI("/api/bikes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bike", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bikes : Updates an existing bike.
     *
     * @param bike the bike to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bike,
     * or with status 400 (Bad Request) if the bike is not valid,
     * or with status 500 (Internal Server Error) if the bike couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bikes")
    @Timed
    public ResponseEntity<Bike> updateBike(@RequestBody Bike bike) throws URISyntaxException {
        log.debug("REST request to update Bike : {}", bike);
        if (bike.getId() == null) {
            return createBike(bike);
        }
        Bike result = bikeService.save(bike);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bike", bike.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bikes : get all the bikes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bikes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bikes")
    @Timed
    public ResponseEntity<List<Bike>> getAllBikes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bikes");
        Page<Bike> page = bikeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bikes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bikes/:id : get the "id" bike.
     *
     * @param id the id of the bike to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bike, or with status 404 (Not Found)
     */
    @GetMapping("/bikes/{id}")
    @Timed
    public ResponseEntity<Bike> getBike(@PathVariable Long id) {
        log.debug("REST request to get Bike : {}", id);
        Bike bike = bikeService.findOne(id);
        return Optional.ofNullable(bike)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bikes/:id : delete the "id" bike.
     *
     * @param id the id of the bike to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bikes/{id}")
    @Timed
    public ResponseEntity<Void> deleteBike(@PathVariable Long id) {
        log.debug("REST request to delete Bike : {}", id);
        bikeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bike", id.toString())).build();
    }

}
