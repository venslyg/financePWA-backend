package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.PettyCashLedger;
import com.gvsolutions.repository.PettyCashLedgerRepository;
import com.gvsolutions.repository.search.PettyCashLedgerSearchRepository;
import com.gvsolutions.service.criteria.PettyCashLedgerCriteria;
import com.gvsolutions.service.dto.PettyCashLedgerDTO;
import com.gvsolutions.service.mapper.PettyCashLedgerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PettyCashLedger} entities in the database.
 * The main input is a {@link PettyCashLedgerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PettyCashLedgerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PettyCashLedgerQueryService extends QueryService<PettyCashLedger> {

    private static final Logger LOG = LoggerFactory.getLogger(PettyCashLedgerQueryService.class);

    private final PettyCashLedgerRepository pettyCashLedgerRepository;

    private final PettyCashLedgerMapper pettyCashLedgerMapper;

    private final PettyCashLedgerSearchRepository pettyCashLedgerSearchRepository;

    public PettyCashLedgerQueryService(
        PettyCashLedgerRepository pettyCashLedgerRepository,
        PettyCashLedgerMapper pettyCashLedgerMapper,
        PettyCashLedgerSearchRepository pettyCashLedgerSearchRepository
    ) {
        this.pettyCashLedgerRepository = pettyCashLedgerRepository;
        this.pettyCashLedgerMapper = pettyCashLedgerMapper;
        this.pettyCashLedgerSearchRepository = pettyCashLedgerSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link PettyCashLedgerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PettyCashLedgerDTO> findByCriteria(PettyCashLedgerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PettyCashLedger> specification = createSpecification(criteria);
        return pettyCashLedgerRepository.findAll(specification, page).map(pettyCashLedgerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PettyCashLedgerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PettyCashLedger> specification = createSpecification(criteria);
        return pettyCashLedgerRepository.count(specification);
    }

    /**
     * Function to convert {@link PettyCashLedgerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PettyCashLedger> createSpecification(PettyCashLedgerCriteria criteria) {
        Specification<PettyCashLedger> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), PettyCashLedger_.id),
                buildStringSpecification(criteria.getBranchCode(), PettyCashLedger_.branchCode),
                buildStringSpecification(criteria.getPettyCashCode(), PettyCashLedger_.pettyCashCode),
                buildRangeSpecification(criteria.getDate(), PettyCashLedger_.date),
                buildStringSpecification(criteria.getPettyCashVoucherNo(), PettyCashLedger_.pettyCashVoucherNo),
                buildStringSpecification(criteria.getDescription(), PettyCashLedger_.description),
                buildRangeSpecification(criteria.getCashIn(), PettyCashLedger_.cashIn),
                buildRangeSpecification(criteria.getCashOut(), PettyCashLedger_.cashOut),
                buildRangeSpecification(criteria.getRunningBalance(), PettyCashLedger_.runningBalance),
                buildStringSpecification(criteria.getLinkedAccountCode(), PettyCashLedger_.linkedAccountCode),
                buildStringSpecification(criteria.getReferenceNo(), PettyCashLedger_.referenceNo),
                buildStringSpecification(criteria.getCreatedBy(), PettyCashLedger_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), PettyCashLedger_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), PettyCashLedger_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), PettyCashLedger_.lastModifiedDate)
            );
        }
        return specification;
    }
}
