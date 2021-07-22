package edu.msg.restaurant.service.mapper;

import edu.msg.restaurant.domain.*;
import edu.msg.restaurant.service.dto.MeseDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mese} and its DTO {@link MeseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MeseMapper extends EntityMapper<MeseDTO, Mese> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MeseDTO toDtoId(Mese mese);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<MeseDTO> toDtoIdSet(Set<Mese> mese);
}
