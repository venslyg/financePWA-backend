package com.gvsolutions.service;

import com.gvsolutions.domain.IncomeEntry;
import com.gvsolutions.repository.IncomeEntryRepository;
import com.gvsolutions.repository.search.IncomeEntrySearchRepository;
import com.gvsolutions.service.dto.IncomeEntryDTO;
import com.gvsolutions.service.mapper.IncomeEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.IncomeEntry}.
 */
@Service
@Transactional
public class IncomeEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(IncomeEntryService.class);

    private final IncomeEntryRepository incomeEntryRepository;

    private final IncomeEntryMapper incomeEntryMapper;

    private final IncomeEntrySearchRepository incomeEntrySearchRepository;

    public IncomeEntryService(
        IncomeEntryRepository incomeEntryRepository,
        IncomeEntryMapper incomeEntryMapper,
        IncomeEntrySearchRepository incomeEntrySearchRepository
    ) {
        this.incomeEntryRepository = incomeEntryRepository;
        this.incomeEntryMapper = incomeEntryMapper;
        this.incomeEntrySearchRepository = incomeEntrySearchRepository;
    }

    /**
     * Save a incomeEntry.
     *
     * @param incomeEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public IncomeEntryDTO save(IncomeEntryDTO incomeEntryDTO) {
        LOG.debug("Request to save IncomeEntry : {}", incomeEntryDTO);
        IncomeEntry incomeEntry = incomeEntryMapper.toEntity(incomeEntryDTO);
        incomeEntry = incomeEntryRepository.save(incomeEntry);
        incomeEntrySearchRepository.index(incomeEntry);
        return incomeEntryMapper.toDto(incomeEntry);
    }

    /**
     * Update a incomeEntry.
     *
     * @param incomeEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public IncomeEntryDTO update(IncomeEntryDTO incomeEntryDTO) {
        LOG.debug("Request to update IncomeEntry : {}", incomeEntryDTO);
        IncomeEntry incomeEntry = incomeEntryMapper.toEntity(incomeEntryDTO);
        incomeEntry.setIsPersisted();
        incomeEntry = incomeEntryRepository.save(incomeEntry);
        incomeEntrySearchRepository.index(incomeEntry);
        return incomeEntryMapper.toDto(incomeEntry);
    }

    /**
     * Partially update a incomeEntry.
     *
     * @param incomeEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IncomeEntryDTO> partialUpdate(IncomeEntryDTO incomeEntryDTO) {
        LOG.debug("Request to partially update IncomeEntry : {}", incomeEntryDTO);

        return incomeEntryRepository
            .findById(incomeEntryDTO.getId())
            .map(existingIncomeEntry -> {
                incomeEntryMapper.partialUpdate(existingIncomeEntry, incomeEntryDTO);

                return existingIncomeEntry;
            })
            .map(incomeEntryRepository::save)
            .map(savedIncomeEntry -> {
                incomeEntrySearchRepository.index(savedIncomeEntry);
                return savedIncomeEntry;
            })
            .map(incomeEntryMapper::toDto);
    }

    /**
     * Get one incomeEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IncomeEntryDTO> findOne(Long id) {
        LOG.debug("Request to get IncomeEntry : {}", id);
        return incomeEntryRepository.findById(id).map(incomeEntryMapper::toDto);
    }

    /**
     * Delete the incomeEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IncomeEntry : {}", id);
        incomeEntryRepository.deleteById(id);
        incomeEntrySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the incomeEntry corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IncomeEntryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of IncomeEntries for query {}", query);
        return incomeEntrySearchRepository.search(query, pageable).map(incomeEntryMapper::toDto);
    }
}
