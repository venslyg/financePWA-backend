package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.ExpenseSubCategory;
import com.gvsolutions.repository.ExpenseSubCategoryRepository;
import com.gvsolutions.repository.search.ExpenseSubCategorySearchRepository;
import com.gvsolutions.service.criteria.ExpenseSubCategoryCriteria;
import com.gvsolutions.service.dto.ExpenseSubCategoryDTO;
import com.gvsolutions.service.mapper.ExpenseSubCategoryMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExpenseSubCategory} entities in the database.
 * The main input is a {@link ExpenseSubCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExpenseSubCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseSubCategoryQueryService extends QueryService<ExpenseSubCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseSubCategoryQueryService.class);

    private final ExpenseSubCategoryRepository expenseSubCategoryRepository;

    private final ExpenseSubCategoryMapper expenseSubCategoryMapper;

    private final ExpenseSubCategorySearchRepository expenseSubCategorySearchRepository;

    public ExpenseSubCategoryQueryService(
        ExpenseSubCategoryRepository expenseSubCategoryRepository,
        ExpenseSubCategoryMapper expenseSubCategoryMapper,
        ExpenseSubCategorySearchRepository expenseSubCategorySearchRepository
    ) {
        this.expenseSubCategoryRepository = expenseSubCategoryRepository;
        this.expenseSubCategoryMapper = expenseSubCategoryMapper;
        this.expenseSubCategorySearchRepository = expenseSubCategorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ExpenseSubCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseSubCategoryDTO> findByCriteria(ExpenseSubCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpenseSubCategory> specification = createSpecification(criteria);
        return expenseSubCategoryRepository.findAll(specification, page).map(expenseSubCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseSubCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExpenseSubCategory> specification = createSpecification(criteria);
        return expenseSubCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseSubCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpenseSubCategory> createSpecification(ExpenseSubCategoryCriteria criteria) {
        Specification<ExpenseSubCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExpenseSubCategory_.id),
                buildStringSpecification(criteria.getCategoryCode(), ExpenseSubCategory_.categoryCode),
                buildStringSpecification(criteria.getSubCategoryCode(), ExpenseSubCategory_.subCategoryCode),
                buildStringSpecification(criteria.getSubCategoryName(), ExpenseSubCategory_.subCategoryName),
                buildStringSpecification(criteria.getCreatedBy(), ExpenseSubCategory_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ExpenseSubCategory_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), ExpenseSubCategory_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), ExpenseSubCategory_.lastModifiedDate),
                buildSpecification(criteria.getCategoryId(), root ->
                    root.join(ExpenseSubCategory_.category, JoinType.LEFT).get(ExpenseCategory_.id)
                )
            );
        }
        return specification;
    }
}
