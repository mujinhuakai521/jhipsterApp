package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Bike;
import com.mycompany.myapp.repository.BikeRepository;
import com.mycompany.myapp.service.BikeService;

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
 * Test class for the BikeResource REST controller.
 *
 * @see BikeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class BikeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    @Inject
    private BikeRepository bikeRepository;

    @Inject
    private BikeService bikeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBikeMockMvc;

    private Bike bike;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BikeResource bikeResource = new BikeResource();
        ReflectionTestUtils.setField(bikeResource, "bikeService", bikeService);
        this.restBikeMockMvc = MockMvcBuilders.standaloneSetup(bikeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bike createEntity(EntityManager em) {
        Bike bike = new Bike()
                .name(DEFAULT_NAME)
                .color(DEFAULT_COLOR)
                .price(DEFAULT_PRICE);
        return bike;
    }

    @Before
    public void initTest() {
        bike = createEntity(em);
    }

    @Test
    @Transactional
    public void createBike() throws Exception {
        int databaseSizeBeforeCreate = bikeRepository.findAll().size();

        // Create the Bike

        restBikeMockMvc.perform(post("/api/bikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bike)))
            .andExpect(status().isCreated());

        // Validate the Bike in the database
        List<Bike> bikeList = bikeRepository.findAll();
        assertThat(bikeList).hasSize(databaseSizeBeforeCreate + 1);
        Bike testBike = bikeList.get(bikeList.size() - 1);
        assertThat(testBike.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBike.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testBike.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createBikeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bikeRepository.findAll().size();

        // Create the Bike with an existing ID
        Bike existingBike = new Bike();
        existingBike.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBikeMockMvc.perform(post("/api/bikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBike)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bike> bikeList = bikeRepository.findAll();
        assertThat(bikeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBikes() throws Exception {
        // Initialize the database
        bikeRepository.saveAndFlush(bike);

        // Get all the bikeList
        restBikeMockMvc.perform(get("/api/bikes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bike.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())));
    }

    @Test
    @Transactional
    public void getBike() throws Exception {
        // Initialize the database
        bikeRepository.saveAndFlush(bike);

        // Get the bike
        restBikeMockMvc.perform(get("/api/bikes/{id}", bike.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bike.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBike() throws Exception {
        // Get the bike
        restBikeMockMvc.perform(get("/api/bikes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBike() throws Exception {
        // Initialize the database
        bikeService.save(bike);

        int databaseSizeBeforeUpdate = bikeRepository.findAll().size();

        // Update the bike
        Bike updatedBike = bikeRepository.findOne(bike.getId());
        updatedBike
                .name(UPDATED_NAME)
                .color(UPDATED_COLOR)
                .price(UPDATED_PRICE);

        restBikeMockMvc.perform(put("/api/bikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBike)))
            .andExpect(status().isOk());

        // Validate the Bike in the database
        List<Bike> bikeList = bikeRepository.findAll();
        assertThat(bikeList).hasSize(databaseSizeBeforeUpdate);
        Bike testBike = bikeList.get(bikeList.size() - 1);
        assertThat(testBike.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBike.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testBike.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingBike() throws Exception {
        int databaseSizeBeforeUpdate = bikeRepository.findAll().size();

        // Create the Bike

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBikeMockMvc.perform(put("/api/bikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bike)))
            .andExpect(status().isCreated());

        // Validate the Bike in the database
        List<Bike> bikeList = bikeRepository.findAll();
        assertThat(bikeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBike() throws Exception {
        // Initialize the database
        bikeService.save(bike);

        int databaseSizeBeforeDelete = bikeRepository.findAll().size();

        // Get the bike
        restBikeMockMvc.perform(delete("/api/bikes/{id}", bike.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bike> bikeList = bikeRepository.findAll();
        assertThat(bikeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
