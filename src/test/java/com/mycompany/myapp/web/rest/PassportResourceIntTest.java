package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Passport;
import com.mycompany.myapp.repository.PassportRepository;
import com.mycompany.myapp.service.PassportService;

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
 * Test class for the PassportResource REST controller.
 *
 * @see PassportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class PassportResourceIntTest {

    private static final String DEFAULT_PASSPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_NUMBER = "BBBBBBBBBB";

    @Inject
    private PassportRepository passportRepository;

    @Inject
    private PassportService passportService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPassportMockMvc;

    private Passport passport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PassportResource passportResource = new PassportResource();
        ReflectionTestUtils.setField(passportResource, "passportService", passportService);
        this.restPassportMockMvc = MockMvcBuilders.standaloneSetup(passportResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passport createEntity(EntityManager em) {
        Passport passport = new Passport()
                .passportNumber(DEFAULT_PASSPORT_NUMBER);
        return passport;
    }

    @Before
    public void initTest() {
        passport = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassport() throws Exception {
        int databaseSizeBeforeCreate = passportRepository.findAll().size();

        // Create the Passport

        restPassportMockMvc.perform(post("/api/passports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isCreated());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeCreate + 1);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
    }

    @Test
    @Transactional
    public void createPassportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passportRepository.findAll().size();

        // Create the Passport with an existing ID
        Passport existingPassport = new Passport();
        existingPassport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassportMockMvc.perform(post("/api/passports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPassport)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPassports() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        // Get all the passportList
        restPassportMockMvc.perform(get("/api/passports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passport.getId().intValue())))
            .andExpect(jsonPath("$.[*].passportNumber").value(hasItem(DEFAULT_PASSPORT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getPassport() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        // Get the passport
        restPassportMockMvc.perform(get("/api/passports/{id}", passport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(passport.getId().intValue()))
            .andExpect(jsonPath("$.passportNumber").value(DEFAULT_PASSPORT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPassport() throws Exception {
        // Get the passport
        restPassportMockMvc.perform(get("/api/passports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassport() throws Exception {
        // Initialize the database
        passportService.save(passport);

        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Update the passport
        Passport updatedPassport = passportRepository.findOne(passport.getId());
        updatedPassport
                .passportNumber(UPDATED_PASSPORT_NUMBER);

        restPassportMockMvc.perform(put("/api/passports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPassport)))
            .andExpect(status().isOk());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Create the Passport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPassportMockMvc.perform(put("/api/passports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passport)))
            .andExpect(status().isCreated());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePassport() throws Exception {
        // Initialize the database
        passportService.save(passport);

        int databaseSizeBeforeDelete = passportRepository.findAll().size();

        // Get the passport
        restPassportMockMvc.perform(delete("/api/passports/{id}", passport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
