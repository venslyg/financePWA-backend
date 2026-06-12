package com.gvsolutions.service;

import com.gvsolutions.domain.ExpenseCategory;
import com.gvsolutions.repository.ExpenseCategoryRepository;
import com.gvsolutions.repository.search.ExpenseCategorySearchRepository;
import com.gvsolutions.service.dto.ExpenseCategoryDTO;
import com.gvsolutions.service.mapper.ExpenseCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.ExpenseCategory}.
 */
@Service
@Transactional
public class ExpenseCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseCategoryService.class);

    private final ExpenseCategoryRepository expenseCategoryRepository;

    private final ExpenseCategoryMapper expenseCategoryMapper;

    private final ExpenseCategorySearchRepository expenseCategorySearchRepository;

    public ExpenseCategoryService(
        ExpenseCategoryRepository expenseCategoryRepository,
        ExpenseCategoryMapper expenseCategoryMapper,
        ExpenseCategorySearchRepository expenseCategorySearchRepository
    ) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.expenseCategoryMapper = expenseCategoryMapper;
        this.expenseCategorySearchRepository = expenseCategorySearchRepository;
    }

    /**
     * Save a expenseCategory.
     *
     * @param expenseCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseCategoryDTO save(ExpenseCategoryDTO expenseCategoryDTO) {
        LOG.debug("Request to save ExpenseCategory : {}", expenseCategoryDTO);
        ExpenseCategory expenseCategory = expenseCategoryMapper.toEntity(expenseCategoryDTO);
        expenseCategory = expenseCategoryRepository.save(expenseCategory);
        expenseCategorySearchRepository.index(expenseCategory);
        return expenseCategoryMapper.toDto(expenseCategory);
    }

    /**
     * Update a expenseCategory.
     *
     * @param expenseCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseCategoryDTO update(ExpenseCategoryDTO expenseCategoryDTO) {
        LOG.debug("Request to update ExpenseCategory : {}", expenseCategoryDTO);
        ExpenseCategory expenseCategory = expenseCategoryMapper.toEntity(expenseCategoryDTO);
        expenseCategory.setIsPersisted();
        expenseCategory = expenseCategoryRepository.save(expenseCategory);
        expenseCategorySearchRepository.index(expenseCategory);
        return expenseCategoryMapper.toDto(expenseCategory);
    }

    /**
     * Partially update a expenseCategory.
     *
     * @param expenseCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpenseCategoryDTO> partialUpdate(ExpenseCategoryDTO expenseCategoryDTO) {
        LOG.debug("Request to partially update ExpenseCategory : {}", expenseCategoryDTO);

        return expenseCategoryRepository
            .findById(expenseCategoryDTO.getId())
            .map(existingExpenseCategory -> {
                expenseCategoryMapper.partialUpdate(existingExpenseCategory, expenseCategoryDTO);

                return existingExpenseCategory;
            })
            .map(expenseCategoryRepository::save)
            .map(savedExpenseCategory -> {
                expenseCategorySearchRepository.index(savedExpenseCategory);
                return savedExpenseCategory;
            })
            .map(expenseCategoryMapper::toDto);
    }

    /**
     * Get one expenseCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get ExpenseCategory : {}", id);
        return expenseCategoryRepository.findById(id).map(expenseCategoryMapper::toDto);
    }

    /**
     * Delete the expenseCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExpenseCategory : {}", id);
        expenseCategoryRepository.deleteById(id);
        expenseCategorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the expenseCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseCategoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ExpenseCategories for query {}", query);
        return expenseCategorySearchRepository.search(query, pageable).map(expenseCategoryMapper::toDto);
    }
}
