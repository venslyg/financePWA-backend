package com.gvsolutions.service;

import com.gvsolutions.domain.ChurchStaff;
import com.gvsolutions.repository.ChurchStaffRepository;
import com.gvsolutions.repository.search.ChurchStaffSearchRepository;
import com.gvsolutions.service.dto.ChurchStaffDTO;
import com.gvsolutions.service.mapper.ChurchStaffMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.ChurchStaff}.
 */
@Service
@Transactional
public class ChurchStaffService {

    private static final Logger LOG = LoggerFactory.getLogger(ChurchStaffService.class);

    private final ChurchStaffRepository churchStaffRepository;

    private final ChurchStaffMapper churchStaffMapper;

    private final ChurchStaffSearchRepository churchStaffSearchRepository;

    public ChurchStaffService(
        ChurchStaffRepository churchStaffRepository,
        ChurchStaffMapper churchStaffMapper,
        ChurchStaffSearchRepository churchStaffSearchRepository
    ) {
        this.churchStaffRepository = churchStaffRepository;
        this.churchStaffMapper = churchStaffMapper;
        this.churchStaffSearchRepository = churchStaffSearchRepository;
    }

    /**
     * Save a churchStaff.
     *
     * @param churchStaffDTO the entity to save.
     * @return the persisted entity.
     */
    public ChurchStaffDTO save(ChurchStaffDTO churchStaffDTO) {
        LOG.debug("Request to save ChurchStaff : {}", churchStaffDTO);
        ChurchStaff churchStaff = churchStaffMapper.toEntity(churchStaffDTO);
        churchStaff = churchStaffRepository.save(churchStaff);
        churchStaffSearchRepository.index(churchStaff);
        return churchStaffMapper.toDto(churchStaff);
    }

    /**
     * Update a churchStaff.
     *
     * @param churchStaffDTO the entity to save.
     * @return the persisted entity.
     */
    public ChurchStaffDTO update(ChurchStaffDTO churchStaffDTO) {
        LOG.debug("Request to update ChurchStaff : {}", churchStaffDTO);
        ChurchStaff churchStaff = churchStaffMapper.toEntity(churchStaffDTO);
        churchStaff.setIsPersisted();
        churchStaff = churchStaffRepository.save(churchStaff);
        churchStaffSearchRepository.index(churchStaff);
        return churchStaffMapper.toDto(churchStaff);
    }

    /**
     * Partially update a churchStaff.
     *
     * @param churchStaffDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChurchStaffDTO> partialUpdate(ChurchStaffDTO churchStaffDTO) {
        LOG.debug("Request to partially update ChurchStaff : {}", churchStaffDTO);

        return churchStaffRepository
            .findById(churchStaffDTO.getId())
            .map(existingChurchStaff -> {
                churchStaffMapper.partialUpdate(existingChurchStaff, churchStaffDTO);

                return existingChurchStaff;
            })
            .map(churchStaffRepository::save)
            .map(savedChurchStaff -> {
                churchStaffSearchRepository.index(savedChurchStaff);
                return savedChurchStaff;
            })
            .map(churchStaffMapper::toDto);
    }

    /**
     * Get one churchStaff by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChurchStaffDTO> findOne(Long id) {
        LOG.debug("Request to get ChurchStaff : {}", id);
        return churchStaffRepository.findById(id).map(churchStaffMapper::toDto);
    }

    /**
     * Delete the churchStaff by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ChurchStaff : {}", id);
        churchStaffRepository.deleteById(id);
        churchStaffSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the churchStaff corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChurchStaffDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ChurchStaffs for query {}", query);
        return churchStaffSearchRepository.search(query, pageable).map(churchStaffMapper::toDto);
    }
}
