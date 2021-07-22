package edu.msg.restaurant.service;

import edu.msg.restaurant.domain.Mese;
import edu.msg.restaurant.repository.MeseRepository;
import edu.msg.restaurant.service.dto.MeseDTO;
import edu.msg.restaurant.service.mapper.MeseMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mese}.
 */
@Service
@Transactional
public class MeseService {

    private final Logger log = LoggerFactory.getLogger(MeseService.class);

    private final MeseRepository meseRepository;

    private final MeseMapper meseMapper;

    public MeseService(MeseRepository meseRepository, MeseMapper meseMapper) {
        this.meseRepository = meseRepository;
        this.meseMapper = meseMapper;
    }

    /**
     * Save a mese.
     *
     * @param meseDTO the entity to save.
     * @return the persisted entity.
     */
    public MeseDTO save(MeseDTO meseDTO) {
        log.debug("Request to save Mese : {}", meseDTO);
        Mese mese = meseMapper.toEntity(meseDTO);
        mese = meseRepository.save(mese);
        return meseMapper.toDto(mese);
    }

    /**
     * Partially update a mese.
     *
     * @param meseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MeseDTO> partialUpdate(MeseDTO meseDTO) {
        log.debug("Request to partially update Mese : {}", meseDTO);

        return meseRepository
            .findById(meseDTO.getId())
            .map(
                existingMese -> {
                    meseMapper.partialUpdate(existingMese, meseDTO);

                    return existingMese;
                }
            )
            .map(meseRepository::save)
            .map(meseMapper::toDto);
    }

    /**
     * Get all the mese.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MeseDTO> findAll() {
        log.debug("Request to get all Mese");
        return meseRepository.findAll().stream().map(meseMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one mese by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MeseDTO> findOne(Long id) {
        log.debug("Request to get Mese : {}", id);
        return meseRepository.findById(id).map(meseMapper::toDto);
    }

    /**
     * Delete the mese by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mese : {}", id);
        meseRepository.deleteById(id);
    }
}
