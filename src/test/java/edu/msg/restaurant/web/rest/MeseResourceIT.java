package edu.msg.restaurant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.msg.restaurant.IntegrationTest;
import edu.msg.restaurant.domain.Mese;
import edu.msg.restaurant.domain.Reservation;
import edu.msg.restaurant.domain.Restaurants;
import edu.msg.restaurant.repository.MeseRepository;
import edu.msg.restaurant.service.criteria.MeseCriteria;
import edu.msg.restaurant.service.dto.MeseDTO;
import edu.msg.restaurant.service.mapper.MeseMapper;
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

/**
 * Integration tests for the {@link MeseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeseResourceIT {

    private static final Integer DEFAULT_NR_LOC = 1;
    private static final Integer UPDATED_NR_LOC = 2;
    private static final Integer SMALLER_NR_LOC = 1 - 1;

    private static final Boolean DEFAULT_OUTDOOR = false;
    private static final Boolean UPDATED_OUTDOOR = true;

    private static final String ENTITY_API_URL = "/api/mese";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeseRepository meseRepository;

    @Autowired
    private MeseMapper meseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeseMockMvc;

    private Mese mese;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mese createEntity(EntityManager em) {
        Mese mese = new Mese().nrLoc(DEFAULT_NR_LOC).outdoor(DEFAULT_OUTDOOR);
        return mese;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mese createUpdatedEntity(EntityManager em) {
        Mese mese = new Mese().nrLoc(UPDATED_NR_LOC).outdoor(UPDATED_OUTDOOR);
        return mese;
    }

    @BeforeEach
    public void initTest() {
        mese = createEntity(em);
    }

    @Test
    @Transactional
    void createMese() throws Exception {
        int databaseSizeBeforeCreate = meseRepository.findAll().size();
        // Create the Mese
        MeseDTO meseDTO = meseMapper.toDto(mese);
        restMeseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meseDTO)))
            .andExpect(status().isCreated());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeCreate + 1);
        Mese testMese = meseList.get(meseList.size() - 1);
        assertThat(testMese.getNrLoc()).isEqualTo(DEFAULT_NR_LOC);
        assertThat(testMese.getOutdoor()).isEqualTo(DEFAULT_OUTDOOR);
    }

    @Test
    @Transactional
    void createMeseWithExistingId() throws Exception {
        // Create the Mese with an existing ID
        mese.setId(1L);
        MeseDTO meseDTO = meseMapper.toDto(mese);

        int databaseSizeBeforeCreate = meseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMese() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList
        restMeseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mese.getId().intValue())))
            .andExpect(jsonPath("$.[*].nrLoc").value(hasItem(DEFAULT_NR_LOC)))
            .andExpect(jsonPath("$.[*].outdoor").value(hasItem(DEFAULT_OUTDOOR.booleanValue())));
    }

    @Test
    @Transactional
    void getMese() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get the mese
        restMeseMockMvc
            .perform(get(ENTITY_API_URL_ID, mese.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mese.getId().intValue()))
            .andExpect(jsonPath("$.nrLoc").value(DEFAULT_NR_LOC))
            .andExpect(jsonPath("$.outdoor").value(DEFAULT_OUTDOOR.booleanValue()));
    }

    @Test
    @Transactional
    void getMeseByIdFiltering() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        Long id = mese.getId();

        defaultMeseShouldBeFound("id.equals=" + id);
        defaultMeseShouldNotBeFound("id.notEquals=" + id);

        defaultMeseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMeseShouldNotBeFound("id.greaterThan=" + id);

        defaultMeseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMeseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc equals to DEFAULT_NR_LOC
        defaultMeseShouldBeFound("nrLoc.equals=" + DEFAULT_NR_LOC);

        // Get all the meseList where nrLoc equals to UPDATED_NR_LOC
        defaultMeseShouldNotBeFound("nrLoc.equals=" + UPDATED_NR_LOC);
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsNotEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc not equals to DEFAULT_NR_LOC
        defaultMeseShouldNotBeFound("nrLoc.notEquals=" + DEFAULT_NR_LOC);

        // Get all the meseList where nrLoc not equals to UPDATED_NR_LOC
        defaultMeseShouldBeFound("nrLoc.notEquals=" + UPDATED_NR_LOC);
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsInShouldWork() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc in DEFAULT_NR_LOC or UPDATED_NR_LOC
        defaultMeseShouldBeFound("nrLoc.in=" + DEFAULT_NR_LOC + "," + UPDATED_NR_LOC);

        // Get all the meseList where nrLoc equals to UPDATED_NR_LOC
        defaultMeseShouldNotBeFound("nrLoc.in=" + UPDATED_NR_LOC);
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsNullOrNotNull() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc is not null
        defaultMeseShouldBeFound("nrLoc.specified=true");

        // Get all the meseList where nrLoc is null
        defaultMeseShouldNotBeFound("nrLoc.specified=false");
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc is greater than or equal to DEFAULT_NR_LOC
        defaultMeseShouldBeFound("nrLoc.greaterThanOrEqual=" + DEFAULT_NR_LOC);

        // Get all the meseList where nrLoc is greater than or equal to UPDATED_NR_LOC
        defaultMeseShouldNotBeFound("nrLoc.greaterThanOrEqual=" + UPDATED_NR_LOC);
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc is less than or equal to DEFAULT_NR_LOC
        defaultMeseShouldBeFound("nrLoc.lessThanOrEqual=" + DEFAULT_NR_LOC);

        // Get all the meseList where nrLoc is less than or equal to SMALLER_NR_LOC
        defaultMeseShouldNotBeFound("nrLoc.lessThanOrEqual=" + SMALLER_NR_LOC);
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsLessThanSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc is less than DEFAULT_NR_LOC
        defaultMeseShouldNotBeFound("nrLoc.lessThan=" + DEFAULT_NR_LOC);

        // Get all the meseList where nrLoc is less than UPDATED_NR_LOC
        defaultMeseShouldBeFound("nrLoc.lessThan=" + UPDATED_NR_LOC);
    }

    @Test
    @Transactional
    void getAllMeseByNrLocIsGreaterThanSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where nrLoc is greater than DEFAULT_NR_LOC
        defaultMeseShouldNotBeFound("nrLoc.greaterThan=" + DEFAULT_NR_LOC);

        // Get all the meseList where nrLoc is greater than SMALLER_NR_LOC
        defaultMeseShouldBeFound("nrLoc.greaterThan=" + SMALLER_NR_LOC);
    }

    @Test
    @Transactional
    void getAllMeseByOutdoorIsEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where outdoor equals to DEFAULT_OUTDOOR
        defaultMeseShouldBeFound("outdoor.equals=" + DEFAULT_OUTDOOR);

        // Get all the meseList where outdoor equals to UPDATED_OUTDOOR
        defaultMeseShouldNotBeFound("outdoor.equals=" + UPDATED_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllMeseByOutdoorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where outdoor not equals to DEFAULT_OUTDOOR
        defaultMeseShouldNotBeFound("outdoor.notEquals=" + DEFAULT_OUTDOOR);

        // Get all the meseList where outdoor not equals to UPDATED_OUTDOOR
        defaultMeseShouldBeFound("outdoor.notEquals=" + UPDATED_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllMeseByOutdoorIsInShouldWork() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where outdoor in DEFAULT_OUTDOOR or UPDATED_OUTDOOR
        defaultMeseShouldBeFound("outdoor.in=" + DEFAULT_OUTDOOR + "," + UPDATED_OUTDOOR);

        // Get all the meseList where outdoor equals to UPDATED_OUTDOOR
        defaultMeseShouldNotBeFound("outdoor.in=" + UPDATED_OUTDOOR);
    }

    @Test
    @Transactional
    void getAllMeseByOutdoorIsNullOrNotNull() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        // Get all the meseList where outdoor is not null
        defaultMeseShouldBeFound("outdoor.specified=true");

        // Get all the meseList where outdoor is null
        defaultMeseShouldNotBeFound("outdoor.specified=false");
    }

    @Test
    @Transactional
    void getAllMeseByRestaurantsIsEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);
        Restaurants restaurants = RestaurantsResourceIT.createEntity(em);
        em.persist(restaurants);
        em.flush();
        mese.addRestaurants(restaurants);
        meseRepository.saveAndFlush(mese);
        Long restaurantsId = restaurants.getId();

        // Get all the meseList where restaurants equals to restaurantsId
        defaultMeseShouldBeFound("restaurantsId.equals=" + restaurantsId);

        // Get all the meseList where restaurants equals to (restaurantsId + 1)
        defaultMeseShouldNotBeFound("restaurantsId.equals=" + (restaurantsId + 1));
    }

    @Test
    @Transactional
    void getAllMeseByReservationIsEqualToSomething() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);
        Reservation reservation = ReservationResourceIT.createEntity(em);
        em.persist(reservation);
        em.flush();
        mese.addReservation(reservation);
        meseRepository.saveAndFlush(mese);
        Long reservationId = reservation.getId();

        // Get all the meseList where reservation equals to reservationId
        defaultMeseShouldBeFound("reservationId.equals=" + reservationId);

        // Get all the meseList where reservation equals to (reservationId + 1)
        defaultMeseShouldNotBeFound("reservationId.equals=" + (reservationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMeseShouldBeFound(String filter) throws Exception {
        restMeseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mese.getId().intValue())))
            .andExpect(jsonPath("$.[*].nrLoc").value(hasItem(DEFAULT_NR_LOC)))
            .andExpect(jsonPath("$.[*].outdoor").value(hasItem(DEFAULT_OUTDOOR.booleanValue())));

        // Check, that the count call also returns 1
        restMeseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMeseShouldNotBeFound(String filter) throws Exception {
        restMeseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMese() throws Exception {
        // Get the mese
        restMeseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMese() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        int databaseSizeBeforeUpdate = meseRepository.findAll().size();

        // Update the mese
        Mese updatedMese = meseRepository.findById(mese.getId()).get();
        // Disconnect from session so that the updates on updatedMese are not directly saved in db
        em.detach(updatedMese);
        updatedMese.nrLoc(UPDATED_NR_LOC).outdoor(UPDATED_OUTDOOR);
        MeseDTO meseDTO = meseMapper.toDto(updatedMese);

        restMeseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
        Mese testMese = meseList.get(meseList.size() - 1);
        assertThat(testMese.getNrLoc()).isEqualTo(UPDATED_NR_LOC);
        assertThat(testMese.getOutdoor()).isEqualTo(UPDATED_OUTDOOR);
    }

    @Test
    @Transactional
    void putNonExistingMese() throws Exception {
        int databaseSizeBeforeUpdate = meseRepository.findAll().size();
        mese.setId(count.incrementAndGet());

        // Create the Mese
        MeseDTO meseDTO = meseMapper.toDto(mese);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMese() throws Exception {
        int databaseSizeBeforeUpdate = meseRepository.findAll().size();
        mese.setId(count.incrementAndGet());

        // Create the Mese
        MeseDTO meseDTO = meseMapper.toDto(mese);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMese() throws Exception {
        int databaseSizeBeforeUpdate = meseRepository.findAll().size();
        mese.setId(count.incrementAndGet());

        // Create the Mese
        MeseDTO meseDTO = meseMapper.toDto(mese);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeseWithPatch() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        int databaseSizeBeforeUpdate = meseRepository.findAll().size();

        // Update the mese using partial update
        Mese partialUpdatedMese = new Mese();
        partialUpdatedMese.setId(mese.getId());

        partialUpdatedMese.outdoor(UPDATED_OUTDOOR);

        restMeseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMese.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMese))
            )
            .andExpect(status().isOk());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
        Mese testMese = meseList.get(meseList.size() - 1);
        assertThat(testMese.getNrLoc()).isEqualTo(DEFAULT_NR_LOC);
        assertThat(testMese.getOutdoor()).isEqualTo(UPDATED_OUTDOOR);
    }

    @Test
    @Transactional
    void fullUpdateMeseWithPatch() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        int databaseSizeBeforeUpdate = meseRepository.findAll().size();

        // Update the mese using partial update
        Mese partialUpdatedMese = new Mese();
        partialUpdatedMese.setId(mese.getId());

        partialUpdatedMese.nrLoc(UPDATED_NR_LOC).outdoor(UPDATED_OUTDOOR);

        restMeseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMese.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMese))
            )
            .andExpect(status().isOk());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
        Mese testMese = meseList.get(meseList.size() - 1);
        assertThat(testMese.getNrLoc()).isEqualTo(UPDATED_NR_LOC);
        assertThat(testMese.getOutdoor()).isEqualTo(UPDATED_OUTDOOR);
    }

    @Test
    @Transactional
    void patchNonExistingMese() throws Exception {
        int databaseSizeBeforeUpdate = meseRepository.findAll().size();
        mese.setId(count.incrementAndGet());

        // Create the Mese
        MeseDTO meseDTO = meseMapper.toDto(mese);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMese() throws Exception {
        int databaseSizeBeforeUpdate = meseRepository.findAll().size();
        mese.setId(count.incrementAndGet());

        // Create the Mese
        MeseDTO meseDTO = meseMapper.toDto(mese);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMese() throws Exception {
        int databaseSizeBeforeUpdate = meseRepository.findAll().size();
        mese.setId(count.incrementAndGet());

        // Create the Mese
        MeseDTO meseDTO = meseMapper.toDto(mese);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mese in the database
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMese() throws Exception {
        // Initialize the database
        meseRepository.saveAndFlush(mese);

        int databaseSizeBeforeDelete = meseRepository.findAll().size();

        // Delete the mese
        restMeseMockMvc
            .perform(delete(ENTITY_API_URL_ID, mese.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mese> meseList = meseRepository.findAll();
        assertThat(meseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
