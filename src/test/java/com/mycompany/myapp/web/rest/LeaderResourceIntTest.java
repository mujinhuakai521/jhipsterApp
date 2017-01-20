package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Leader;
import com.mycompany.myapp.repository.LeaderRepository;
import com.mycompany.myapp.service.LeaderService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LeaderResource REST controller.
 *
 * @see LeaderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class LeaderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGE = "AAAAAAAAAA";
    private static final String UPDATED_AGE = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    @Inject
    private LeaderRepository leaderRepository;

    @Inject
    private LeaderService leaderService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLeaderMockMvc;

    private Leader leader;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LeaderResource leaderResource = new LeaderResource();
        ReflectionTestUtils.setField(leaderResource, "leaderService", leaderService);
        this.restLeaderMockMvc = MockMvcBuilders.standaloneSetup(leaderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leader createEntity(EntityManager em) {
        Leader leader = new Leader()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE)
                .sex(DEFAULT_SEX);
        return leader;
    }

    @Before
    public void initTest() {
        leader = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeader() throws Exception {
        int databaseSizeBeforeCreate = leaderRepository.findAll().size();

        // Create the Leader

        restLeaderMockMvc.perform(post("/api/leaders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leader)))
            .andExpect(status().isCreated());

        // Validate the Leader in the database
        List<Leader> leaderList = leaderRepository.findAll();
        assertThat(leaderList).hasSize(databaseSizeBeforeCreate + 1);
        Leader testLeader = leaderList.get(leaderList.size() - 1);
        assertThat(testLeader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLeader.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testLeader.getSex()).isEqualTo(DEFAULT_SEX);
    }

    @Test
    @Transactional
    public void createLeaderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaderRepository.findAll().size();

        // Create the Leader with an existing ID
        Leader existingLeader = new Leader();
        existingLeader.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaderMockMvc.perform(post("/api/leaders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLeader)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Leader> leaderList = leaderRepository.findAll();
        assertThat(leaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLeaders() throws Exception {
        // Initialize the database
        leaderRepository.saveAndFlush(leader);

        // Get all the leaderList
        restLeaderMockMvc.perform(get("/api/leaders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leader.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())));
    }

    @Test
    @Transactional
    public void getLeader() throws Exception {
        // Initialize the database
        leaderRepository.saveAndFlush(leader);

        // Get the leader
        restLeaderMockMvc.perform(get("/api/leaders/{id}", leader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leader.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLeader() throws Exception {
        // Get the leader
        restLeaderMockMvc.perform(get("/api/leaders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeader() throws Exception {
        // Initialize the database
        leaderService.save(leader);

        int databaseSizeBeforeUpdate = leaderRepository.findAll().size();

        // Update the leader
        Leader updatedLeader = leaderRepository.findOne(leader.getId());
        updatedLeader
                .name(UPDATED_NAME)
                .age(UPDATED_AGE)
                .sex(UPDATED_SEX);

        restLeaderMockMvc.perform(put("/api/leaders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLeader)))
            .andExpect(status().isOk());

        // Validate the Leader in the database
        List<Leader> leaderList = leaderRepository.findAll();
        assertThat(leaderList).hasSize(databaseSizeBeforeUpdate);
        Leader testLeader = leaderList.get(leaderList.size() - 1);
        assertThat(testLeader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLeader.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testLeader.getSex()).isEqualTo(UPDATED_SEX);
    }

    @Test
    @Transactional
    public void updateNonExistingLeader() throws Exception {
        int databaseSizeBeforeUpdate = leaderRepository.findAll().size();

        // Create the Leader

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeaderMockMvc.perform(put("/api/leaders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leader)))
            .andExpect(status().isCreated());

        // Validate the Leader in the database
        List<Leader> leaderList = leaderRepository.findAll();
        assertThat(leaderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLeader() throws Exception {
        // Initialize the database
        leaderService.save(leader);

        int databaseSizeBeforeDelete = leaderRepository.findAll().size();

        // Get the leader
        restLeaderMockMvc.perform(delete("/api/leaders/{id}", leader.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Leader> leaderList = leaderRepository.findAll();
        assertThat(leaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
