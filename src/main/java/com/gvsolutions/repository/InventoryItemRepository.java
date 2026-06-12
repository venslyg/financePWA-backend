package com.gvsolutions.repository;

import com.gvsolutions.domain.InventoryItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InventoryItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long>, JpaSpecificationExecutor<InventoryItem> {}
