package edu.msg.restaurant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.msg.restaurant.IntegrationTest;
import edu.msg.restaurant.domain.Mese;
import edu.msg.restaurant.domain.Restaurants;
import edu.msg.restaurant.repository.RestaurantsRepository;
import edu.msg.restaurant.service.criteria.RestaurantsCriteria;
import edu.msg.restaurant.service.dto.RestaurantsDTO;
import edu.msg.restaurant.service.mapper.RestaurantsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RestaurantsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurantsResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/restaurants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    @Autowired
    private RestaurantsMapper restaurantsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantsMockMvc;

    private Restaurants restaurants;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurants createEntity(EntityManager em) {
        Restaurants restaurants = new Restaurants()
            .location(DEFAULT_LOCATION)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return restaurants;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurants createUpdatedEntity(EntityManager em) {
        Restaurants restaurants = new Restaurants()
            .location(UPDATED_LOCATION)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return restaurants;
    }

    @BeforeEach
    public void initTest() {
        restaurants = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurants() throws Exception {
        int databaseSizeBeforeCreate = restaurantsRepository.findAll().size();
        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);
        restRestaurantsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testRestaurants.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurants.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestaurants.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testRestaurants.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createRestaurantsWithExistingId() throws Exception {
        // Create the Restaurants with an existing ID
        restaurants.setId(1L);
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        int databaseSizeBeforeCreate = restaurantsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurants.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get the restaurants
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurants.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getRestaurantsByIdFiltering() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        Long id = restaurants.getId();

        defaultRestaurantsShouldBeFound("id.equals=" + id);
        defaultRestaurantsShouldNotBeFound("id.notEquals=" + id);

        defaultRestaurantsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRestaurantsShouldNotBeFound("id.greaterThan=" + id);

        defaultRestaurantsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRestaurantsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where location equals to DEFAULT_LOCATION
        defaultRestaurantsShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the restaurantsList where location equals to UPDATED_LOCATION
        defaultRestaurantsShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where location not equals to DEFAULT_LOCATION
        defaultRestaurantsShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the restaurantsList where location not equals to UPDATED_LOCATION
        defaultRestaurantsShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultRestaurantsShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the restaurantsList where location equals to UPDATED_LOCATION
        defaultRestaurantsShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where location is not null
        defaultRestaurantsShouldBeFound("location.specified=true");

        // Get all the restaurantsList where location is null
        defaultRestaurantsShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByLocationContainsSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where location contains DEFAULT_LOCATION
        defaultRestaurantsShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the restaurantsList where location contains UPDATED_LOCATION
        defaultRestaurantsShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where location does not contain DEFAULT_LOCATION
        defaultRestaurantsShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the restaurantsList where location does not contain UPDATED_LOCATION
        defaultRestaurantsShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where name equals to DEFAULT_NAME
        defaultRestaurantsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the restaurantsList where name equals to UPDATED_NAME
        defaultRestaurantsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where name not equals to DEFAULT_NAME
        defaultRestaurantsShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the restaurantsList where name not equals to UPDATED_NAME
        defaultRestaurantsShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRestaurantsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the restaurantsList where name equals to UPDATED_NAME
        defaultRestaurantsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where name is not null
        defaultRestaurantsShouldBeFound("name.specified=true");

        // Get all the restaurantsList where name is null
        defaultRestaurantsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameContainsSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where name contains DEFAULT_NAME
        defaultRestaurantsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the restaurantsList where name contains UPDATED_NAME
        defaultRestaurantsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where name does not contain DEFAULT_NAME
        defaultRestaurantsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the restaurantsList where name does not contain UPDATED_NAME
        defaultRestaurantsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where description equals to DEFAULT_DESCRIPTION
        defaultRestaurantsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the restaurantsList where description equals to UPDATED_DESCRIPTION
        defaultRestaurantsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where description not equals to DEFAULT_DESCRIPTION
        defaultRestaurantsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the restaurantsList where description not equals to UPDATED_DESCRIPTION
        defaultRestaurantsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRestaurantsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the restaurantsList where description equals to UPDATED_DESCRIPTION
        defaultRestaurantsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where description is not null
        defaultRestaurantsShouldBeFound("description.specified=true");

        // Get all the restaurantsList where description is null
        defaultRestaurantsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where description contains DEFAULT_DESCRIPTION
        defaultRestaurantsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the restaurantsList where description contains UPDATED_DESCRIPTION
        defaultRestaurantsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        // Get all the restaurantsList where description does not contain DEFAULT_DESCRIPTION
        defaultRestaurantsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the restaurantsList where description does not contain UPDATED_DESCRIPTION
        defaultRestaurantsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByMeseIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);
        Mese mese = MeseResourceIT.createEntity(em);
        em.persist(mese);
        em.flush();
        restaurants.setMese(mese);
        restaurantsRepository.saveAndFlush(restaurants);
        Long meseId = mese.getId();

        // Get all the restaurantsList where mese equals to meseId
        defaultRestaurantsShouldBeFound("meseId.equals=" + meseId);

        // Get all the restaurantsList where mese equals to (meseId + 1)
        defaultRestaurantsShouldNotBeFound("meseId.equals=" + (meseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRestaurantsShouldBeFound(String filter) throws Exception {
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurants.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRestaurantsShouldNotBeFound(String filter) throws Exception {
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRestaurantsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRestaurants() throws Exception {
        // Get the restaurants
        restRestaurantsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();

        // Update the restaurants
        Restaurants updatedRestaurants = restaurantsRepository.findById(restaurants.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurants are not directly saved in db
        em.detach(updatedRestaurants);
        updatedRestaurants
            .location(UPDATED_LOCATION)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(updatedRestaurants);

        restRestaurantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testRestaurants.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurants.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestaurants.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRestaurants.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantsWithPatch() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();

        // Update the restaurants using partial update
        Restaurants partialUpdatedRestaurants = new Restaurants();
        partialUpdatedRestaurants.setId(restaurants.getId());

        partialUpdatedRestaurants.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurants))
            )
            .andExpect(status().isOk());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testRestaurants.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurants.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestaurants.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRestaurants.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantsWithPatch() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();

        // Update the restaurants using partial update
        Restaurants partialUpdatedRestaurants = new Restaurants();
        partialUpdatedRestaurants.setId(restaurants.getId());

        partialUpdatedRestaurants
            .location(UPDATED_LOCATION)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurants))
            )
            .andExpect(status().isOk());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
        Restaurants testRestaurants = restaurantsList.get(restaurantsList.size() - 1);
        assertThat(testRestaurants.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testRestaurants.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurants.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestaurants.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRestaurants.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurants() throws Exception {
        int databaseSizeBeforeUpdate = restaurantsRepository.findAll().size();
        restaurants.setId(count.incrementAndGet());

        // Create the Restaurants
        RestaurantsDTO restaurantsDTO = restaurantsMapper.toDto(restaurants);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restaurantsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurants in the database
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurants() throws Exception {
        // Initialize the database
        restaurantsRepository.saveAndFlush(restaurants);

        int databaseSizeBeforeDelete = restaurantsRepository.findAll().size();

        // Delete the restaurants
        restRestaurantsMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurants.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurants> restaurantsList = restaurantsRepository.findAll();
        assertThat(restaurantsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
