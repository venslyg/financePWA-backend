package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.BudgetPlan;
import com.gvsolutions.repository.BudgetPlanRepository;
import com.gvsolutions.repository.search.BudgetPlanSearchRepository;
import com.gvsolutions.service.criteria.BudgetPlanCriteria;
import com.gvsolutions.service.dto.BudgetPlanDTO;
import com.gvsolutions.service.mapper.BudgetPlanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BudgetPlan} entities in the database.
 * The main input is a {@link BudgetPlanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BudgetPlanDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BudgetPlanQueryService extends QueryService<BudgetPlan> {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetPlanQueryService.class);

    private final BudgetPlanRepository budgetPlanRepository;

    private final BudgetPlanMapper budgetPlanMapper;

    private final BudgetPlanSearchRepository budgetPlanSearchRepository;

    public BudgetPlanQueryService(
        BudgetPlanRepository budgetPlanRepository,
        BudgetPlanMapper budgetPlanMapper,
        BudgetPlanSearchRepository budgetPlanSearchRepository
    ) {
        this.budgetPlanRepository = budgetPlanRepository;
        this.budgetPlanMapper = budgetPlanMapper;
        this.budgetPlanSearchRepository = budgetPlanSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link BudgetPlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BudgetPlanDTO> findByCriteria(BudgetPlanCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BudgetPlan> specification = createSpecification(criteria);
        return budgetPlanRepository.findAll(specification, page).map(budgetPlanMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BudgetPlanCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<BudgetPlan> specification = createSpecification(criteria);
        return budgetPlanRepository.count(specification);
    }

    /**
     * Function to convert {@link BudgetPlanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BudgetPlan> createSpecification(BudgetPlanCriteria criteria) {
        Specification<BudgetPlan> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), BudgetPlan_.id),
                buildStringSpecification(criteria.getBranchCode(), BudgetPlan_.branchCode),
                buildStringSpecification(criteria.getAccountCode(), BudgetPlan_.accountCode),
                buildStringSpecification(criteria.getBudgetPlanCode(), BudgetPlan_.budgetPlanCode),
                buildStringSpecification(criteria.getDepartmentName(), BudgetPlan_.departmentName),
                buildRangeSpecification(criteria.getYear(), BudgetPlan_.year),
                buildRangeSpecification(criteria.getAllocatedAmount(), BudgetPlan_.allocatedAmount),
                buildRangeSpecification(criteria.getSpentAmount(), BudgetPlan_.spentAmount),
                buildRangeSpecification(criteria.getRemainingAmount(), BudgetPlan_.remainingAmount),
                buildRangeSpecification(criteria.getUsedPercentage(), BudgetPlan_.usedPercentage),
                buildSpecification(criteria.getAlertStatus(), BudgetPlan_.alertStatus),
                buildStringSpecification(criteria.getCreatedBy(), BudgetPlan_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), BudgetPlan_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), BudgetPlan_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), BudgetPlan_.lastModifiedDate)
            );
        }
        return specification;
    }
}
