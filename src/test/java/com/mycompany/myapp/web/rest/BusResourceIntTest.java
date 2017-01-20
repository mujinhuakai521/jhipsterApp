package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Bus;
import com.mycompany.myapp.repository.BusRepository;
import com.mycompany.myapp.service.BusService;

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
 * Test class for the BusResource REST controller.
 *
 * @see BusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class BusResourceIntTest {

    private static final String DEFAULT_BUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_CAR_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CAR_NUMBER = "BBBBBBBBBB";

    @Inject
    private BusRepository busRepository;

    @Inject
    private BusService busService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBusMockMvc;

    private Bus bus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusResource busResource = new BusResource();
        ReflectionTestUtils.setField(busResource, "busService", busService);
        this.restBusMockMvc = MockMvcBuilders.standaloneSetup(busResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bus createEntity(EntityManager em) {
        Bus bus = new Bus()
                .busName(DEFAULT_BUS_NAME)
                .color(DEFAULT_COLOR)
                .carNumber(DEFAULT_CAR_NUMBER);
        return bus;
    }

    @Before
    public void initTest() {
        bus = createEntity(em);
    }

    @Test
    @Transactional
    public void createBus() throws Exception {
        int databaseSizeBeforeCreate = busRepository.findAll().size();

        // Create the Bus

        restBusMockMvc.perform(post("/api/buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bus)))
            .andExpect(status().isCreated());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeCreate + 1);
        Bus testBus = busList.get(busList.size() - 1);
        assertThat(testBus.getBusName()).isEqualTo(DEFAULT_BUS_NAME);
        assertThat(testBus.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testBus.getCarNumber()).isEqualTo(DEFAULT_CAR_NUMBER);
    }

    @Test
    @Transactional
    public void createBusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = busRepository.findAll().size();

        // Create the Bus with an existing ID
        Bus existingBus = new Bus();
        existingBus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusMockMvc.perform(post("/api/buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBuses() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the busList
        restBusMockMvc.perform(get("/api/buses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bus.getId().intValue())))
            .andExpect(jsonPath("$.[*].busName").value(hasItem(DEFAULT_BUS_NAME.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].carNumber").value(hasItem(DEFAULT_CAR_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get the bus
        restBusMockMvc.perform(get("/api/buses/{id}", bus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bus.getId().intValue()))
            .andExpect(jsonPath("$.busName").value(DEFAULT_BUS_NAME.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.carNumber").value(DEFAULT_CAR_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBus() throws Exception {
        // Get the bus
        restBusMockMvc.perform(get("/api/buses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBus() throws Exception {
        // Initialize the database
        busService.save(bus);

        int databaseSizeBeforeUpdate = busRepository.findAll().size();

        // Update the bus
        Bus updatedBus = busRepository.findOne(bus.getId());
        updatedBus
                .busName(UPDATED_BUS_NAME)
                .color(UPDATED_COLOR)
                .carNumber(UPDATED_CAR_NUMBER);

        restBusMockMvc.perform(put("/api/buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBus)))
            .andExpect(status().isOk());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate);
        Bus testBus = busList.get(busList.size() - 1);
        assertThat(testBus.getBusName()).isEqualTo(UPDATED_BUS_NAME);
        assertThat(testBus.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testBus.getCarNumber()).isEqualTo(UPDATED_CAR_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingBus() throws Exception {
        int databaseSizeBeforeUpdate = busRepository.findAll().size();

        // Create the Bus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusMockMvc.perform(put("/api/buses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bus)))
            .andExpect(status().isCreated());

        // Validate the Bus in the database
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBus() throws Exception {
        // Initialize the database
        busService.save(bus);

        int databaseSizeBeforeDelete = busRepository.findAll().size();

        // Get the bus
        restBusMockMvc.perform(delete("/api/buses/{id}", bus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bus> busList = busRepository.findAll();
        assertThat(busList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
