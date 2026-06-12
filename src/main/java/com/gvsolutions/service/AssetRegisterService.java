package com.gvsolutions.service;

import com.gvsolutions.domain.AssetRegister;
import com.gvsolutions.repository.AssetRegisterRepository;
import com.gvsolutions.repository.search.AssetRegisterSearchRepository;
import com.gvsolutions.service.dto.AssetRegisterDTO;
import com.gvsolutions.service.mapper.AssetRegisterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.AssetRegister}.
 */
@Service
@Transactional
public class AssetRegisterService {

    private static final Logger LOG = LoggerFactory.getLogger(AssetRegisterService.class);

    private final AssetRegisterRepository assetRegisterRepository;

    private final AssetRegisterMapper assetRegisterMapper;

    private final AssetRegisterSearchRepository assetRegisterSearchRepository;

    public AssetRegisterService(
        AssetRegisterRepository assetRegisterRepository,
        AssetRegisterMapper assetRegisterMapper,
        AssetRegisterSearchRepository assetRegisterSearchRepository
    ) {
        this.assetRegisterRepository = assetRegisterRepository;
        this.assetRegisterMapper = assetRegisterMapper;
        this.assetRegisterSearchRepository = assetRegisterSearchRepository;
    }

    /**
     * Save a assetRegister.
     *
     * @param assetRegisterDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetRegisterDTO save(AssetRegisterDTO assetRegisterDTO) {
        LOG.debug("Request to save AssetRegister : {}", assetRegisterDTO);
        AssetRegister assetRegister = assetRegisterMapper.toEntity(assetRegisterDTO);
        assetRegister = assetRegisterRepository.save(assetRegister);
        assetRegisterSearchRepository.index(assetRegister);
        return assetRegisterMapper.toDto(assetRegister);
    }

    /**
     * Update a assetRegister.
     *
     * @param assetRegisterDTO the entity to save.
     * @return the persisted entity.
     */
    public AssetRegisterDTO update(AssetRegisterDTO assetRegisterDTO) {
        LOG.debug("Request to update AssetRegister : {}", assetRegisterDTO);
        AssetRegister assetRegister = assetRegisterMapper.toEntity(assetRegisterDTO);
        assetRegister.setIsPersisted();
        assetRegister = assetRegisterRepository.save(assetRegister);
        assetRegisterSearchRepository.index(assetRegister);
        return assetRegisterMapper.toDto(assetRegister);
    }

    /**
     * Partially update a assetRegister.
     *
     * @param assetRegisterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssetRegisterDTO> partialUpdate(AssetRegisterDTO assetRegisterDTO) {
        LOG.debug("Request to partially update AssetRegister : {}", assetRegisterDTO);

        return assetRegisterRepository
            .findById(assetRegisterDTO.getId())
            .map(existingAssetRegister -> {
                assetRegisterMapper.partialUpdate(existingAssetRegister, assetRegisterDTO);

                return existingAssetRegister;
            })
            .map(assetRegisterRepository::save)
            .map(savedAssetRegister -> {
                assetRegisterSearchRepository.index(savedAssetRegister);
                return savedAssetRegister;
            })
            .map(assetRegisterMapper::toDto);
    }

    /**
     * Get one assetRegister by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssetRegisterDTO> findOne(Long id) {
        LOG.debug("Request to get AssetRegister : {}", id);
        return assetRegisterRepository.findById(id).map(assetRegisterMapper::toDto);
    }

    /**
     * Delete the assetRegister by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AssetRegister : {}", id);
        assetRegisterRepository.deleteById(id);
        assetRegisterSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the assetRegister corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetRegisterDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AssetRegisters for query {}", query);
        return assetRegisterSearchRepository.search(query, pageable).map(assetRegisterMapper::toDto);
    }
}
