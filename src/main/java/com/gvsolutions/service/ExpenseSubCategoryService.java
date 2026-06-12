package com.gvsolutions.service;

import com.gvsolutions.domain.ExpenseSubCategory;
import com.gvsolutions.repository.ExpenseSubCategoryRepository;
import com.gvsolutions.repository.search.ExpenseSubCategorySearchRepository;
import com.gvsolutions.service.dto.ExpenseSubCategoryDTO;
import com.gvsolutions.service.mapper.ExpenseSubCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.ExpenseSubCategory}.
 */
@Service
@Transactional
public class ExpenseSubCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseSubCategoryService.class);

    private final ExpenseSubCategoryRepository expenseSubCategoryRepository;

    private final ExpenseSubCategoryMapper expenseSubCategoryMapper;

    private final ExpenseSubCategorySearchRepository expenseSubCategorySearchRepository;

    public ExpenseSubCategoryService(
        ExpenseSubCategoryRepository expenseSubCategoryRepository,
        ExpenseSubCategoryMapper expenseSubCategoryMapper,
        ExpenseSubCategorySearchRepository expenseSubCategorySearchRepository
    ) {
        this.expenseSubCategoryRepository = expenseSubCategoryRepository;
        this.expenseSubCategoryMapper = expenseSubCategoryMapper;
        this.expenseSubCategorySearchRepository = expenseSubCategorySearchRepository;
    }

    /**
     * Save a expenseSubCategory.
     *
     * @param expenseSubCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseSubCategoryDTO save(ExpenseSubCategoryDTO expenseSubCategoryDTO) {
        LOG.debug("Request to save ExpenseSubCategory : {}", expenseSubCategoryDTO);
        ExpenseSubCategory expenseSubCategory = expenseSubCategoryMapper.toEntity(expenseSubCategoryDTO);
        expenseSubCategory = expenseSubCategoryRepository.save(expenseSubCategory);
        expenseSubCategorySearchRepository.index(expenseSubCategory);
        return expenseSubCategoryMapper.toDto(expenseSubCategory);
    }

    /**
     * Update a expenseSubCategory.
     *
     * @param expenseSubCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExpenseSubCategoryDTO update(ExpenseSubCategoryDTO expenseSubCategoryDTO) {
        LOG.debug("Request to update ExpenseSubCategory : {}", expenseSubCategoryDTO);
        ExpenseSubCategory expenseSubCategory = expenseSubCategoryMapper.toEntity(expenseSubCategoryDTO);
        expenseSubCategory.setIsPersisted();
        expenseSubCategory = expenseSubCategoryRepository.save(expenseSubCategory);
        expenseSubCategorySearchRepository.index(expenseSubCategory);
        return expenseSubCategoryMapper.toDto(expenseSubCategory);
    }

    /**
     * Partially update a expenseSubCategory.
     *
     * @param expenseSubCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExpenseSubCategoryDTO> partialUpdate(ExpenseSubCategoryDTO expenseSubCategoryDTO) {
        LOG.debug("Request to partially update ExpenseSubCategory : {}", expenseSubCategoryDTO);

        return expenseSubCategoryRepository
            .findById(expenseSubCategoryDTO.getId())
            .map(existingExpenseSubCategory -> {
                expenseSubCategoryMapper.partialUpdate(existingExpenseSubCategory, expenseSubCategoryDTO);

                return existingExpenseSubCategory;
            })
            .map(expenseSubCategoryRepository::save)
            .map(savedExpenseSubCategory -> {
                expenseSubCategorySearchRepository.index(savedExpenseSubCategory);
                return savedExpenseSubCategory;
            })
            .map(expenseSubCategoryMapper::toDto);
    }

    /**
     * Get all the expenseSubCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ExpenseSubCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return expenseSubCategoryRepository.findAllWithEagerRelationships(pageable).map(expenseSubCategoryMapper::toDto);
    }

    /**
     * Get one expenseSubCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseSubCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get ExpenseSubCategory : {}", id);
        return expenseSubCategoryRepository.findOneWithEagerRelationships(id).map(expenseSubCategoryMapper::toDto);
    }

    /**
     * Delete the expenseSubCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExpenseSubCategory : {}", id);
        expenseSubCategoryRepository.deleteById(id);
        expenseSubCategorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the expenseSubCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseSubCategoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ExpenseSubCategories for query {}", query);
        return expenseSubCategorySearchRepository.search(query, pageable).map(expenseSubCategoryMapper::toDto);
    }
}
