package edu.msg.restaurant.service;

import edu.msg.restaurant.domain.*; // for static metamodels
import edu.msg.restaurant.domain.Mese;
import edu.msg.restaurant.repository.MeseRepository;
import edu.msg.restaurant.service.criteria.MeseCriteria;
import edu.msg.restaurant.service.dto.MeseDTO;
import edu.msg.restaurant.service.mapper.MeseMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Mese} entities in the database.
 * The main input is a {@link MeseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MeseDTO} or a {@link Page} of {@link MeseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeseQueryService extends QueryService<Mese> {

    private final Logger log = LoggerFactory.getLogger(MeseQueryService.class);

    private final MeseRepository meseRepository;

    private final MeseMapper meseMapper;

    public MeseQueryService(MeseRepository meseRepository, MeseMapper meseMapper) {
        this.meseRepository = meseRepository;
        this.meseMapper = meseMapper;
    }

    /**
     * Return a {@link List} of {@link MeseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MeseDTO> findByCriteria(MeseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mese> specification = createSpecification(criteria);
        return meseMapper.toDto(meseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MeseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MeseDTO> findByCriteria(MeseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mese> specification = createSpecification(criteria);
        return meseRepository.findAll(specification, page).map(meseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MeseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mese> specification = createSpecification(criteria);
        return meseRepository.count(specification);
    }

    /**
     * Function to convert {@link MeseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mese> createSpecification(MeseCriteria criteria) {
        Specification<Mese> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mese_.id));
            }
            if (criteria.getNrLoc() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNrLoc(), Mese_.nrLoc));
            }
            if (criteria.getOutdoor() != null) {
                specification = specification.and(buildSpecification(criteria.getOutdoor(), Mese_.outdoor));
            }
            if (criteria.getRestaurantsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRestaurantsId(),
                            root -> root.join(Mese_.restaurants, JoinType.LEFT).get(Restaurants_.id)
                        )
                    );
            }
            if (criteria.getReservationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReservationId(),
                            root -> root.join(Mese_.reservations, JoinType.LEFT).get(Reservation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
