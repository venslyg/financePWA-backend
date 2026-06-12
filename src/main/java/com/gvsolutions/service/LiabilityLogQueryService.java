package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.LiabilityLog;
import com.gvsolutions.repository.LiabilityLogRepository;
import com.gvsolutions.repository.search.LiabilityLogSearchRepository;
import com.gvsolutions.service.criteria.LiabilityLogCriteria;
import com.gvsolutions.service.dto.LiabilityLogDTO;
import com.gvsolutions.service.mapper.LiabilityLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LiabilityLog} entities in the database.
 * The main input is a {@link LiabilityLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LiabilityLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LiabilityLogQueryService extends QueryService<LiabilityLog> {

    private static final Logger LOG = LoggerFactory.getLogger(LiabilityLogQueryService.class);

    private final LiabilityLogRepository liabilityLogRepository;

    private final LiabilityLogMapper liabilityLogMapper;

    private final LiabilityLogSearchRepository liabilityLogSearchRepository;

    public LiabilityLogQueryService(
        LiabilityLogRepository liabilityLogRepository,
        LiabilityLogMapper liabilityLogMapper,
        LiabilityLogSearchRepository liabilityLogSearchRepository
    ) {
        this.liabilityLogRepository = liabilityLogRepository;
        this.liabilityLogMapper = liabilityLogMapper;
        this.liabilityLogSearchRepository = liabilityLogSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link LiabilityLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LiabilityLogDTO> findByCriteria(LiabilityLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LiabilityLog> specification = createSpecification(criteria);
        return liabilityLogRepository.findAll(specification, page).map(liabilityLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LiabilityLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LiabilityLog> specification = createSpecification(criteria);
        return liabilityLogRepository.count(specification);
    }

    /**
     * Function to convert {@link LiabilityLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LiabilityLog> createSpecification(LiabilityLogCriteria criteria) {
        Specification<LiabilityLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), LiabilityLog_.id),
                buildStringSpecification(criteria.getBranchCode(), LiabilityLog_.branchCode),
                buildStringSpecification(criteria.getBranchId(), LiabilityLog_.branchId),
                buildStringSpecification(criteria.getLiabilityCode(), LiabilityLog_.liabilityCode),
                buildStringSpecification(criteria.getLoanFrom(), LiabilityLog_.loanFrom),
                buildStringSpecification(criteria.getDescription(), LiabilityLog_.description),
                buildSpecification(criteria.getLiabilityType(), LiabilityLog_.liabilityType),
                buildRangeSpecification(criteria.getTotalLoanAmount(), LiabilityLog_.totalLoanAmount),
                buildRangeSpecification(criteria.getStartDate(), LiabilityLog_.startDate),
                buildRangeSpecification(criteria.getEndDate(), LiabilityLog_.endDate),
                buildRangeSpecification(criteria.getInterestPercentage(), LiabilityLog_.interestPercentage),
                buildRangeSpecification(criteria.getMonthlyPaymentAmount(), LiabilityLog_.monthlyPaymentAmount),
                buildRangeSpecification(criteria.getPrincipalPaid(), LiabilityLog_.principalPaid),
                buildRangeSpecification(criteria.getBalanceToPay(), LiabilityLog_.balanceToPay),
                buildSpecification(criteria.getStatus(), LiabilityLog_.status),
                buildStringSpecification(criteria.getCreatedBy(), LiabilityLog_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), LiabilityLog_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), LiabilityLog_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), LiabilityLog_.lastModifiedDate)
            );
        }
        return specification;
    }
}
