package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Human;
import com.mycompany.myapp.repository.HumanRepository;
import com.mycompany.myapp.service.HumanService;

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
 * Test class for the HumanResource REST controller.
 *
 * @see HumanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class HumanResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGE = "AAAAAAAAAA";
    private static final String UPDATED_AGE = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    @Inject
    private HumanRepository humanRepository;

    @Inject
    private HumanService humanService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHumanMockMvc;

    private Human human;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HumanResource humanResource = new HumanResource();
        ReflectionTestUtils.setField(humanResource, "humanService", humanService);
        this.restHumanMockMvc = MockMvcBuilders.standaloneSetup(humanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Human createEntity(EntityManager em) {
        Human human = new Human()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE)
                .sex(DEFAULT_SEX);
        return human;
    }

    @Before
    public void initTest() {
        human = createEntity(em);
    }

    @Test
    @Transactional
    public void createHuman() throws Exception {
        int databaseSizeBeforeCreate = humanRepository.findAll().size();

        // Create the Human

        restHumanMockMvc.perform(post("/api/humans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isCreated());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeCreate + 1);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHuman.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testHuman.getSex()).isEqualTo(DEFAULT_SEX);
    }

    @Test
    @Transactional
    public void createHumanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = humanRepository.findAll().size();

        // Create the Human with an existing ID
        Human existingHuman = new Human();
        existingHuman.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHumanMockMvc.perform(post("/api/humans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHuman)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHumans() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        // Get all the humanList
        restHumanMockMvc.perform(get("/api/humans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(human.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())));
    }

    @Test
    @Transactional
    public void getHuman() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        // Get the human
        restHumanMockMvc.perform(get("/api/humans/{id}", human.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(human.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHuman() throws Exception {
        // Get the human
        restHumanMockMvc.perform(get("/api/humans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHuman() throws Exception {
        // Initialize the database
        humanService.save(human);

        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Update the human
        Human updatedHuman = humanRepository.findOne(human.getId());
        updatedHuman
                .name(UPDATED_NAME)
                .age(UPDATED_AGE)
                .sex(UPDATED_SEX);

        restHumanMockMvc.perform(put("/api/humans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHuman)))
            .andExpect(status().isOk());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuman.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testHuman.getSex()).isEqualTo(UPDATED_SEX);
    }

    @Test
    @Transactional
    public void updateNonExistingHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Create the Human

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHumanMockMvc.perform(put("/api/humans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isCreated());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHuman() throws Exception {
        // Initialize the database
        humanService.save(human);

        int databaseSizeBeforeDelete = humanRepository.findAll().size();

        // Get the human
        restHumanMockMvc.perform(delete("/api/humans/{id}", human.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
