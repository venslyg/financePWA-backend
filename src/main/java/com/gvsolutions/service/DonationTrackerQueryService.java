package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.DonationTracker;
import com.gvsolutions.repository.DonationTrackerRepository;
import com.gvsolutions.repository.search.DonationTrackerSearchRepository;
import com.gvsolutions.service.criteria.DonationTrackerCriteria;
import com.gvsolutions.service.dto.DonationTrackerDTO;
import com.gvsolutions.service.mapper.DonationTrackerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DonationTracker} entities in the database.
 * The main input is a {@link DonationTrackerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DonationTrackerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DonationTrackerQueryService extends QueryService<DonationTracker> {

    private static final Logger LOG = LoggerFactory.getLogger(DonationTrackerQueryService.class);

    private final DonationTrackerRepository donationTrackerRepository;

    private final DonationTrackerMapper donationTrackerMapper;

    private final DonationTrackerSearchRepository donationTrackerSearchRepository;

    public DonationTrackerQueryService(
        DonationTrackerRepository donationTrackerRepository,
        DonationTrackerMapper donationTrackerMapper,
        DonationTrackerSearchRepository donationTrackerSearchRepository
    ) {
        this.donationTrackerRepository = donationTrackerRepository;
        this.donationTrackerMapper = donationTrackerMapper;
        this.donationTrackerSearchRepository = donationTrackerSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DonationTrackerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DonationTrackerDTO> findByCriteria(DonationTrackerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DonationTracker> specification = createSpecification(criteria);
        return donationTrackerRepository.findAll(specification, page).map(donationTrackerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DonationTrackerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DonationTracker> specification = createSpecification(criteria);
        return donationTrackerRepository.count(specification);
    }

    /**
     * Function to convert {@link DonationTrackerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DonationTracker> createSpecification(DonationTrackerCriteria criteria) {
        Specification<DonationTracker> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DonationTracker_.id),
                buildStringSpecification(criteria.getBranchCode(), DonationTracker_.branchCode),
                buildStringSpecification(criteria.getDonationIdCode(), DonationTracker_.donationIdCode),
                buildRangeSpecification(criteria.getDate(), DonationTracker_.date),
                buildStringSpecification(criteria.getDonorNameOrOrg(), DonationTracker_.donorNameOrOrg),
                buildStringSpecification(criteria.getContactDetails(), DonationTracker_.contactDetails),
                buildRangeSpecification(criteria.getAmount(), DonationTracker_.amount),
                buildStringSpecification(criteria.getPurpose(), DonationTracker_.purpose),
                buildSpecification(criteria.getReceivedViaMode(), DonationTracker_.receivedViaMode),
                buildStringSpecification(criteria.getNotes(), DonationTracker_.notes),
                buildStringSpecification(criteria.getCreatedBy(), DonationTracker_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), DonationTracker_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), DonationTracker_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), DonationTracker_.lastModifiedDate)
            );
        }
        return specification;
    }
}
