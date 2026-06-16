package com.gvsolutions.service;

import com.gvsolutions.domain.*; // for static metamodels
import com.gvsolutions.domain.InventoryItem;
import com.gvsolutions.repository.InventoryItemRepository;
import com.gvsolutions.repository.search.InventoryItemSearchRepository;
import com.gvsolutions.service.criteria.InventoryItemCriteria;
import com.gvsolutions.service.dto.InventoryItemDTO;
import com.gvsolutions.service.mapper.InventoryItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InventoryItem} entities in the database.
 * The main input is a {@link InventoryItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InventoryItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryItemQueryService extends QueryService<InventoryItem> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemQueryService.class);

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryItemMapper inventoryItemMapper;

    private final InventoryItemSearchRepository inventoryItemSearchRepository;

    public InventoryItemQueryService(
        InventoryItemRepository inventoryItemRepository,
        InventoryItemMapper inventoryItemMapper,
        InventoryItemSearchRepository inventoryItemSearchRepository
    ) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryItemMapper = inventoryItemMapper;
        this.inventoryItemSearchRepository = inventoryItemSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link InventoryItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryItemDTO> findByCriteria(InventoryItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InventoryItem> specification = createSpecification(criteria);
        return inventoryItemRepository.findAll(specification, page).map(inventoryItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InventoryItem> specification = createSpecification(criteria);
        return inventoryItemRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InventoryItem> createSpecification(InventoryItemCriteria criteria) {
        Specification<InventoryItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InventoryItem_.id),
                buildStringSpecification(criteria.getBranchCode(), InventoryItem_.branchCode),
                buildStringSpecification(criteria.getBranchId(), InventoryItem_.branchId),
                buildStringSpecification(criteria.getInventoryItemCode(), InventoryItem_.inventoryItemCode),
                buildStringSpecification(criteria.getItemName(), InventoryItem_.itemName),
                buildStringSpecification(criteria.getCategory(), InventoryItem_.category),
                buildRangeSpecification(criteria.getQuantity(), InventoryItem_.quantity),
                buildRangeSpecification(criteria.getUnitPrice(), InventoryItem_.unitPrice),
                buildRangeSpecification(criteria.getRunningStockCount(), InventoryItem_.runningStockCount),
                buildSpecification(criteria.getIsActive(), InventoryItem_.isActive),
                buildStringSpecification(criteria.getCreatedBy(), InventoryItem_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), InventoryItem_.createdDate),
                buildStringSpecification(criteria.getLastModifiedBy(), InventoryItem_.lastModifiedBy),
                buildRangeSpecification(criteria.getLastModifiedDate(), InventoryItem_.lastModifiedDate)
            );
        }
        return specification;
    }
}
