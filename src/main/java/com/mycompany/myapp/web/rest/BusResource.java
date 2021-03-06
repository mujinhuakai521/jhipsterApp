package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Bus;
import com.mycompany.myapp.service.BusService;
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
 * REST controller for managing Bus.
 */
@RestController
@RequestMapping("/api")
public class BusResource {

    private final Logger log = LoggerFactory.getLogger(BusResource.class);
        
    @Inject
    private BusService busService;

    /**
     * POST  /buses : Create a new bus.
     *
     * @param bus the bus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bus, or with status 400 (Bad Request) if the bus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/buses")
    @Timed
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) throws URISyntaxException {
        log.debug("REST request to save Bus : {}", bus);
        if (bus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bus", "idexists", "A new bus cannot already have an ID")).body(null);
        }
        Bus result = busService.save(bus);
        return ResponseEntity.created(new URI("/api/buses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /buses : Updates an existing bus.
     *
     * @param bus the bus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bus,
     * or with status 400 (Bad Request) if the bus is not valid,
     * or with status 500 (Internal Server Error) if the bus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/buses")
    @Timed
    public ResponseEntity<Bus> updateBus(@RequestBody Bus bus) throws URISyntaxException {
        log.debug("REST request to update Bus : {}", bus);
        if (bus.getId() == null) {
            return createBus(bus);
        }
        Bus result = busService.save(bus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bus", bus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /buses : get all the buses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of buses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/buses")
    @Timed
    public ResponseEntity<List<Bus>> getAllBuses(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Buses");
        Page<Bus> page = busService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/buses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /buses/:id : get the "id" bus.
     *
     * @param id the id of the bus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bus, or with status 404 (Not Found)
     */
    @GetMapping("/buses/{id}")
    @Timed
    public ResponseEntity<Bus> getBus(@PathVariable Long id) {
        log.debug("REST request to get Bus : {}", id);
        Bus bus = busService.findOne(id);
        return Optional.ofNullable(bus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /buses/:id : delete the "id" bus.
     *
     * @param id the id of the bus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/buses/{id}")
    @Timed
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        log.debug("REST request to delete Bus : {}", id);
        busService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bus", id.toString())).build();
    }

}
