package edu.msg.restaurant.repository;

import edu.msg.restaurant.domain.Restaurants;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaurants entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantsRepository extends JpaRepository<Restaurants, Long>, JpaSpecificationExecutor<Restaurants> {}
