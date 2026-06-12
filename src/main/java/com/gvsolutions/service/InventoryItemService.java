package com.gvsolutions.service;

import com.gvsolutions.domain.InventoryItem;
import com.gvsolutions.repository.InventoryItemRepository;
import com.gvsolutions.repository.search.InventoryItemSearchRepository;
import com.gvsolutions.service.dto.InventoryItemDTO;
import com.gvsolutions.service.mapper.InventoryItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gvsolutions.domain.InventoryItem}.
 */
@Service
@Transactional
public class InventoryItemService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemService.class);

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryItemMapper inventoryItemMapper;

    private final InventoryItemSearchRepository inventoryItemSearchRepository;

    public InventoryItemService(
        InventoryItemRepository inventoryItemRepository,
        InventoryItemMapper inventoryItemMapper,
        InventoryItemSearchRepository inventoryItemSearchRepository
    ) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryItemMapper = inventoryItemMapper;
        this.inventoryItemSearchRepository = inventoryItemSearchRepository;
    }

    /**
     * Save a inventoryItem.
     *
     * @param inventoryItemDTO the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemDTO save(InventoryItemDTO inventoryItemDTO) {
        LOG.debug("Request to save InventoryItem : {}", inventoryItemDTO);
        InventoryItem inventoryItem = inventoryItemMapper.toEntity(inventoryItemDTO);
        inventoryItem = inventoryItemRepository.save(inventoryItem);
        inventoryItemSearchRepository.index(inventoryItem);
        return inventoryItemMapper.toDto(inventoryItem);
    }

    /**
     * Update a inventoryItem.
     *
     * @param inventoryItemDTO the entity to save.
     * @return the persisted entity.
     */
    public InventoryItemDTO update(InventoryItemDTO inventoryItemDTO) {
        LOG.debug("Request to update InventoryItem : {}", inventoryItemDTO);
        InventoryItem inventoryItem = inventoryItemMapper.toEntity(inventoryItemDTO);
        inventoryItem.setIsPersisted();
        inventoryItem = inventoryItemRepository.save(inventoryItem);
        inventoryItemSearchRepository.index(inventoryItem);
        return inventoryItemMapper.toDto(inventoryItem);
    }

    /**
     * Partially update a inventoryItem.
     *
     * @param inventoryItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventoryItemDTO> partialUpdate(InventoryItemDTO inventoryItemDTO) {
        LOG.debug("Request to partially update InventoryItem : {}", inventoryItemDTO);

        return inventoryItemRepository
            .findById(inventoryItemDTO.getId())
            .map(existingInventoryItem -> {
                inventoryItemMapper.partialUpdate(existingInventoryItem, inventoryItemDTO);

                return existingInventoryItem;
            })
            .map(inventoryItemRepository::save)
            .map(savedInventoryItem -> {
                inventoryItemSearchRepository.index(savedInventoryItem);
                return savedInventoryItem;
            })
            .map(inventoryItemMapper::toDto);
    }

    /**
     * Get one inventoryItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventoryItemDTO> findOne(Long id) {
        LOG.debug("Request to get InventoryItem : {}", id);
        return inventoryItemRepository.findById(id).map(inventoryItemMapper::toDto);
    }

    /**
     * Delete the inventoryItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InventoryItem : {}", id);
        inventoryItemRepository.deleteById(id);
        inventoryItemSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the inventoryItem corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryItemDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of InventoryItems for query {}", query);
        return inventoryItemSearchRepository.search(query, pageable).map(inventoryItemMapper::toDto);
    }
}
