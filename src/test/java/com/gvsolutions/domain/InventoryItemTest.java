package com.gvsolutions.domain;

import static com.gvsolutions.domain.InventoryItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItem.class);
        InventoryItem inventoryItem1 = getInventoryItemSample1();
        InventoryItem inventoryItem2 = new InventoryItem();
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);

        inventoryItem2.setId(inventoryItem1.getId());
        assertThat(inventoryItem1).isEqualTo(inventoryItem2);

        inventoryItem2 = getInventoryItemSample2();
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);
    }
}
