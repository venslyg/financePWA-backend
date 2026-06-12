package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.AssetDepreciationHistory;
import com.gvsolutions.repository.AssetDepreciationHistoryRepository;
import com.gvsolutions.repository.search.AssetDepreciationHistorySearchRepository;
import com.gvsolutions.service.criteria.AssetDepreciationHistoryCriteria;
import com.gvsolutions.service.dto.AssetDepreciationHistoryDTO;
import com.gvsolutions.service.mapper.AssetDepreciationHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AssetDepreciationHistory} entities in the database.
 * The main input is a {@link AssetDepreciationHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AssetDepreciationHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetDepreciationHistoryQueryService extends QueryService<AssetDepreciationHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetDepreciationHistoryQueryService.class);

    private final AssetDepreciationHistoryRepository assetDepreciationHistoryRepository;

    private final AssetDepreciationHistoryMapper assetDepreciationHistoryMapper;

    private final AssetDepreciationHistorySearchRepository assetDepreciationHistorySearchRepository;

    public AssetDepreciationHistoryQueryService(
        AssetDepreciationHistoryRepository assetDepreciationHistoryRepository,
        AssetDepreciationHistoryMapper assetDepreciationHistoryMapper,
        AssetDepreciationHistorySearchRepository assetDepreciationHistorySearchRepository
    ) {
        this.assetDepreciationHistoryRepository = assetDepreciationHistoryRepository;
        this.assetDepreciationHistoryMapper = assetDepreciationHistoryMapper;
        this.assetDepreciationHistorySearchRepository = assetDepreciationHistorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AssetDepreciationHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetDepreciationHistoryDTO> findByCriteria(AssetDepreciationHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetDepreciationHistory> specification = createSpecification(criteria);
        return assetDepreciationHistoryRepository.findAll(specification, page).map(assetDepreciationHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetDepreciationHistoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AssetDepreciationHistory> specification = createSpecification(criteria);
        return assetDepreciationHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetDepreciationHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetDepreciationHistory> createSpecification(AssetDepreciationHistoryCriteria criteria) {
        Specification<AssetDepreciationHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AssetDepreciationHistory_.id),
                buildStringSpecification(criteria.getBranchCode(), AssetDepreciationHistory_.branchCode),
                buildStringSpecification(criteria.getBranchId(), AssetDepreciationHistory_.branchId),
                buildStringSpecification(criteria.getAssetRegisterCode(), AssetDepreciationHistory_.assetRegisterCode),
                buildRangeSpecification(criteria.getDepreciationDate(), AssetDepreciationHistory_.depreciationDate),
                buildRangeSpecification(criteria.getDepreciationAmount(), AssetDepreciationHistory_.depreciationAmount),
                buildRangeSpecification(criteria.getValueAfterDepreciation(), AssetDepreciationHistory_.valueAfterDepreciation),
                buildStringSpecification(criteria.getProcessedBy(), AssetDepreciationHistory_.processedBy),
                buildStringSpecification(criteria.getCreatedBy(), AssetDepreciationHistory_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), AssetDepreciationHistory_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), AssetDepreciationHistory_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), AssetDepreciationHistory_.lastModifiedDate)
            );
        }
        return specification;
    }
}
