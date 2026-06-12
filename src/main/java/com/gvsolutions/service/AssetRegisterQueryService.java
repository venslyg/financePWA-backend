package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.AssetRegister;
import com.gvsolutions.repository.AssetRegisterRepository;
import com.gvsolutions.repository.search.AssetRegisterSearchRepository;
import com.gvsolutions.service.criteria.AssetRegisterCriteria;
import com.gvsolutions.service.dto.AssetRegisterDTO;
import com.gvsolutions.service.mapper.AssetRegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AssetRegister} entities in the database.
 * The main input is a {@link AssetRegisterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AssetRegisterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetRegisterQueryService extends QueryService<AssetRegister> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetRegisterQueryService.class);

    private final AssetRegisterRepository assetRegisterRepository;

    private final AssetRegisterMapper assetRegisterMapper;

    private final AssetRegisterSearchRepository assetRegisterSearchRepository;

    public AssetRegisterQueryService(
        AssetRegisterRepository assetRegisterRepository,
        AssetRegisterMapper assetRegisterMapper,
        AssetRegisterSearchRepository assetRegisterSearchRepository
    ) {
        this.assetRegisterRepository = assetRegisterRepository;
        this.assetRegisterMapper = assetRegisterMapper;
        this.assetRegisterSearchRepository = assetRegisterSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AssetRegisterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetRegisterDTO> findByCriteria(AssetRegisterCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetRegister> specification = createSpecification(criteria);
        return assetRegisterRepository.findAll(specification, page).map(assetRegisterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetRegisterCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AssetRegister> specification = createSpecification(criteria);
        return assetRegisterRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetRegisterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetRegister> createSpecification(AssetRegisterCriteria criteria) {
        Specification<AssetRegister> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AssetRegister_.id),
                buildStringSpecification(criteria.getBranchCode(), AssetRegister_.branchCode),
                buildStringSpecification(criteria.getBranchId(), AssetRegister_.branchId),
                buildStringSpecification(criteria.getAssetRegisterCode(), AssetRegister_.assetRegisterCode),
                buildStringSpecification(criteria.getAssetCategoryCode(), AssetRegister_.assetCategoryCode),
                buildStringSpecification(criteria.getAssetSubCategoryCode(), AssetRegister_.assetSubCategoryCode),
                buildStringSpecification(criteria.getAssetName(), AssetRegister_.assetName),
                buildStringSpecification(criteria.getCategory(), AssetRegister_.category),
                buildRangeSpecification(criteria.getPurchaseDate(), AssetRegister_.purchaseDate),
                buildRangeSpecification(criteria.getPurchaseCost(), AssetRegister_.purchaseCost),
                buildRangeSpecification(criteria.getCurrentValue(), AssetRegister_.currentValue),
                buildRangeSpecification(criteria.getDepreciationRate(), AssetRegister_.depreciationRate),
                buildRangeSpecification(criteria.getAccumulatedDepreciation(), AssetRegister_.accumulatedDepreciation),
                buildStringSpecification(criteria.getCreatedBy(), AssetRegister_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), AssetRegister_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), AssetRegister_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), AssetRegister_.lastModifiedDate)
            );
        }
        return specification;
    }
}
