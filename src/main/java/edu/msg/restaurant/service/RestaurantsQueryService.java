package edu.msg.restaurant.service;

import edu.msg.restaurant.domain.*; // for static metamodels
import edu.msg.restaurant.domain.Restaurants;
import edu.msg.restaurant.repository.RestaurantsRepository;
import edu.msg.restaurant.service.criteria.RestaurantsCriteria;
import edu.msg.restaurant.service.dto.RestaurantsDTO;
import edu.msg.restaurant.service.mapper.RestaurantsMapper;
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
 * Service for executing complex queries for {@link Restaurants} entities in the database.
 * The main input is a {@link RestaurantsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RestaurantsDTO} or a {@link Page} of {@link RestaurantsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RestaurantsQueryService extends QueryService<Restaurants> {

    private final Logger log = LoggerFactory.getLogger(RestaurantsQueryService.class);

    private final RestaurantsRepository restaurantsRepository;

    private final RestaurantsMapper restaurantsMapper;

    public RestaurantsQueryService(RestaurantsRepository restaurantsRepository, RestaurantsMapper restaurantsMapper) {
        this.restaurantsRepository = restaurantsRepository;
        this.restaurantsMapper = restaurantsMapper;
    }

    /**
     * Return a {@link List} of {@link RestaurantsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantsDTO> findByCriteria(RestaurantsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Restaurants> specification = createSpecification(criteria);
        return restaurantsMapper.toDto(restaurantsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RestaurantsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RestaurantsDTO> findByCriteria(RestaurantsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Restaurants> specification = createSpecification(criteria);
        return restaurantsRepository.findAll(specification, page).map(restaurantsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RestaurantsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Restaurants> specification = createSpecification(criteria);
        return restaurantsRepository.count(specification);
    }

    /**
     * Function to convert {@link RestaurantsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Restaurants> createSpecification(RestaurantsCriteria criteria) {
        Specification<Restaurants> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Restaurants_.id));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Restaurants_.location));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Restaurants_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Restaurants_.description));
            }
            if (criteria.getMeseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMeseId(), root -> root.join(Restaurants_.mese, JoinType.LEFT).get(Mese_.id))
                    );
            }
        }
        return specification;
    }
}
