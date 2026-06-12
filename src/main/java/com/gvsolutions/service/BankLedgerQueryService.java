package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.BankLedger;
import com.gvsolutions.repository.BankLedgerRepository;
import com.gvsolutions.repository.search.BankLedgerSearchRepository;
import com.gvsolutions.service.criteria.BankLedgerCriteria;
import com.gvsolutions.service.dto.BankLedgerDTO;
import com.gvsolutions.service.mapper.BankLedgerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BankLedger} entities in the database.
 * The main input is a {@link BankLedgerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BankLedgerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankLedgerQueryService extends QueryService<BankLedger> {

    private static final Logger LOG = LoggerFactory.getLogger(BankLedgerQueryService.class);

    private final BankLedgerRepository bankLedgerRepository;

    private final BankLedgerMapper bankLedgerMapper;

    private final BankLedgerSearchRepository bankLedgerSearchRepository;

    public BankLedgerQueryService(
        BankLedgerRepository bankLedgerRepository,
        BankLedgerMapper bankLedgerMapper,
        BankLedgerSearchRepository bankLedgerSearchRepository
    ) {
        this.bankLedgerRepository = bankLedgerRepository;
        this.bankLedgerMapper = bankLedgerMapper;
        this.bankLedgerSearchRepository = bankLedgerSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link BankLedgerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankLedgerDTO> findByCriteria(BankLedgerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BankLedger> specification = createSpecification(criteria);
        return bankLedgerRepository.findAll(specification, page).map(bankLedgerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankLedgerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<BankLedger> specification = createSpecification(criteria);
        return bankLedgerRepository.count(specification);
    }

    /**
     * Function to convert {@link BankLedgerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BankLedger> createSpecification(BankLedgerCriteria criteria) {
        Specification<BankLedger> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), BankLedger_.id),
                buildStringSpecification(criteria.getBranchCode(), BankLedger_.branchCode),
                buildStringSpecification(criteria.getBranchId(), BankLedger_.branchId),
                buildStringSpecification(criteria.getBankLedgerCode(), BankLedger_.bankLedgerCode),
                buildRangeSpecification(criteria.getDate(), BankLedger_.date),
                buildStringSpecification(criteria.getReferenceNo(), BankLedger_.referenceNo),
                buildStringSpecification(criteria.getDescription(), BankLedger_.description),
                buildRangeSpecification(criteria.getDepositAmount(), BankLedger_.depositAmount),
                buildRangeSpecification(criteria.getWithdrawalAmount(), BankLedger_.withdrawalAmount),
                buildRangeSpecification(criteria.getRunningBalance(), BankLedger_.runningBalance),
                buildStringSpecification(criteria.getRemark(), BankLedger_.remark),
                buildStringSpecification(criteria.getCreatedBy(), BankLedger_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), BankLedger_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), BankLedger_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), BankLedger_.lastModifiedDate)
            );
        }
        return specification;
    }
}
