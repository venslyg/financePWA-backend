package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.SalaryPayout;
import com.gvsolutions.repository.SalaryPayoutRepository;
import com.gvsolutions.repository.search.SalaryPayoutSearchRepository;
import com.gvsolutions.service.criteria.SalaryPayoutCriteria;
import com.gvsolutions.service.dto.SalaryPayoutDTO;
import com.gvsolutions.service.mapper.SalaryPayoutMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SalaryPayout} entities in the database.
 * The main input is a {@link SalaryPayoutCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SalaryPayoutDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SalaryPayoutQueryService extends QueryService<SalaryPayout> {

    private static final Logger LOG = LoggerFactory.getLogger(SalaryPayoutQueryService.class);

    private final SalaryPayoutRepository salaryPayoutRepository;

    private final SalaryPayoutMapper salaryPayoutMapper;

    private final SalaryPayoutSearchRepository salaryPayoutSearchRepository;

    public SalaryPayoutQueryService(
        SalaryPayoutRepository salaryPayoutRepository,
        SalaryPayoutMapper salaryPayoutMapper,
        SalaryPayoutSearchRepository salaryPayoutSearchRepository
    ) {
        this.salaryPayoutRepository = salaryPayoutRepository;
        this.salaryPayoutMapper = salaryPayoutMapper;
        this.salaryPayoutSearchRepository = salaryPayoutSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link SalaryPayoutDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SalaryPayoutDTO> findByCriteria(SalaryPayoutCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SalaryPayout> specification = createSpecification(criteria);
        return salaryPayoutRepository.findAll(specification, page).map(salaryPayoutMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SalaryPayoutCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SalaryPayout> specification = createSpecification(criteria);
        return salaryPayoutRepository.count(specification);
    }

    /**
     * Function to convert {@link SalaryPayoutCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SalaryPayout> createSpecification(SalaryPayoutCriteria criteria) {
        Specification<SalaryPayout> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SalaryPayout_.id),
                buildStringSpecification(criteria.getBranchCode(), SalaryPayout_.branchCode),
                buildStringSpecification(criteria.getSalaryPayoutCode(), SalaryPayout_.salaryPayoutCode),
                buildStringSpecification(criteria.getStaffCode(), SalaryPayout_.staffCode),
                buildStringSpecification(criteria.getPayPeriod(), SalaryPayout_.payPeriod),
                buildRangeSpecification(criteria.getBaseSalary(), SalaryPayout_.baseSalary),
                buildRangeSpecification(criteria.getAllowances(), SalaryPayout_.allowances),
                buildRangeSpecification(criteria.getDeductions(), SalaryPayout_.deductions),
                buildRangeSpecification(criteria.getNetPay(), SalaryPayout_.netPay),
                buildRangeSpecification(criteria.getPayoutDate(), SalaryPayout_.payoutDate),
                buildStringSpecification(criteria.getCreatedBy(), SalaryPayout_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), SalaryPayout_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), SalaryPayout_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), SalaryPayout_.lastModifiedDate)
            );
        }
        return specification;
    }
}
