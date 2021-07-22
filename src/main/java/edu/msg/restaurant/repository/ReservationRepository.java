package edu.msg.restaurant.repository;

import edu.msg.restaurant.domain.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reservation entity.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    @Query(
        value = "select distinct reservation from Reservation reservation left join fetch reservation.mese",
        countQuery = "select count(distinct reservation) from Reservation reservation"
    )
    Page<Reservation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct reservation from Reservation reservation left join fetch reservation.mese")
    List<Reservation> findAllWithEagerRelationships();

    @Query("select reservation from Reservation reservation left join fetch reservation.mese where reservation.id =:id")
    Optional<Reservation> findOneWithEagerRelationships(@Param("id") Long id);
}
