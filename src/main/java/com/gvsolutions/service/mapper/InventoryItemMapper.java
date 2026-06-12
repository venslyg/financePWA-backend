package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.InventoryItem;
import com.gvsolutions.service.dto.InventoryItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoryItem} and its DTO {@link InventoryItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoryItemMapper extends EntityMapper<InventoryItemDTO, InventoryItem> {}
