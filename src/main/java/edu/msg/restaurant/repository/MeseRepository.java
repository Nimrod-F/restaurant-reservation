package edu.msg.restaurant.repository;

import edu.msg.restaurant.domain.Mese;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mese entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeseRepository extends JpaRepository<Mese, Long>, JpaSpecificationExecutor<Mese> {}
