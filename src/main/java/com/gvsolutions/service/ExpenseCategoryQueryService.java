package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.ExpenseCategory;
import com.gvsolutions.repository.ExpenseCategoryRepository;
import com.gvsolutions.repository.search.ExpenseCategorySearchRepository;
import com.gvsolutions.service.criteria.ExpenseCategoryCriteria;
import com.gvsolutions.service.dto.ExpenseCategoryDTO;
import com.gvsolutions.service.mapper.ExpenseCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExpenseCategory} entities in the database.
 * The main input is a {@link ExpenseCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExpenseCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseCategoryQueryService extends QueryService<ExpenseCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseCategoryQueryService.class);

    private final ExpenseCategoryRepository expenseCategoryRepository;

    private final ExpenseCategoryMapper expenseCategoryMapper;

    private final ExpenseCategorySearchRepository expenseCategorySearchRepository;

    public ExpenseCategoryQueryService(
        ExpenseCategoryRepository expenseCategoryRepository,
        ExpenseCategoryMapper expenseCategoryMapper,
        ExpenseCategorySearchRepository expenseCategorySearchRepository
    ) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.expenseCategoryMapper = expenseCategoryMapper;
        this.expenseCategorySearchRepository = expenseCategorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ExpenseCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseCategoryDTO> findByCriteria(ExpenseCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpenseCategory> specification = createSpecification(criteria);
        return expenseCategoryRepository.findAll(specification, page).map(expenseCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExpenseCategory> specification = createSpecification(criteria);
        return expenseCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpenseCategory> createSpecification(ExpenseCategoryCriteria criteria) {
        Specification<ExpenseCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExpenseCategory_.id),
                buildStringSpecification(criteria.getCategoryCode(), ExpenseCategory_.categoryCode),
                buildStringSpecification(criteria.getCategoryName(), ExpenseCategory_.categoryName),
                buildStringSpecification(criteria.getDescription(), ExpenseCategory_.description),
                buildStringSpecification(criteria.getCreatedBy(), ExpenseCategory_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ExpenseCategory_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), ExpenseCategory_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), ExpenseCategory_.lastModifiedDate)
            );
        }
        return specification;
    }
}
