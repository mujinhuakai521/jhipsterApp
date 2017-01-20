package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Passport;
import com.mycompany.myapp.service.PassportService;
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
 * REST controller for managing Passport.
 */
@RestController
@RequestMapping("/api")
public class PassportResource {

    private final Logger log = LoggerFactory.getLogger(PassportResource.class);
        
    @Inject
    private PassportService passportService;

    /**
     * POST  /passports : Create a new passport.
     *
     * @param passport the passport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new passport, or with status 400 (Bad Request) if the passport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/passports")
    @Timed
    public ResponseEntity<Passport> createPassport(@RequestBody Passport passport) throws URISyntaxException {
        log.debug("REST request to save Passport : {}", passport);
        if (passport.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("passport", "idexists", "A new passport cannot already have an ID")).body(null);
        }
        Passport result = passportService.save(passport);
        return ResponseEntity.created(new URI("/api/passports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("passport", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /passports : Updates an existing passport.
     *
     * @param passport the passport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated passport,
     * or with status 400 (Bad Request) if the passport is not valid,
     * or with status 500 (Internal Server Error) if the passport couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/passports")
    @Timed
    public ResponseEntity<Passport> updatePassport(@RequestBody Passport passport) throws URISyntaxException {
        log.debug("REST request to update Passport : {}", passport);
        if (passport.getId() == null) {
            return createPassport(passport);
        }
        Passport result = passportService.save(passport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("passport", passport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /passports : get all the passports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of passports in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/passports")
    @Timed
    public ResponseEntity<List<Passport>> getAllPassports(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Passports");
        Page<Passport> page = passportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/passports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /passports/:id : get the "id" passport.
     *
     * @param id the id of the passport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the passport, or with status 404 (Not Found)
     */
    @GetMapping("/passports/{id}")
    @Timed
    public ResponseEntity<Passport> getPassport(@PathVariable Long id) {
        log.debug("REST request to get Passport : {}", id);
        Passport passport = passportService.findOne(id);
        return Optional.ofNullable(passport)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /passports/:id : delete the "id" passport.
     *
     * @param id the id of the passport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/passports/{id}")
    @Timed
    public ResponseEntity<Void> deletePassport(@PathVariable Long id) {
        log.debug("REST request to delete Passport : {}", id);
        passportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("passport", id.toString())).build();
    }

}
