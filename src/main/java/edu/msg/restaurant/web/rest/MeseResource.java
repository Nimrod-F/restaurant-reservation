package edu.msg.restaurant.web.rest;

import edu.msg.restaurant.repository.MeseRepository;
import edu.msg.restaurant.service.MeseQueryService;
import edu.msg.restaurant.service.MeseService;
import edu.msg.restaurant.service.criteria.MeseCriteria;
import edu.msg.restaurant.service.dto.MeseDTO;
import edu.msg.restaurant.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.msg.restaurant.domain.Mese}.
 */
@RestController
@RequestMapping("/api")
public class MeseResource {

    private final Logger log = LoggerFactory.getLogger(MeseResource.class);

    private static final String ENTITY_NAME = "mese";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeseService meseService;

    private final MeseRepository meseRepository;

    private final MeseQueryService meseQueryService;

    public MeseResource(MeseService meseService, MeseRepository meseRepository, MeseQueryService meseQueryService) {
        this.meseService = meseService;
        this.meseRepository = meseRepository;
        this.meseQueryService = meseQueryService;
    }

    /**
     * {@code POST  /mese} : Create a new mese.
     *
     * @param meseDTO the meseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meseDTO, or with status {@code 400 (Bad Request)} if the mese has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mese")
    public ResponseEntity<MeseDTO> createMese(@RequestBody MeseDTO meseDTO) throws URISyntaxException {
        log.debug("REST request to save Mese : {}", meseDTO);
        if (meseDTO.getId() != null) {
            throw new BadRequestAlertException("A new mese cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeseDTO result = meseService.save(meseDTO);
        return ResponseEntity
            .created(new URI("/api/mese/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mese/:id} : Updates an existing mese.
     *
     * @param id the id of the meseDTO to save.
     * @param meseDTO the meseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meseDTO,
     * or with status {@code 400 (Bad Request)} if the meseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mese/{id}")
    public ResponseEntity<MeseDTO> updateMese(@PathVariable(value = "id", required = false) final Long id, @RequestBody MeseDTO meseDTO)
        throws URISyntaxException {
        log.debug("REST request to update Mese : {}, {}", id, meseDTO);
        if (meseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeseDTO result = meseService.save(meseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mese/:id} : Partial updates given fields of an existing mese, field will ignore if it is null
     *
     * @param id the id of the meseDTO to save.
     * @param meseDTO the meseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meseDTO,
     * or with status {@code 400 (Bad Request)} if the meseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the meseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the meseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mese/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MeseDTO> partialUpdateMese(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeseDTO meseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mese partially : {}, {}", id, meseDTO);
        if (meseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeseDTO> result = meseService.partialUpdate(meseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mese} : get all the mese.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mese in body.
     */
    @GetMapping("/mese")
    public ResponseEntity<List<MeseDTO>> getAllMese(MeseCriteria criteria) {
        log.debug("REST request to get Mese by criteria: {}", criteria);
        List<MeseDTO> entityList = meseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /mese/count} : count all the mese.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mese/count")
    public ResponseEntity<Long> countMese(MeseCriteria criteria) {
        log.debug("REST request to count Mese by criteria: {}", criteria);
        return ResponseEntity.ok().body(meseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mese/:id} : get the "id" mese.
     *
     * @param id the id of the meseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mese/{id}")
    public ResponseEntity<MeseDTO> getMese(@PathVariable Long id) {
        log.debug("REST request to get Mese : {}", id);
        Optional<MeseDTO> meseDTO = meseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(meseDTO);
    }

    /**
     * {@code DELETE  /mese/:id} : delete the "id" mese.
     *
     * @param id the id of the meseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mese/{id}")
    public ResponseEntity<Void> deleteMese(@PathVariable Long id) {
        log.debug("REST request to delete Mese : {}", id);
        meseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
