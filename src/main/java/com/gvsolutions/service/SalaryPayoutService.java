package com.gvsolutions.service;

import com.gvsolutions.domain.SalaryPayout;
import com.gvsolutions.repository.SalaryPayoutRepository;
import com.gvsolutions.repository.search.SalaryPayoutSearchRepository;
import com.gvsolutions.service.dto.SalaryPayoutDTO;
import com.gvsolutions.service.mapper.SalaryPayoutMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.SalaryPayout}.
 */
@Service
@Transactional
public class SalaryPayoutService {

    private static final Logger LOG = LoggerFactory.getLogger(SalaryPayoutService.class);

    private final SalaryPayoutRepository salaryPayoutRepository;

    private final SalaryPayoutMapper salaryPayoutMapper;

    private final SalaryPayoutSearchRepository salaryPayoutSearchRepository;

    public SalaryPayoutService(
        SalaryPayoutRepository salaryPayoutRepository,
        SalaryPayoutMapper salaryPayoutMapper,
        SalaryPayoutSearchRepository salaryPayoutSearchRepository
    ) {
        this.salaryPayoutRepository = salaryPayoutRepository;
        this.salaryPayoutMapper = salaryPayoutMapper;
        this.salaryPayoutSearchRepository = salaryPayoutSearchRepository;
    }

    /**
     * Save a salaryPayout.
     *
     * @param salaryPayoutDTO the entity to save.
     * @return the persisted entity.
     */
    public SalaryPayoutDTO save(SalaryPayoutDTO salaryPayoutDTO) {
        LOG.debug("Request to save SalaryPayout : {}", salaryPayoutDTO);
        SalaryPayout salaryPayout = salaryPayoutMapper.toEntity(salaryPayoutDTO);
        salaryPayout = salaryPayoutRepository.save(salaryPayout);
        salaryPayoutSearchRepository.index(salaryPayout);
        return salaryPayoutMapper.toDto(salaryPayout);
    }

    /**
     * Update a salaryPayout.
     *
     * @param salaryPayoutDTO the entity to save.
     * @return the persisted entity.
     */
    public SalaryPayoutDTO update(SalaryPayoutDTO salaryPayoutDTO) {
        LOG.debug("Request to update SalaryPayout : {}", salaryPayoutDTO);
        SalaryPayout salaryPayout = salaryPayoutMapper.toEntity(salaryPayoutDTO);
        salaryPayout.setIsPersisted();
        salaryPayout = salaryPayoutRepository.save(salaryPayout);
        salaryPayoutSearchRepository.index(salaryPayout);
        return salaryPayoutMapper.toDto(salaryPayout);
    }

    /**
     * Partially update a salaryPayout.
     *
     * @param salaryPayoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalaryPayoutDTO> partialUpdate(SalaryPayoutDTO salaryPayoutDTO) {
        LOG.debug("Request to partially update SalaryPayout : {}", salaryPayoutDTO);

        return salaryPayoutRepository
            .findById(salaryPayoutDTO.getId())
            .map(existingSalaryPayout -> {
                salaryPayoutMapper.partialUpdate(existingSalaryPayout, salaryPayoutDTO);

                return existingSalaryPayout;
            })
            .map(salaryPayoutRepository::save)
            .map(savedSalaryPayout -> {
                salaryPayoutSearchRepository.index(savedSalaryPayout);
                return savedSalaryPayout;
            })
            .map(salaryPayoutMapper::toDto);
    }

    /**
     * Get one salaryPayout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalaryPayoutDTO> findOne(Long id) {
        LOG.debug("Request to get SalaryPayout : {}", id);
        return salaryPayoutRepository.findById(id).map(salaryPayoutMapper::toDto);
    }

    /**
     * Delete the salaryPayout by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SalaryPayout : {}", id);
        salaryPayoutRepository.deleteById(id);
        salaryPayoutSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the salaryPayout corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SalaryPayoutDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SalaryPayouts for query {}", query);
        return salaryPayoutSearchRepository.search(query, pageable).map(salaryPayoutMapper::toDto);
    }
}
