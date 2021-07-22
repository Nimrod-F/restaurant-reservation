package edu.msg.restaurant.service;

import edu.msg.restaurant.domain.Restaurants;
import edu.msg.restaurant.repository.RestaurantsRepository;
import edu.msg.restaurant.service.dto.RestaurantsDTO;
import edu.msg.restaurant.service.mapper.RestaurantsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurants}.
 */
@Service
@Transactional
public class RestaurantsService {

    private final Logger log = LoggerFactory.getLogger(RestaurantsService.class);

    private final RestaurantsRepository restaurantsRepository;

    private final RestaurantsMapper restaurantsMapper;

    public RestaurantsService(RestaurantsRepository restaurantsRepository, RestaurantsMapper restaurantsMapper) {
        this.restaurantsRepository = restaurantsRepository;
        this.restaurantsMapper = restaurantsMapper;
    }

    /**
     * Save a restaurants.
     *
     * @param restaurantsDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantsDTO save(RestaurantsDTO restaurantsDTO) {
        log.debug("Request to save Restaurants : {}", restaurantsDTO);
        Restaurants restaurants = restaurantsMapper.toEntity(restaurantsDTO);
        restaurants = restaurantsRepository.save(restaurants);
        return restaurantsMapper.toDto(restaurants);
    }

    /**
     * Partially update a restaurants.
     *
     * @param restaurantsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RestaurantsDTO> partialUpdate(RestaurantsDTO restaurantsDTO) {
        log.debug("Request to partially update Restaurants : {}", restaurantsDTO);

        return restaurantsRepository
            .findById(restaurantsDTO.getId())
            .map(
                existingRestaurants -> {
                    restaurantsMapper.partialUpdate(existingRestaurants, restaurantsDTO);

                    return existingRestaurants;
                }
            )
            .map(restaurantsRepository::save)
            .map(restaurantsMapper::toDto);
    }

    /**
     * Get all the restaurants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RestaurantsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Restaurants");
        return restaurantsRepository.findAll(pageable).map(restaurantsMapper::toDto);
    }

    /**
     * Get one restaurants by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RestaurantsDTO> findOne(Long id) {
        log.debug("Request to get Restaurants : {}", id);
        return restaurantsRepository.findById(id).map(restaurantsMapper::toDto);
    }

    /**
     * Delete the restaurants by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Restaurants : {}", id);
        restaurantsRepository.deleteById(id);
    }
}
