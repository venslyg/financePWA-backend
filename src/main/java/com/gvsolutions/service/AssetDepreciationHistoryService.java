package com.gvsolutions.service;

import com.gvsolutions.domain.AssetDepreciationHistory;
import com.gvsolutions.repository.AssetDepreciationHistoryRepository;
import com.gvsolutions.repository.search.AssetDepreciationHistorySearchRepository;
import com.gvsolutions.service.dto.AssetDepreciationHistoryDTO;
import com.gvsolutions.service.mapper.AssetDepreciationHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.AssetDepreciationHistory}.
 */
@Service
@Transactional
public class AssetDepreciationHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AssetDepreciationHistoryService.class);

    private final AssetDepreciationHistoryRepository assetDepreciationHistoryRepository;

    private final AssetDepreciationHistoryMapper assetDepreciationHistoryMapper;

    private final AssetDepreciationHistorySearchRepository assetDepreciationHistorySearchRepository;

    public AssetDepreciationHistoryService(
        AssetDepreciationHistoryRepository assetDepreciationHistoryRepository,
        AssetDepreciationHistoryMapper assetDepreciationHistoryMapper,
        AssetDepreciationHistorySearchRepository assetDepreciationHistorySearchRepository
    ) {
        this.assetDepreciationHistoryRepository = assetDepreciationHistoryRepository;
        this.assetDepreciationHistoryMapper = assetDepreciationHistoryMapper;
        this.assetDepreciationHistorySearchRepository = assetDepreciationHistorySearchRepository;
    }

    /**
     * Save a assetDepreciationHistory.
     *
     * @param assetDepreciationHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetDepreciationHistoryDTO save(AssetDepreciationHistoryDTO assetDepreciationHistoryDTO) {
        LOG.debug("Request to save AssetDepreciationHistory : {}", assetDepreciationHistoryDTO);
        AssetDepreciationHistory assetDepreciationHistory = assetDepreciationHistoryMapper.toEntity(assetDepreciationHistoryDTO);
        assetDepreciationHistory = assetDepreciationHistoryRepository.save(assetDepreciationHistory);
        assetDepreciationHistorySearchRepository.index(assetDepreciationHistory);
        return assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);
    }

    /**
     * Update a assetDepreciationHistory.
     *
     * @param assetDepreciationHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetDepreciationHistoryDTO update(AssetDepreciationHistoryDTO assetDepreciationHistoryDTO) {
        LOG.debug("Request to update AssetDepreciationHistory : {}", assetDepreciationHistoryDTO);
        AssetDepreciationHistory assetDepreciationHistory = assetDepreciationHistoryMapper.toEntity(assetDepreciationHistoryDTO);
        assetDepreciationHistory.setIsPersisted();
        assetDepreciationHistory = assetDepreciationHistoryRepository.save(assetDepreciationHistory);
        assetDepreciationHistorySearchRepository.index(assetDepreciationHistory);
        return assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);
    }

    /**
     * Partially update a assetDepreciationHistory.
     *
     * @param assetDepreciationHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssetDepreciationHistoryDTO> partialUpdate(AssetDepreciationHistoryDTO assetDepreciationHistoryDTO) {
        LOG.debug("Request to partially update AssetDepreciationHistory : {}", assetDepreciationHistoryDTO);

        return assetDepreciationHistoryRepository
            .findById(assetDepreciationHistoryDTO.getId())
            .map(existingAssetDepreciationHistory -> {
                assetDepreciationHistoryMapper.partialUpdate(existingAssetDepreciationHistory, assetDepreciationHistoryDTO);

                return existingAssetDepreciationHistory;
            })
            .map(assetDepreciationHistoryRepository::save)
            .map(savedAssetDepreciationHistory -> {
                assetDepreciationHistorySearchRepository.index(savedAssetDepreciationHistory);
                return savedAssetDepreciationHistory;
            })
            .map(assetDepreciationHistoryMapper::toDto);
    }

    /**
     * Get one assetDepreciationHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssetDepreciationHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get AssetDepreciationHistory : {}", id);
        return assetDepreciationHistoryRepository.findById(id).map(assetDepreciationHistoryMapper::toDto);
    }

    /**
     * Delete the assetDepreciationHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AssetDepreciationHistory : {}", id);
        assetDepreciationHistoryRepository.deleteById(id);
        assetDepreciationHistorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the assetDepreciationHistory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetDepreciationHistoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AssetDepreciationHistories for query {}", query);
        return assetDepreciationHistorySearchRepository.search(query, pageable).map(assetDepreciationHistoryMapper::toDto);
    }
}
