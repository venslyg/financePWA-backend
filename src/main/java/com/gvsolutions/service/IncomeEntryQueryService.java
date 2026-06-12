package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.IncomeEntry;
import com.gvsolutions.repository.IncomeEntryRepository;
import com.gvsolutions.repository.search.IncomeEntrySearchRepository;
import com.gvsolutions.service.criteria.IncomeEntryCriteria;
import com.gvsolutions.service.dto.IncomeEntryDTO;
import com.gvsolutions.service.mapper.IncomeEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IncomeEntry} entities in the database.
 * The main input is a {@link IncomeEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IncomeEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IncomeEntryQueryService extends QueryService<IncomeEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(IncomeEntryQueryService.class);

    private final IncomeEntryRepository incomeEntryRepository;

    private final IncomeEntryMapper incomeEntryMapper;

    private final IncomeEntrySearchRepository incomeEntrySearchRepository;

    public IncomeEntryQueryService(
        IncomeEntryRepository incomeEntryRepository,
        IncomeEntryMapper incomeEntryMapper,
        IncomeEntrySearchRepository incomeEntrySearchRepository
    ) {
        this.incomeEntryRepository = incomeEntryRepository;
        this.incomeEntryMapper = incomeEntryMapper;
        this.incomeEntrySearchRepository = incomeEntrySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link IncomeEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IncomeEntryDTO> findByCriteria(IncomeEntryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IncomeEntry> specification = createSpecification(criteria);
        return incomeEntryRepository.findAll(specification, page).map(incomeEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IncomeEntryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IncomeEntry> specification = createSpecification(criteria);
        return incomeEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link IncomeEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IncomeEntry> createSpecification(IncomeEntryCriteria criteria) {
        Specification<IncomeEntry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IncomeEntry_.id),
                buildStringSpecification(criteria.getBranchCode(), IncomeEntry_.branchCode),
                buildStringSpecification(criteria.getAccountCode(), IncomeEntry_.accountCode),
                buildStringSpecification(criteria.getIncomeCode(), IncomeEntry_.incomeCode),
                buildStringSpecification(criteria.getCreatedByUsername(), IncomeEntry_.createdByUsername),
                buildRangeSpecification(criteria.getDate(), IncomeEntry_.date),
                buildStringSpecification(criteria.getReceiptNo(), IncomeEntry_.receiptNo),
                buildStringSpecification(criteria.getDescription(), IncomeEntry_.description),
                buildSpecification(criteria.getIncomeType(), IncomeEntry_.incomeType),
                buildRangeSpecification(criteria.getAmount(), IncomeEntry_.amount),
                buildSpecification(criteria.getPaymentMethod(), IncomeEntry_.paymentMethod),
                buildStringSpecification(criteria.getReceivablePerson(), IncomeEntry_.receivablePerson),
                buildStringSpecification(criteria.getReceivedBy(), IncomeEntry_.receivedBy),
                buildSpecification(criteria.getSyncStatus(), IncomeEntry_.syncStatus),
                buildStringSpecification(criteria.getCreatedBy(), IncomeEntry_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), IncomeEntry_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), IncomeEntry_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), IncomeEntry_.lastModifiedDate)
            );
        }
        return specification;
    }
}
