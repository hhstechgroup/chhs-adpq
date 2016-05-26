package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Place;
import com.engagepoint.cws.apqd.repository.PlaceRepository;
import com.engagepoint.cws.apqd.repository.search.PlaceSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PlaceResource REST controller.
 *
 * @see PlaceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlaceResourceIntTest {

    private static final String DEFAULT_UNIT_NUMBER = "AAAAA";
    private static final String UPDATED_UNIT_NUMBER = "BBBBB";
    private static final String DEFAULT_CITY_NAME = "AAAAA";
    private static final String UPDATED_CITY_NAME = "BBBBB";
    private static final String DEFAULT_STREET_NAME = "AAAAA";
    private static final String UPDATED_STREET_NAME = "BBBBB";
    private static final String DEFAULT_STREET_NUMBER = "AAAAA";
    private static final String UPDATED_STREET_NUMBER = "BBBBB";
    private static final String DEFAULT_ZIP_CODE = "AAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBB";
    private static final String DEFAULT_ZIP_SUFFIX = "AAAA";
    private static final String UPDATED_ZIP_SUFFIX = "BBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Boolean DEFAULT_VALID_ADDRESS_FLAG = false;
    private static final Boolean UPDATED_VALID_ADDRESS_FLAG = true;
    private static final String DEFAULT_VALIDATION_STATUS = "AAAAA";
    private static final String UPDATED_VALIDATION_STATUS = "BBBBB";
    private static final String DEFAULT_VALIDATION_MESSAGE = "AAAAA";
    private static final String UPDATED_VALIDATION_MESSAGE = "BBBBB";

    @Inject
    private PlaceRepository placeRepository;

    @Inject
    private PlaceSearchRepository placeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPlaceMockMvc;

    private Place place;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlaceResource placeResource = new PlaceResource();
        ReflectionTestUtils.setField(placeResource, "placeSearchRepository", placeSearchRepository);
        ReflectionTestUtils.setField(placeResource, "placeRepository", placeRepository);
        this.restPlaceMockMvc = MockMvcBuilders.standaloneSetup(placeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        place = new Place();
        place.setUnitNumber(DEFAULT_UNIT_NUMBER);
        place.setCityName(DEFAULT_CITY_NAME);
        place.setStreetName(DEFAULT_STREET_NAME);
        place.setStreetNumber(DEFAULT_STREET_NUMBER);
        place.setZipCode(DEFAULT_ZIP_CODE);
        place.setZipSuffix(DEFAULT_ZIP_SUFFIX);
        place.setLongitude(DEFAULT_LONGITUDE);
        place.setLatitude(DEFAULT_LATITUDE);
        place.setValidAddressFlag(DEFAULT_VALID_ADDRESS_FLAG);
        place.setValidationStatus(DEFAULT_VALIDATION_STATUS);
        place.setValidationMessage(DEFAULT_VALIDATION_MESSAGE);
    }

    @Test
    @Transactional
    public void createPlace() throws Exception {
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place

        restPlaceMockMvc.perform(post("/api/places")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(place)))
                .andExpect(status().isCreated());

        // Validate the Place in the database
        List<Place> places = placeRepository.findAll();
        assertThat(places).hasSize(databaseSizeBeforeCreate + 1);
        Place testPlace = places.get(places.size() - 1);
        assertThat(testPlace.getUnitNumber()).isEqualTo(DEFAULT_UNIT_NUMBER);
        assertThat(testPlace.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
        assertThat(testPlace.getStreetName()).isEqualTo(DEFAULT_STREET_NAME);
        assertThat(testPlace.getStreetNumber()).isEqualTo(DEFAULT_STREET_NUMBER);
        assertThat(testPlace.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testPlace.getZipSuffix()).isEqualTo(DEFAULT_ZIP_SUFFIX);
        assertThat(testPlace.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testPlace.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPlace.getValidAddressFlag()).isEqualTo(DEFAULT_VALID_ADDRESS_FLAG);
        assertThat(testPlace.getValidationStatus()).isEqualTo(DEFAULT_VALIDATION_STATUS);
        assertThat(testPlace.getValidationMessage()).isEqualTo(DEFAULT_VALIDATION_MESSAGE);
    }

    @Test
    @Transactional
    public void checkStreetNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = placeRepository.findAll().size();
        // set the field null
        place.setStreetName(null);

        // Create the Place, which fails.

        restPlaceMockMvc.perform(post("/api/places")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(place)))
                .andExpect(status().isBadRequest());

        List<Place> places = placeRepository.findAll();
        assertThat(places).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlaces() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the places
        restPlaceMockMvc.perform(get("/api/places?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
                .andExpect(jsonPath("$.[*].unitNumber").value(hasItem(DEFAULT_UNIT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME.toString())))
                .andExpect(jsonPath("$.[*].streetName").value(hasItem(DEFAULT_STREET_NAME.toString())))
                .andExpect(jsonPath("$.[*].streetNumber").value(hasItem(DEFAULT_STREET_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
                .andExpect(jsonPath("$.[*].zipSuffix").value(hasItem(DEFAULT_ZIP_SUFFIX.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].validAddressFlag").value(hasItem(DEFAULT_VALID_ADDRESS_FLAG.booleanValue())))
                .andExpect(jsonPath("$.[*].validationStatus").value(hasItem(DEFAULT_VALIDATION_STATUS.toString())))
                .andExpect(jsonPath("$.[*].validationMessage").value(hasItem(DEFAULT_VALIDATION_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getPlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}", place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(place.getId().intValue()))
            .andExpect(jsonPath("$.unitNumber").value(DEFAULT_UNIT_NUMBER.toString()))
            .andExpect(jsonPath("$.cityName").value(DEFAULT_CITY_NAME.toString()))
            .andExpect(jsonPath("$.streetName").value(DEFAULT_STREET_NAME.toString()))
            .andExpect(jsonPath("$.streetNumber").value(DEFAULT_STREET_NUMBER.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.zipSuffix").value(DEFAULT_ZIP_SUFFIX.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.validAddressFlag").value(DEFAULT_VALID_ADDRESS_FLAG.booleanValue()))
            .andExpect(jsonPath("$.validationStatus").value(DEFAULT_VALIDATION_STATUS.toString()))
            .andExpect(jsonPath("$.validationMessage").value(DEFAULT_VALIDATION_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlace() throws Exception {
        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

		int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Update the place
        place.setUnitNumber(UPDATED_UNIT_NUMBER);
        place.setCityName(UPDATED_CITY_NAME);
        place.setStreetName(UPDATED_STREET_NAME);
        place.setStreetNumber(UPDATED_STREET_NUMBER);
        place.setZipCode(UPDATED_ZIP_CODE);
        place.setZipSuffix(UPDATED_ZIP_SUFFIX);
        place.setLongitude(UPDATED_LONGITUDE);
        place.setLatitude(UPDATED_LATITUDE);
        place.setValidAddressFlag(UPDATED_VALID_ADDRESS_FLAG);
        place.setValidationStatus(UPDATED_VALIDATION_STATUS);
        place.setValidationMessage(UPDATED_VALIDATION_MESSAGE);

        restPlaceMockMvc.perform(put("/api/places")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(place)))
                .andExpect(status().isOk());

        // Validate the Place in the database
        List<Place> places = placeRepository.findAll();
        assertThat(places).hasSize(databaseSizeBeforeUpdate);
        Place testPlace = places.get(places.size() - 1);
        assertThat(testPlace.getUnitNumber()).isEqualTo(UPDATED_UNIT_NUMBER);
        assertThat(testPlace.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testPlace.getStreetName()).isEqualTo(UPDATED_STREET_NAME);
        assertThat(testPlace.getStreetNumber()).isEqualTo(UPDATED_STREET_NUMBER);
        assertThat(testPlace.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testPlace.getZipSuffix()).isEqualTo(UPDATED_ZIP_SUFFIX);
        assertThat(testPlace.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testPlace.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPlace.getValidAddressFlag()).isEqualTo(UPDATED_VALID_ADDRESS_FLAG);
        assertThat(testPlace.getValidationStatus()).isEqualTo(UPDATED_VALIDATION_STATUS);
        assertThat(testPlace.getValidationMessage()).isEqualTo(UPDATED_VALIDATION_MESSAGE);
    }

    @Test
    @Transactional
    public void deletePlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

		int databaseSizeBeforeDelete = placeRepository.findAll().size();

        // Get the place
        restPlaceMockMvc.perform(delete("/api/places/{id}", place.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Place> places = placeRepository.findAll();
        assertThat(places).hasSize(databaseSizeBeforeDelete - 1);
    }
}
