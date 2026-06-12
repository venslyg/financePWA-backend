package com.gvsolutions.service;

import com.gvsolutions.domain.ExpenseEntry;
import com.gvsolutions.repository.ExpenseEntryRepository;
import com.gvsolutions.repository.search.ExpenseEntrySearchRepository;
import com.gvsolutions.service.dto.ExpenseEntryDTO;
import com.gvsolutions.service.mapper.ExpenseEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.ExpenseEntry}.
 */
@Service
@Transactional
public class ExpenseEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseEntryService.class);

    private final ExpenseEntryRepository expenseEntryRepository;

    private final ExpenseEntryMapper expenseEntryMapper;

    private final ExpenseEntrySearchRepository expenseEntrySearchRepository;

    public ExpenseEntryService(
        ExpenseEntryRepository expenseEntryRepository,
        ExpenseEntryMapper expenseEntryMapper,
        ExpenseEntrySearchRepository expenseEntrySearchRepository
    ) {
        this.expenseEntryRepository = expenseEntryRepository;
        this.expenseEntryMapper = expenseEntryMapper;
        this.expenseEntrySearchRepository = expenseEntrySearchRepository;
    }

    /**
     * Save a expenseEntry.
     *
     * @param expenseEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseEntryDTO save(ExpenseEntryDTO expenseEntryDTO) {
        LOG.debug("Request to save ExpenseEntry : {}", expenseEntryDTO);
        ExpenseEntry expenseEntry = expenseEntryMapper.toEntity(expenseEntryDTO);
        expenseEntry = expenseEntryRepository.save(expenseEntry);
        expenseEntrySearchRepository.index(expenseEntry);
        return expenseEntryMapper.toDto(expenseEntry);
    }

    /**
     * Update a expenseEntry.
     *
     * @param expenseEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseEntryDTO update(ExpenseEntryDTO expenseEntryDTO) {
        LOG.debug("Request to update ExpenseEntry : {}", expenseEntryDTO);
        ExpenseEntry expenseEntry = expenseEntryMapper.toEntity(expenseEntryDTO);
        expenseEntry.setIsPersisted();
        expenseEntry = expenseEntryRepository.save(expenseEntry);
        expenseEntrySearchRepository.index(expenseEntry);
        return expenseEntryMapper.toDto(expenseEntry);
    }

    /**
     * Partially update a expenseEntry.
     *
     * @param expenseEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpenseEntryDTO> partialUpdate(ExpenseEntryDTO expenseEntryDTO) {
        LOG.debug("Request to partially update ExpenseEntry : {}", expenseEntryDTO);

        return expenseEntryRepository
            .findById(expenseEntryDTO.getId())
            .map(existingExpenseEntry -> {
                expenseEntryMapper.partialUpdate(existingExpenseEntry, expenseEntryDTO);

                return existingExpenseEntry;
            })
            .map(expenseEntryRepository::save)
            .map(savedExpenseEntry -> {
                expenseEntrySearchRepository.index(savedExpenseEntry);
                return savedExpenseEntry;
            })
            .map(expenseEntryMapper::toDto);
    }

    /**
     * Get one expenseEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseEntryDTO> findOne(Long id) {
        LOG.debug("Request to get ExpenseEntry : {}", id);
        return expenseEntryRepository.findById(id).map(expenseEntryMapper::toDto);
    }

    /**
     * Delete the expenseEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExpenseEntry : {}", id);
        expenseEntryRepository.deleteById(id);
        expenseEntrySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the expenseEntry corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseEntryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ExpenseEntries for query {}", query);
        return expenseEntrySearchRepository.search(query, pageable).map(expenseEntryMapper::toDto);
    }
}
