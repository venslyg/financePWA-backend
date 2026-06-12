package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.ChurchStaff;
import com.gvsolutions.repository.ChurchStaffRepository;
import com.gvsolutions.repository.search.ChurchStaffSearchRepository;
import com.gvsolutions.service.criteria.ChurchStaffCriteria;
import com.gvsolutions.service.dto.ChurchStaffDTO;
import com.gvsolutions.service.mapper.ChurchStaffMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ChurchStaff} entities in the database.
 * The main input is a {@link ChurchStaffCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ChurchStaffDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChurchStaffQueryService extends QueryService<ChurchStaff> {

    private static final Logger LOG = LoggerFactory.getLogger(ChurchStaffQueryService.class);

    private final ChurchStaffRepository churchStaffRepository;

    private final ChurchStaffMapper churchStaffMapper;

    private final ChurchStaffSearchRepository churchStaffSearchRepository;

    public ChurchStaffQueryService(
        ChurchStaffRepository churchStaffRepository,
        ChurchStaffMapper churchStaffMapper,
        ChurchStaffSearchRepository churchStaffSearchRepository
    ) {
        this.churchStaffRepository = churchStaffRepository;
        this.churchStaffMapper = churchStaffMapper;
        this.churchStaffSearchRepository = churchStaffSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ChurchStaffDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChurchStaffDTO> findByCriteria(ChurchStaffCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChurchStaff> specification = createSpecification(criteria);
        return churchStaffRepository.findAll(specification, page).map(churchStaffMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChurchStaffCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ChurchStaff> specification = createSpecification(criteria);
        return churchStaffRepository.count(specification);
    }

    /**
     * Function to convert {@link ChurchStaffCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChurchStaff> createSpecification(ChurchStaffCriteria criteria) {
        Specification<ChurchStaff> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ChurchStaff_.id),
                buildStringSpecification(criteria.getStaffCode(), ChurchStaff_.staffCode),
                buildStringSpecification(criteria.getBranchCode(), ChurchStaff_.branchCode),
                buildStringSpecification(criteria.getFullName(), ChurchStaff_.fullName),
                buildStringSpecification(criteria.getPosition(), ChurchStaff_.position),
                buildSpecification(criteria.getStaffType(), ChurchStaff_.staffType),
                buildStringSpecification(criteria.getContactNumber(), ChurchStaff_.contactNumber),
                buildRangeSpecification(criteria.getHourlyRateOrMonthlySalary(), ChurchStaff_.hourlyRateOrMonthlySalary),
                buildSpecification(criteria.getIsActive(), ChurchStaff_.isActive),
                buildStringSpecification(criteria.getCreatedBy(), ChurchStaff_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ChurchStaff_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), ChurchStaff_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), ChurchStaff_.lastModifiedDate)
            );
        }
        return specification;
    }
}
