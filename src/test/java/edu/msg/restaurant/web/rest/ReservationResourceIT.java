package edu.msg.restaurant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.msg.restaurant.IntegrationTest;
import edu.msg.restaurant.domain.Mese;
import edu.msg.restaurant.domain.Reservation;
import edu.msg.restaurant.repository.ReservationRepository;
import edu.msg.restaurant.service.ReservationService;
import edu.msg.restaurant.service.criteria.ReservationCriteria;
import edu.msg.restaurant.service.dto.ReservationDTO;
import edu.msg.restaurant.service.mapper.ReservationMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReservationResourceIT {

    private static final Boolean DEFAULT_SCRUMIERA = false;
    private static final Boolean UPDATED_SCRUMIERA = true;

    private static final Integer DEFAULT_NR_PERS = 1;
    private static final Integer UPDATED_NR_PERS = 2;
    private static final Integer SMALLER_NR_PERS = 1 - 1;

    private static final Instant DEFAULT_START_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATEIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATEIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reservations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationRepository reservationRepositoryMock;

    @Autowired
    private ReservationMapper reservationMapper;

    @Mock
    private ReservationService reservationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .scrumiera(DEFAULT_SCRUMIERA)
            .nrPers(DEFAULT_NR_PERS)
            .startDatetime(DEFAULT_START_DATETIME)
            .endDateime(DEFAULT_END_DATEIME);
        return reservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createUpdatedEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .scrumiera(UPDATED_SCRUMIERA)
            .nrPers(UPDATED_NR_PERS)
            .startDatetime(UPDATED_START_DATETIME)
            .endDateime(UPDATED_END_DATEIME);
        return reservation;
    }

    @BeforeEach
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();
        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getScrumiera()).isEqualTo(DEFAULT_SCRUMIERA);
        assertThat(testReservation.getNrPers()).isEqualTo(DEFAULT_NR_PERS);
        assertThat(testReservation.getStartDatetime()).isEqualTo(DEFAULT_START_DATETIME);
        assertThat(testReservation.getEndDateime()).isEqualTo(DEFAULT_END_DATEIME);
    }

    @Test
    @Transactional
    void createReservationWithExistingId() throws Exception {
        // Create the Reservation with an existing ID
        reservation.setId(1L);
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].scrumiera").value(hasItem(DEFAULT_SCRUMIERA.booleanValue())))
            .andExpect(jsonPath("$.[*].nrPers").value(hasItem(DEFAULT_NR_PERS)))
            .andExpect(jsonPath("$.[*].startDatetime").value(hasItem(DEFAULT_START_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].endDateime").value(hasItem(DEFAULT_END_DATEIME.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(reservationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reservationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reservationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reservationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc
            .perform(get(ENTITY_API_URL_ID, reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.scrumiera").value(DEFAULT_SCRUMIERA.booleanValue()))
            .andExpect(jsonPath("$.nrPers").value(DEFAULT_NR_PERS))
            .andExpect(jsonPath("$.startDatetime").value(DEFAULT_START_DATETIME.toString()))
            .andExpect(jsonPath("$.endDateime").value(DEFAULT_END_DATEIME.toString()));
    }

    @Test
    @Transactional
    void getReservationsByIdFiltering() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        Long id = reservation.getId();

        defaultReservationShouldBeFound("id.equals=" + id);
        defaultReservationShouldNotBeFound("id.notEquals=" + id);

        defaultReservationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReservationShouldNotBeFound("id.greaterThan=" + id);

        defaultReservationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReservationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReservationsByScrumieraIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where scrumiera equals to DEFAULT_SCRUMIERA
        defaultReservationShouldBeFound("scrumiera.equals=" + DEFAULT_SCRUMIERA);

        // Get all the reservationList where scrumiera equals to UPDATED_SCRUMIERA
        defaultReservationShouldNotBeFound("scrumiera.equals=" + UPDATED_SCRUMIERA);
    }

    @Test
    @Transactional
    void getAllReservationsByScrumieraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where scrumiera not equals to DEFAULT_SCRUMIERA
        defaultReservationShouldNotBeFound("scrumiera.notEquals=" + DEFAULT_SCRUMIERA);

        // Get all the reservationList where scrumiera not equals to UPDATED_SCRUMIERA
        defaultReservationShouldBeFound("scrumiera.notEquals=" + UPDATED_SCRUMIERA);
    }

    @Test
    @Transactional
    void getAllReservationsByScrumieraIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where scrumiera in DEFAULT_SCRUMIERA or UPDATED_SCRUMIERA
        defaultReservationShouldBeFound("scrumiera.in=" + DEFAULT_SCRUMIERA + "," + UPDATED_SCRUMIERA);

        // Get all the reservationList where scrumiera equals to UPDATED_SCRUMIERA
        defaultReservationShouldNotBeFound("scrumiera.in=" + UPDATED_SCRUMIERA);
    }

    @Test
    @Transactional
    void getAllReservationsByScrumieraIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where scrumiera is not null
        defaultReservationShouldBeFound("scrumiera.specified=true");

        // Get all the reservationList where scrumiera is null
        defaultReservationShouldNotBeFound("scrumiera.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers equals to DEFAULT_NR_PERS
        defaultReservationShouldBeFound("nrPers.equals=" + DEFAULT_NR_PERS);

        // Get all the reservationList where nrPers equals to UPDATED_NR_PERS
        defaultReservationShouldNotBeFound("nrPers.equals=" + UPDATED_NR_PERS);
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers not equals to DEFAULT_NR_PERS
        defaultReservationShouldNotBeFound("nrPers.notEquals=" + DEFAULT_NR_PERS);

        // Get all the reservationList where nrPers not equals to UPDATED_NR_PERS
        defaultReservationShouldBeFound("nrPers.notEquals=" + UPDATED_NR_PERS);
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers in DEFAULT_NR_PERS or UPDATED_NR_PERS
        defaultReservationShouldBeFound("nrPers.in=" + DEFAULT_NR_PERS + "," + UPDATED_NR_PERS);

        // Get all the reservationList where nrPers equals to UPDATED_NR_PERS
        defaultReservationShouldNotBeFound("nrPers.in=" + UPDATED_NR_PERS);
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers is not null
        defaultReservationShouldBeFound("nrPers.specified=true");

        // Get all the reservationList where nrPers is null
        defaultReservationShouldNotBeFound("nrPers.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers is greater than or equal to DEFAULT_NR_PERS
        defaultReservationShouldBeFound("nrPers.greaterThanOrEqual=" + DEFAULT_NR_PERS);

        // Get all the reservationList where nrPers is greater than or equal to UPDATED_NR_PERS
        defaultReservationShouldNotBeFound("nrPers.greaterThanOrEqual=" + UPDATED_NR_PERS);
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers is less than or equal to DEFAULT_NR_PERS
        defaultReservationShouldBeFound("nrPers.lessThanOrEqual=" + DEFAULT_NR_PERS);

        // Get all the reservationList where nrPers is less than or equal to SMALLER_NR_PERS
        defaultReservationShouldNotBeFound("nrPers.lessThanOrEqual=" + SMALLER_NR_PERS);
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers is less than DEFAULT_NR_PERS
        defaultReservationShouldNotBeFound("nrPers.lessThan=" + DEFAULT_NR_PERS);

        // Get all the reservationList where nrPers is less than UPDATED_NR_PERS
        defaultReservationShouldBeFound("nrPers.lessThan=" + UPDATED_NR_PERS);
    }

    @Test
    @Transactional
    void getAllReservationsByNrPersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where nrPers is greater than DEFAULT_NR_PERS
        defaultReservationShouldNotBeFound("nrPers.greaterThan=" + DEFAULT_NR_PERS);

        // Get all the reservationList where nrPers is greater than SMALLER_NR_PERS
        defaultReservationShouldBeFound("nrPers.greaterThan=" + SMALLER_NR_PERS);
    }

    @Test
    @Transactional
    void getAllReservationsByStartDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where startDatetime equals to DEFAULT_START_DATETIME
        defaultReservationShouldBeFound("startDatetime.equals=" + DEFAULT_START_DATETIME);

        // Get all the reservationList where startDatetime equals to UPDATED_START_DATETIME
        defaultReservationShouldNotBeFound("startDatetime.equals=" + UPDATED_START_DATETIME);
    }

    @Test
    @Transactional
    void getAllReservationsByStartDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where startDatetime not equals to DEFAULT_START_DATETIME
        defaultReservationShouldNotBeFound("startDatetime.notEquals=" + DEFAULT_START_DATETIME);

        // Get all the reservationList where startDatetime not equals to UPDATED_START_DATETIME
        defaultReservationShouldBeFound("startDatetime.notEquals=" + UPDATED_START_DATETIME);
    }

    @Test
    @Transactional
    void getAllReservationsByStartDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where startDatetime in DEFAULT_START_DATETIME or UPDATED_START_DATETIME
        defaultReservationShouldBeFound("startDatetime.in=" + DEFAULT_START_DATETIME + "," + UPDATED_START_DATETIME);

        // Get all the reservationList where startDatetime equals to UPDATED_START_DATETIME
        defaultReservationShouldNotBeFound("startDatetime.in=" + UPDATED_START_DATETIME);
    }

    @Test
    @Transactional
    void getAllReservationsByStartDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where startDatetime is not null
        defaultReservationShouldBeFound("startDatetime.specified=true");

        // Get all the reservationList where startDatetime is null
        defaultReservationShouldNotBeFound("startDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByEndDateimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where endDateime equals to DEFAULT_END_DATEIME
        defaultReservationShouldBeFound("endDateime.equals=" + DEFAULT_END_DATEIME);

        // Get all the reservationList where endDateime equals to UPDATED_END_DATEIME
        defaultReservationShouldNotBeFound("endDateime.equals=" + UPDATED_END_DATEIME);
    }

    @Test
    @Transactional
    void getAllReservationsByEndDateimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where endDateime not equals to DEFAULT_END_DATEIME
        defaultReservationShouldNotBeFound("endDateime.notEquals=" + DEFAULT_END_DATEIME);

        // Get all the reservationList where endDateime not equals to UPDATED_END_DATEIME
        defaultReservationShouldBeFound("endDateime.notEquals=" + UPDATED_END_DATEIME);
    }

    @Test
    @Transactional
    void getAllReservationsByEndDateimeIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where endDateime in DEFAULT_END_DATEIME or UPDATED_END_DATEIME
        defaultReservationShouldBeFound("endDateime.in=" + DEFAULT_END_DATEIME + "," + UPDATED_END_DATEIME);

        // Get all the reservationList where endDateime equals to UPDATED_END_DATEIME
        defaultReservationShouldNotBeFound("endDateime.in=" + UPDATED_END_DATEIME);
    }

    @Test
    @Transactional
    void getAllReservationsByEndDateimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where endDateime is not null
        defaultReservationShouldBeFound("endDateime.specified=true");

        // Get all the reservationList where endDateime is null
        defaultReservationShouldNotBeFound("endDateime.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByMeseIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        Mese mese = MeseResourceIT.createEntity(em);
        em.persist(mese);
        em.flush();
        reservation.addMese(mese);
        reservationRepository.saveAndFlush(reservation);
        Long meseId = mese.getId();

        // Get all the reservationList where mese equals to meseId
        defaultReservationShouldBeFound("meseId.equals=" + meseId);

        // Get all the reservationList where mese equals to (meseId + 1)
        defaultReservationShouldNotBeFound("meseId.equals=" + (meseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReservationShouldBeFound(String filter) throws Exception {
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].scrumiera").value(hasItem(DEFAULT_SCRUMIERA.booleanValue())))
            .andExpect(jsonPath("$.[*].nrPers").value(hasItem(DEFAULT_NR_PERS)))
            .andExpect(jsonPath("$.[*].startDatetime").value(hasItem(DEFAULT_START_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].endDateime").value(hasItem(DEFAULT_END_DATEIME.toString())));

        // Check, that the count call also returns 1
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReservationShouldNotBeFound(String filter) throws Exception {
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .scrumiera(UPDATED_SCRUMIERA)
            .nrPers(UPDATED_NR_PERS)
            .startDatetime(UPDATED_START_DATETIME)
            .endDateime(UPDATED_END_DATEIME);
        ReservationDTO reservationDTO = reservationMapper.toDto(updatedReservation);

        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getScrumiera()).isEqualTo(UPDATED_SCRUMIERA);
        assertThat(testReservation.getNrPers()).isEqualTo(UPDATED_NR_PERS);
        assertThat(testReservation.getStartDatetime()).isEqualTo(UPDATED_START_DATETIME);
        assertThat(testReservation.getEndDateime()).isEqualTo(UPDATED_END_DATEIME);
    }

    @Test
    @Transactional
    void putNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation.startDatetime(UPDATED_START_DATETIME);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getScrumiera()).isEqualTo(DEFAULT_SCRUMIERA);
        assertThat(testReservation.getNrPers()).isEqualTo(DEFAULT_NR_PERS);
        assertThat(testReservation.getStartDatetime()).isEqualTo(UPDATED_START_DATETIME);
        assertThat(testReservation.getEndDateime()).isEqualTo(DEFAULT_END_DATEIME);
    }

    @Test
    @Transactional
    void fullUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .scrumiera(UPDATED_SCRUMIERA)
            .nrPers(UPDATED_NR_PERS)
            .startDatetime(UPDATED_START_DATETIME)
            .endDateime(UPDATED_END_DATEIME);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getScrumiera()).isEqualTo(UPDATED_SCRUMIERA);
        assertThat(testReservation.getNrPers()).isEqualTo(UPDATED_NR_PERS);
        assertThat(testReservation.getStartDatetime()).isEqualTo(UPDATED_START_DATETIME);
        assertThat(testReservation.getEndDateime()).isEqualTo(UPDATED_END_DATEIME);
    }

    @Test
    @Transactional
    void patchNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();
        reservation.setId(count.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reservationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
