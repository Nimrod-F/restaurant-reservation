package edu.msg.restaurant.service.mapper;

import edu.msg.restaurant.domain.*;
import edu.msg.restaurant.service.dto.RestaurantsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurants} and its DTO {@link RestaurantsDTO}.
 */
@Mapper(componentModel = "spring", uses = { MeseMapper.class })
public interface RestaurantsMapper extends EntityMapper<RestaurantsDTO, Restaurants> {
    @Mapping(target = "mese", source = "mese", qualifiedByName = "id")
    RestaurantsDTO toDto(Restaurants s);
}
