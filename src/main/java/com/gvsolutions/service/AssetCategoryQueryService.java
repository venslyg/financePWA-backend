package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.AssetCategory;
import com.gvsolutions.repository.AssetCategoryRepository;
import com.gvsolutions.repository.search.AssetCategorySearchRepository;
import com.gvsolutions.service.criteria.AssetCategoryCriteria;
import com.gvsolutions.service.dto.AssetCategoryDTO;
import com.gvsolutions.service.mapper.AssetCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AssetCategory} entities in the database.
 * The main input is a {@link AssetCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AssetCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetCategoryQueryService extends QueryService<AssetCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(AssetCategoryQueryService.class);

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryMapper assetCategoryMapper;

    private final AssetCategorySearchRepository assetCategorySearchRepository;

    public AssetCategoryQueryService(
        AssetCategoryRepository assetCategoryRepository,
        AssetCategoryMapper assetCategoryMapper,
        AssetCategorySearchRepository assetCategorySearchRepository
    ) {
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryMapper = assetCategoryMapper;
        this.assetCategorySearchRepository = assetCategorySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AssetCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetCategoryDTO> findByCriteria(AssetCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryRepository.findAll(specification, page).map(assetCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetCategory> createSpecification(AssetCategoryCriteria criteria) {
        Specification<AssetCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AssetCategory_.id),
                buildStringSpecification(criteria.getAssetCategoryCode(), AssetCategory_.assetCategoryCode),
                buildStringSpecification(criteria.getAssetCategoryName(), AssetCategory_.assetCategoryName),
                buildStringSpecification(criteria.getDescription(), AssetCategory_.description),
                buildStringSpecification(criteria.getCreatedBy(), AssetCategory_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), AssetCategory_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), AssetCategory_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), AssetCategory_.lastModifiedDate)
            );
        }
        return specification;
    }
}
