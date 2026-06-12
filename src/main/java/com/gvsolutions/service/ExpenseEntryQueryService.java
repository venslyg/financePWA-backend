package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.ExpenseEntry;
import com.gvsolutions.repository.ExpenseEntryRepository;
import com.gvsolutions.repository.search.ExpenseEntrySearchRepository;
import com.gvsolutions.service.criteria.ExpenseEntryCriteria;
import com.gvsolutions.service.dto.ExpenseEntryDTO;
import com.gvsolutions.service.mapper.ExpenseEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExpenseEntry} entities in the database.
 * The main input is a {@link ExpenseEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExpenseEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseEntryQueryService extends QueryService<ExpenseEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseEntryQueryService.class);

    private final ExpenseEntryRepository expenseEntryRepository;

    private final ExpenseEntryMapper expenseEntryMapper;

    private final ExpenseEntrySearchRepository expenseEntrySearchRepository;

    public ExpenseEntryQueryService(
        ExpenseEntryRepository expenseEntryRepository,
        ExpenseEntryMapper expenseEntryMapper,
        ExpenseEntrySearchRepository expenseEntrySearchRepository
    ) {
        this.expenseEntryRepository = expenseEntryRepository;
        this.expenseEntryMapper = expenseEntryMapper;
        this.expenseEntrySearchRepository = expenseEntrySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ExpenseEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseEntryDTO> findByCriteria(ExpenseEntryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExpenseEntry> specification = createSpecification(criteria);
        return expenseEntryRepository.findAll(specification, page).map(expenseEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseEntryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExpenseEntry> specification = createSpecification(criteria);
        return expenseEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExpenseEntry> createSpecification(ExpenseEntryCriteria criteria) {
        Specification<ExpenseEntry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExpenseEntry_.id),
                buildStringSpecification(criteria.getBranchCode(), ExpenseEntry_.branchCode),
                buildStringSpecification(criteria.getAccountCode(), ExpenseEntry_.accountCode),
                buildStringSpecification(criteria.getExpenseCode(), ExpenseEntry_.expenseCode),
                buildStringSpecification(criteria.getExpenseCategoryCode(), ExpenseEntry_.expenseCategoryCode),
                buildStringSpecification(criteria.getExpenseSubCategoryCode(), ExpenseEntry_.expenseSubCategoryCode),
                buildStringSpecification(criteria.getCreatedByUsername(), ExpenseEntry_.createdByUsername),
                buildRangeSpecification(criteria.getDate(), ExpenseEntry_.date),
                buildStringSpecification(criteria.getVoucherNo(), ExpenseEntry_.voucherNo),
                buildStringSpecification(criteria.getDescription(), ExpenseEntry_.description),
                buildRangeSpecification(criteria.getAmount(), ExpenseEntry_.amount),
                buildSpecification(criteria.getPaymentMode(), ExpenseEntry_.paymentMode),
                buildSpecification(criteria.getApprovalStatus(), ExpenseEntry_.approvalStatus),
                buildStringSpecification(criteria.getApprovedBy(), ExpenseEntry_.approvedBy),
                buildStringSpecification(criteria.getVendor(), ExpenseEntry_.vendor),
                buildSpecification(criteria.getSyncStatus(), ExpenseEntry_.syncStatus),
                buildStringSpecification(criteria.getCreatedBy(), ExpenseEntry_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ExpenseEntry_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), ExpenseEntry_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), ExpenseEntry_.lastModifiedDate)
            );
        }
        return specification;
    }
}
