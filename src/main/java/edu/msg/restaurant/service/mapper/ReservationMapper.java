package edu.msg.restaurant.service.mapper;

import edu.msg.restaurant.domain.*;
import edu.msg.restaurant.service.dto.ReservationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring", uses = { MeseMapper.class })
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "mese", source = "mese", qualifiedByName = "idSet")
    ReservationDTO toDto(Reservation s);

    @Mapping(target = "removeMese", ignore = true)
    Reservation toEntity(ReservationDTO reservationDTO);
}
