package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Human;
import com.mycompany.myapp.service.HumanService;
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
 * REST controller for managing Human.
 */
@RestController
@RequestMapping("/api")
public class HumanResource {

    private final Logger log = LoggerFactory.getLogger(HumanResource.class);
        
    @Inject
    private HumanService humanService;

    /**
     * POST  /humans : Create a new human.
     *
     * @param human the human to create
     * @return the ResponseEntity with status 201 (Created) and with body the new human, or with status 400 (Bad Request) if the human has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/humans")
    @Timed
    public ResponseEntity<Human> createHuman(@RequestBody Human human) throws URISyntaxException {
        log.debug("REST request to save Human : {}", human);
        if (human.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("human", "idexists", "A new human cannot already have an ID")).body(null);
        }
        Human result = humanService.save(human);
        return ResponseEntity.created(new URI("/api/humans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("human", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /humans : Updates an existing human.
     *
     * @param human the human to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated human,
     * or with status 400 (Bad Request) if the human is not valid,
     * or with status 500 (Internal Server Error) if the human couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/humans")
    @Timed
    public ResponseEntity<Human> updateHuman(@RequestBody Human human) throws URISyntaxException {
        log.debug("REST request to update Human : {}", human);
        if (human.getId() == null) {
            return createHuman(human);
        }
        Human result = humanService.save(human);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("human", human.getId().toString()))
            .body(result);
    }

    /**
     * GET  /humans : get all the humans.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of humans in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/humans")
    @Timed
    public ResponseEntity<List<Human>> getAllHumans(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Humans");
        Page<Human> page = humanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/humans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /humans/:id : get the "id" human.
     *
     * @param id the id of the human to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the human, or with status 404 (Not Found)
     */
    @GetMapping("/humans/{id}")
    @Timed
    public ResponseEntity<Human> getHuman(@PathVariable Long id) {
        log.debug("REST request to get Human : {}", id);
        Human human = humanService.findOne(id);
        return Optional.ofNullable(human)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /humans/:id : delete the "id" human.
     *
     * @param id the id of the human to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/humans/{id}")
    @Timed
    public ResponseEntity<Void> deleteHuman(@PathVariable Long id) {
        log.debug("REST request to delete Human : {}", id);
        humanService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("human", id.toString())).build();
    }

}
