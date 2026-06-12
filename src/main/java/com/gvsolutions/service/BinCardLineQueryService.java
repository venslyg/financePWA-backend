package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.BinCardLine;
import com.gvsolutions.repository.BinCardLineRepository;
import com.gvsolutions.repository.search.BinCardLineSearchRepository;
import com.gvsolutions.service.criteria.BinCardLineCriteria;
import com.gvsolutions.service.dto.BinCardLineDTO;
import com.gvsolutions.service.mapper.BinCardLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BinCardLine} entities in the database.
 * The main input is a {@link BinCardLineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BinCardLineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BinCardLineQueryService extends QueryService<BinCardLine> {

    private static final Logger LOG = LoggerFactory.getLogger(BinCardLineQueryService.class);

    private final BinCardLineRepository binCardLineRepository;

    private final BinCardLineMapper binCardLineMapper;

    private final BinCardLineSearchRepository binCardLineSearchRepository;

    public BinCardLineQueryService(
        BinCardLineRepository binCardLineRepository,
        BinCardLineMapper binCardLineMapper,
        BinCardLineSearchRepository binCardLineSearchRepository
    ) {
        this.binCardLineRepository = binCardLineRepository;
        this.binCardLineMapper = binCardLineMapper;
        this.binCardLineSearchRepository = binCardLineSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link BinCardLineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BinCardLineDTO> findByCriteria(BinCardLineCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BinCardLine> specification = createSpecification(criteria);
        return binCardLineRepository.findAll(specification, page).map(binCardLineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BinCardLineCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<BinCardLine> specification = createSpecification(criteria);
        return binCardLineRepository.count(specification);
    }

    /**
     * Function to convert {@link BinCardLineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BinCardLine> createSpecification(BinCardLineCriteria criteria) {
        Specification<BinCardLine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), BinCardLine_.id),
                buildStringSpecification(criteria.getBranchCode(), BinCardLine_.branchCode),
                buildStringSpecification(criteria.getBranchId(), BinCardLine_.branchId),
                buildStringSpecification(criteria.getInventoryItemCode(), BinCardLine_.inventoryItemCode),
                buildRangeSpecification(criteria.getDate(), BinCardLine_.date),
                buildStringSpecification(criteria.getReferenceNo(), BinCardLine_.referenceNo),
                buildStringSpecification(criteria.getDescription(), BinCardLine_.description),
                buildRangeSpecification(criteria.getQuantityIn(), BinCardLine_.quantityIn),
                buildRangeSpecification(criteria.getQuantityOut(), BinCardLine_.quantityOut),
                buildRangeSpecification(criteria.getRunningBalance(), BinCardLine_.runningBalance),
                buildStringSpecification(criteria.getCreatedBy(), BinCardLine_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), BinCardLine_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), BinCardLine_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), BinCardLine_.lastModifiedDate)
            );
        }
        return specification;
    }
}
