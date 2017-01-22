package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Leader;
import com.mycompany.myapp.service.LeaderService;
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
 * REST controller for managing Leader.
 */
@RestController
@RequestMapping("/api")
public class LeaderResource {

    private final Logger log = LoggerFactory.getLogger(LeaderResource.class);
        
    @Inject
    private LeaderService leaderService;

    /**
     * POST  /leaders : Create a new leader.
     *
     * @param leader the leader to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leader, or with status 400 (Bad Request) if the leader has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leaders")
    @Timed
    public ResponseEntity<Leader> createLeader(@RequestBody Leader leader) throws URISyntaxException {
        log.debug("REST request to save Leader : {}", leader);
        if (leader.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("leader", "idexists", "A new leader cannot already have an ID")).body(null);
        }
        Leader result = leaderService.save(leader);
        return ResponseEntity.created(new URI("/api/leaders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("leader", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leaders : Updates an existing leader.
     *
     * @param leader the leader to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leader,
     * or with status 400 (Bad Request) if the leader is not valid,
     * or with status 500 (Internal Server Error) if the leader couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leaders")
    @Timed
    public ResponseEntity<Leader> updateLeader(@RequestBody Leader leader) throws URISyntaxException {
        log.debug("REST request to update Leader : {}", leader);
        if (leader.getId() == null) {
            return createLeader(leader);
        }
        Leader result = leaderService.save(leader);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("leader", leader.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leaders : get all the leaders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of leaders in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/leaders")
    @Timed
    public ResponseEntity<List<Leader>> getAllLeaders(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Leaders");
        Page<Leader> page = leaderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leaders/:id : get the "id" leader.
     *
     * @param id the id of the leader to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leader, or with status 404 (Not Found)
     */
    @GetMapping("/leaders/{id}")
    @Timed
    public ResponseEntity<Leader> getLeader(@PathVariable Long id) {
        log.debug("REST request to get Leader : {}", id);
        Leader leader = leaderService.findOne(id);
        return Optional.ofNullable(leader)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /leaders/:id : delete the "id" leader.
     *
     * @param id the id of the leader to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leaders/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeader(@PathVariable Long id) {
        log.debug("REST request to delete Leader : {}", id);
        leaderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("leader", id.toString())).build();
    }

}
