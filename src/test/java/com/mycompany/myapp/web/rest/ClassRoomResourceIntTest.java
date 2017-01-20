package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.ClassRoom;
import com.mycompany.myapp.repository.ClassRoomRepository;
import com.mycompany.myapp.service.ClassRoomService;

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
 * Test class for the ClassRoomResource REST controller.
 *
 * @see ClassRoomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class ClassRoomResourceIntTest {

    @Inject
    private ClassRoomRepository classRoomRepository;

    @Inject
    private ClassRoomService classRoomService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClassRoomMockMvc;

    private ClassRoom classRoom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassRoomResource classRoomResource = new ClassRoomResource();
        ReflectionTestUtils.setField(classRoomResource, "classRoomService", classRoomService);
        this.restClassRoomMockMvc = MockMvcBuilders.standaloneSetup(classRoomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassRoom createEntity(EntityManager em) {
        ClassRoom classRoom = new ClassRoom();
        return classRoom;
    }

    @Before
    public void initTest() {
        classRoom = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassRoom() throws Exception {
        int databaseSizeBeforeCreate = classRoomRepository.findAll().size();

        // Create the ClassRoom

        restClassRoomMockMvc.perform(post("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoom)))
            .andExpect(status().isCreated());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeCreate + 1);
        ClassRoom testClassRoom = classRoomList.get(classRoomList.size() - 1);
    }

    @Test
    @Transactional
    public void createClassRoomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classRoomRepository.findAll().size();

        // Create the ClassRoom with an existing ID
        ClassRoom existingClassRoom = new ClassRoom();
        existingClassRoom.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassRoomMockMvc.perform(post("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingClassRoom)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClassRooms() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList
        restClassRoomMockMvc.perform(get("/api/class-rooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classRoom.getId().intValue())));
    }

    @Test
    @Transactional
    public void getClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", classRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classRoom.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClassRoom() throws Exception {
        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassRoom() throws Exception {
        // Initialize the database
        classRoomService.save(classRoom);

        int databaseSizeBeforeUpdate = classRoomRepository.findAll().size();

        // Update the classRoom
        ClassRoom updatedClassRoom = classRoomRepository.findOne(classRoom.getId());

        restClassRoomMockMvc.perform(put("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassRoom)))
            .andExpect(status().isOk());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeUpdate);
        ClassRoom testClassRoom = classRoomList.get(classRoomList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingClassRoom() throws Exception {
        int databaseSizeBeforeUpdate = classRoomRepository.findAll().size();

        // Create the ClassRoom

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClassRoomMockMvc.perform(put("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoom)))
            .andExpect(status().isCreated());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClassRoom() throws Exception {
        // Initialize the database
        classRoomService.save(classRoom);

        int databaseSizeBeforeDelete = classRoomRepository.findAll().size();

        // Get the classRoom
        restClassRoomMockMvc.perform(delete("/api/class-rooms/{id}", classRoom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
