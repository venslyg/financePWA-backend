package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.AssetSubCategory;
import com.gvsolutions.repository.AssetSubCategoryRepository;
import com.gvsolutions.repository.search.AssetSubCategorySearchRepository;
import com.gvsolutions.service.criteria.AssetSubCategoryCriteria;
import com.gvsolutions.service.dto.AssetSubCategoryDTO;
import com.gvsolutions.service.mapper.AssetSubCategoryMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AssetSubCategory} entities in the database.
 * The main input is a {@link AssetSubCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AssetSubCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetSubCategoryQueryService extends QueryService<AssetSubCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetSubCategoryQueryService.class);

    private final AssetSubCategoryRepository assetSubCategoryRepository;

    private final AssetSubCategoryMapper assetSubCategoryMapper;

    private final AssetSubCategorySearchRepository assetSubCategorySearchRepository;

    public AssetSubCategoryQueryService(
        AssetSubCategoryRepository assetSubCategoryRepository,
        AssetSubCategoryMapper assetSubCategoryMapper,
        AssetSubCategorySearchRepository assetSubCategorySearchRepository
    ) {
        this.assetSubCategoryRepository = assetSubCategoryRepository;
        this.assetSubCategoryMapper = assetSubCategoryMapper;
        this.assetSubCategorySearchRepository = assetSubCategorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AssetSubCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetSubCategoryDTO> findByCriteria(AssetSubCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetSubCategory> specification = createSpecification(criteria);
        return assetSubCategoryRepository.findAll(specification, page).map(assetSubCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetSubCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AssetSubCategory> specification = createSpecification(criteria);
        return assetSubCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetSubCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetSubCategory> createSpecification(AssetSubCategoryCriteria criteria) {
        Specification<AssetSubCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AssetSubCategory_.id),
                buildStringSpecification(criteria.getBranchCode(), AssetSubCategory_.branchCode),
                buildStringSpecification(criteria.getBranchId(), AssetSubCategory_.branchId),
                buildStringSpecification(criteria.getAssetCategoryCode(), AssetSubCategory_.assetCategoryCode),
                buildStringSpecification(criteria.getAssetSubCategoryCode(), AssetSubCategory_.assetSubCategoryCode),
                buildStringSpecification(criteria.getAssetSubCategoryName(), AssetSubCategory_.assetSubCategoryName),
                buildSpecification(criteria.getIsActive(), AssetSubCategory_.isActive),
                buildStringSpecification(criteria.getCreatedBy(), AssetSubCategory_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), AssetSubCategory_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), AssetSubCategory_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), AssetSubCategory_.lastModifiedDate),
                buildSpecification(criteria.getCategoryId(), root ->
                    root.join(AssetSubCategory_.category, JoinType.LEFT).get(AssetCategory_.id)
                )
            );
        }
        return specification;
    }
}
