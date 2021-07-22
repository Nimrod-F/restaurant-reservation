package edu.msg.restaurant.web.rest;

import edu.msg.restaurant.repository.RestaurantsRepository;
import edu.msg.restaurant.service.RestaurantsQueryService;
import edu.msg.restaurant.service.RestaurantsService;
import edu.msg.restaurant.service.criteria.RestaurantsCriteria;
import edu.msg.restaurant.service.dto.RestaurantsDTO;
import edu.msg.restaurant.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.msg.restaurant.domain.Restaurants}.
 */
@RestController
@RequestMapping("/api")
public class RestaurantsResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantsResource.class);

    private static final String ENTITY_NAME = "restaurants";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurantsService restaurantsService;

    private final RestaurantsRepository restaurantsRepository;

    private final RestaurantsQueryService restaurantsQueryService;

    public RestaurantsResource(
        RestaurantsService restaurantsService,
        RestaurantsRepository restaurantsRepository,
        RestaurantsQueryService restaurantsQueryService
    ) {
        this.restaurantsService = restaurantsService;
        this.restaurantsRepository = restaurantsRepository;
        this.restaurantsQueryService = restaurantsQueryService;
    }

    /**
     * {@code POST  /restaurants} : Create a new restaurants.
     *
     * @param restaurantsDTO the restaurantsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantsDTO, or with status {@code 400 (Bad Request)} if the restaurants has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantsDTO> createRestaurants(@RequestBody RestaurantsDTO restaurantsDTO) throws URISyntaxException {
        log.debug("REST request to save Restaurants : {}", restaurantsDTO);
        if (restaurantsDTO.getId() != null) {
            throw new BadRequestAlertException("A new restaurants cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurantsDTO result = restaurantsService.save(restaurantsDTO);
        return ResponseEntity
            .created(new URI("/api/restaurants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurants/:id} : Updates an existing restaurants.
     *
     * @param id the id of the restaurantsDTO to save.
     * @param restaurantsDTO the restaurantsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantsDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantsDTO> updateRestaurants(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantsDTO restaurantsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Restaurants : {}, {}", id, restaurantsDTO);
        if (restaurantsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestaurantsDTO result = restaurantsService.save(restaurantsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurants/:id} : Partial updates given fields of an existing restaurants, field will ignore if it is null
     *
     * @param id the id of the restaurantsDTO to save.
     * @param restaurantsDTO the restaurantsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantsDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restaurantsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurantsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RestaurantsDTO> partialUpdateRestaurants(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantsDTO restaurantsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restaurants partially : {}, {}", id, restaurantsDTO);
        if (restaurantsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurantsDTO> result = restaurantsService.partialUpdate(restaurantsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurants} : get all the restaurants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurants in body.
     */
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantsDTO>> getAllRestaurants(RestaurantsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Restaurants by criteria: {}", criteria);
        Page<RestaurantsDTO> page = restaurantsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restaurants/count} : count all the restaurants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/restaurants/count")
    public ResponseEntity<Long> countRestaurants(RestaurantsCriteria criteria) {
        log.debug("REST request to count Restaurants by criteria: {}", criteria);
        return ResponseEntity.ok().body(restaurantsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /restaurants/:id} : get the "id" restaurants.
     *
     * @param id the id of the restaurantsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantsDTO> getRestaurants(@PathVariable Long id) {
        log.debug("REST request to get Restaurants : {}", id);
        Optional<RestaurantsDTO> restaurantsDTO = restaurantsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurantsDTO);
    }

    /**
     * {@code DELETE  /restaurants/:id} : delete the "id" restaurants.
     *
     * @param id the id of the restaurantsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurants(@PathVariable Long id) {
        log.debug("REST request to delete Restaurants : {}", id);
        restaurantsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
