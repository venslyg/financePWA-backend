package com.gvsolutions.service;

import com.gvsolutions.domain.AssetSubCategory;
import com.gvsolutions.repository.AssetSubCategoryRepository;
import com.gvsolutions.repository.search.AssetSubCategorySearchRepository;
import com.gvsolutions.service.dto.AssetSubCategoryDTO;
import com.gvsolutions.service.mapper.AssetSubCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.AssetSubCategory}.
 */
@Service
@Transactional
public class AssetSubCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AssetSubCategoryService.class);

    private final AssetSubCategoryRepository assetSubCategoryRepository;

    private final AssetSubCategoryMapper assetSubCategoryMapper;

    private final AssetSubCategorySearchRepository assetSubCategorySearchRepository;

    public AssetSubCategoryService(
        AssetSubCategoryRepository assetSubCategoryRepository,
        AssetSubCategoryMapper assetSubCategoryMapper,
        AssetSubCategorySearchRepository assetSubCategorySearchRepository
    ) {
        this.assetSubCategoryRepository = assetSubCategoryRepository;
        this.assetSubCategoryMapper = assetSubCategoryMapper;
        this.assetSubCategorySearchRepository = assetSubCategorySearchRepository;
    }

    /**
     * Save a assetSubCategory.
     *
     * @param assetSubCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetSubCategoryDTO save(AssetSubCategoryDTO assetSubCategoryDTO) {
        LOG.debug("Request to save AssetSubCategory : {}", assetSubCategoryDTO);
        AssetSubCategory assetSubCategory = assetSubCategoryMapper.toEntity(assetSubCategoryDTO);
        assetSubCategory = assetSubCategoryRepository.save(assetSubCategory);
        assetSubCategorySearchRepository.index(assetSubCategory);
        return assetSubCategoryMapper.toDto(assetSubCategory);
    }

    /**
     * Update a assetSubCategory.
     *
     * @param assetSubCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetSubCategoryDTO update(AssetSubCategoryDTO assetSubCategoryDTO) {
        LOG.debug("Request to update AssetSubCategory : {}", assetSubCategoryDTO);
        AssetSubCategory assetSubCategory = assetSubCategoryMapper.toEntity(assetSubCategoryDTO);
        assetSubCategory.setIsPersisted();
        assetSubCategory = assetSubCategoryRepository.save(assetSubCategory);
        assetSubCategorySearchRepository.index(assetSubCategory);
        return assetSubCategoryMapper.toDto(assetSubCategory);
    }

    /**
     * Partially update a assetSubCategory.
     *
     * @param assetSubCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssetSubCategoryDTO> partialUpdate(AssetSubCategoryDTO assetSubCategoryDTO) {
        LOG.debug("Request to partially update AssetSubCategory : {}", assetSubCategoryDTO);

        return assetSubCategoryRepository
            .findById(assetSubCategoryDTO.getId())
            .map(existingAssetSubCategory -> {
                assetSubCategoryMapper.partialUpdate(existingAssetSubCategory, assetSubCategoryDTO);

                return existingAssetSubCategory;
            })
            .map(assetSubCategoryRepository::save)
            .map(savedAssetSubCategory -> {
                assetSubCategorySearchRepository.index(savedAssetSubCategory);
                return savedAssetSubCategory;
            })
            .map(assetSubCategoryMapper::toDto);
    }

    /**
     * Get all the assetSubCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AssetSubCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetSubCategoryRepository.findAllWithEagerRelationships(pageable).map(assetSubCategoryMapper::toDto);
    }

    /**
     * Get one assetSubCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssetSubCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get AssetSubCategory : {}", id);
        return assetSubCategoryRepository.findOneWithEagerRelationships(id).map(assetSubCategoryMapper::toDto);
    }

    /**
     * Delete the assetSubCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AssetSubCategory : {}", id);
        assetSubCategoryRepository.deleteById(id);
        assetSubCategorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the assetSubCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetSubCategoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AssetSubCategories for query {}", query);
        return assetSubCategorySearchRepository.search(query, pageable).map(assetSubCategoryMapper::toDto);
    }
}
