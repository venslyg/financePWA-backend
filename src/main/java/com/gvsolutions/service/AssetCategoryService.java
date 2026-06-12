package com.gvsolutions.service;

import com.gvsolutions.domain.AssetCategory;
import com.gvsolutions.repository.AssetCategoryRepository;
import com.gvsolutions.repository.search.AssetCategorySearchRepository;
import com.gvsolutions.service.dto.AssetCategoryDTO;
import com.gvsolutions.service.mapper.AssetCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.AssetCategory}.
 */
@Service
@Transactional
public class AssetCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AssetCategoryService.class);

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryMapper assetCategoryMapper;

    private final AssetCategorySearchRepository assetCategorySearchRepository;

    public AssetCategoryService(
        AssetCategoryRepository assetCategoryRepository,
        AssetCategoryMapper assetCategoryMapper,
        AssetCategorySearchRepository assetCategorySearchRepository
    ) {
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryMapper = assetCategoryMapper;
        this.assetCategorySearchRepository = assetCategorySearchRepository;
    }

    /**
     * Save a assetCategory.
     *
     * @param assetCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetCategoryDTO save(AssetCategoryDTO assetCategoryDTO) {
        LOG.debug("Request to save AssetCategory : {}", assetCategoryDTO);
        AssetCategory assetCategory = assetCategoryMapper.toEntity(assetCategoryDTO);
        assetCategory = assetCategoryRepository.save(assetCategory);
        assetCategorySearchRepository.index(assetCategory);
        return assetCategoryMapper.toDto(assetCategory);
    }

    /**
     * Update a assetCategory.
     *
     * @param assetCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetCategoryDTO update(AssetCategoryDTO assetCategoryDTO) {
        LOG.debug("Request to update AssetCategory : {}", assetCategoryDTO);
        AssetCategory assetCategory = assetCategoryMapper.toEntity(assetCategoryDTO);
        assetCategory.setIsPersisted();
        assetCategory = assetCategoryRepository.save(assetCategory);
        assetCategorySearchRepository.index(assetCategory);
        return assetCategoryMapper.toDto(assetCategory);
    }

    /**
     * Partially update a assetCategory.
     *
     * @param assetCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssetCategoryDTO> partialUpdate(AssetCategoryDTO assetCategoryDTO) {
        LOG.debug("Request to partially update AssetCategory : {}", assetCategoryDTO);

        return assetCategoryRepository
            .findById(assetCategoryDTO.getId())
            .map(existingAssetCategory -> {
                assetCategoryMapper.partialUpdate(existingAssetCategory, assetCategoryDTO);

                return existingAssetCategory;
            })
            .map(assetCategoryRepository::save)
            .map(savedAssetCategory -> {
                assetCategorySearchRepository.index(savedAssetCategory);
                return savedAssetCategory;
            })
            .map(assetCategoryMapper::toDto);
    }

    /**
     * Get one assetCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssetCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get AssetCategory : {}", id);
        return assetCategoryRepository.findById(id).map(assetCategoryMapper::toDto);
    }

    /**
     * Delete the assetCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AssetCategory : {}", id);
        assetCategoryRepository.deleteById(id);
        assetCategorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the assetCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetCategoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AssetCategories for query {}", query);
        return assetCategorySearchRepository.search(query, pageable).map(assetCategoryMapper::toDto);
    }
}
