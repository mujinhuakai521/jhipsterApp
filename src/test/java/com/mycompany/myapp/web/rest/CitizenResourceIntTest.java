package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Citizen;
import com.mycompany.myapp.repository.CitizenRepository;
import com.mycompany.myapp.service.CitizenService;

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
 * Test class for the CitizenResource REST controller.
 *
 * @see CitizenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class CitizenResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGE = "AAAAAAAAAA";
    private static final String UPDATED_AGE = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    @Inject
    private CitizenRepository citizenRepository;

    @Inject
    private CitizenService citizenService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCitizenMockMvc;

    private Citizen citizen;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CitizenResource citizenResource = new CitizenResource();
        ReflectionTestUtils.setField(citizenResource, "citizenService", citizenService);
        this.restCitizenMockMvc = MockMvcBuilders.standaloneSetup(citizenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Citizen createEntity(EntityManager em) {
        Citizen citizen = new Citizen()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE)
                .sex(DEFAULT_SEX);
        return citizen;
    }

    @Before
    public void initTest() {
        citizen = createEntity(em);
    }

    @Test
    @Transactional
    public void createCitizen() throws Exception {
        int databaseSizeBeforeCreate = citizenRepository.findAll().size();

        // Create the Citizen

        restCitizenMockMvc.perform(post("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(citizen)))
            .andExpect(status().isCreated());

        // Validate the Citizen in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeCreate + 1);
        Citizen testCitizen = citizenList.get(citizenList.size() - 1);
        assertThat(testCitizen.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCitizen.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCitizen.getSex()).isEqualTo(DEFAULT_SEX);
    }

    @Test
    @Transactional
    public void createCitizenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = citizenRepository.findAll().size();

        // Create the Citizen with an existing ID
        Citizen existingCitizen = new Citizen();
        existingCitizen.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitizenMockMvc.perform(post("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCitizen)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCitizens() throws Exception {
        // Initialize the database
        citizenRepository.saveAndFlush(citizen);

        // Get all the citizenList
        restCitizenMockMvc.perform(get("/api/citizens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(citizen.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())));
    }

    @Test
    @Transactional
    public void getCitizen() throws Exception {
        // Initialize the database
        citizenRepository.saveAndFlush(citizen);

        // Get the citizen
        restCitizenMockMvc.perform(get("/api/citizens/{id}", citizen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(citizen.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCitizen() throws Exception {
        // Get the citizen
        restCitizenMockMvc.perform(get("/api/citizens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCitizen() throws Exception {
        // Initialize the database
        citizenService.save(citizen);

        int databaseSizeBeforeUpdate = citizenRepository.findAll().size();

        // Update the citizen
        Citizen updatedCitizen = citizenRepository.findOne(citizen.getId());
        updatedCitizen
                .name(UPDATED_NAME)
                .age(UPDATED_AGE)
                .sex(UPDATED_SEX);

        restCitizenMockMvc.perform(put("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCitizen)))
            .andExpect(status().isOk());

        // Validate the Citizen in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeUpdate);
        Citizen testCitizen = citizenList.get(citizenList.size() - 1);
        assertThat(testCitizen.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCitizen.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCitizen.getSex()).isEqualTo(UPDATED_SEX);
    }

    @Test
    @Transactional
    public void updateNonExistingCitizen() throws Exception {
        int databaseSizeBeforeUpdate = citizenRepository.findAll().size();

        // Create the Citizen

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCitizenMockMvc.perform(put("/api/citizens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(citizen)))
            .andExpect(status().isCreated());

        // Validate the Citizen in the database
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCitizen() throws Exception {
        // Initialize the database
        citizenService.save(citizen);

        int databaseSizeBeforeDelete = citizenRepository.findAll().size();

        // Get the citizen
        restCitizenMockMvc.perform(delete("/api/citizens/{id}", citizen.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Citizen> citizenList = citizenRepository.findAll();
        assertThat(citizenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
