package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.InventoryItemAsserts.*;
import static com.gvsolutions.web.rest.TestUtil.createUpdateProxyForBean;
import static com.gvsolutions.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gvsolutions.IntegrationTest;
import com.gvsolutions.domain.InventoryItem;
import com.gvsolutions.repository.InventoryItemRepository;
import com.gvsolutions.repository.search.InventoryItemSearchRepository;
import com.gvsolutions.service.dto.InventoryItemDTO;
import com.gvsolutions.service.mapper.InventoryItemMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InventoryItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryItemResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVENTORY_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_RUNNING_STOCK_COUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_RUNNING_STOCK_COUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_RUNNING_STOCK_COUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/inventory-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/inventory-items/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryItemMapper inventoryItemMapper;

    @Autowired
    private InventoryItemSearchRepository inventoryItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryItemMockMvc;

    private InventoryItem inventoryItem;

    private InventoryItem insertedInventoryItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItem createEntity() {
        return new InventoryItem()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .inventoryItemCode(DEFAULT_INVENTORY_ITEM_CODE)
            .itemName(DEFAULT_ITEM_NAME)
            .category(DEFAULT_CATEGORY)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .runningStockCount(DEFAULT_RUNNING_STOCK_COUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItem createUpdatedEntity() {
        return new InventoryItem()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .category(UPDATED_CATEGORY)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .runningStockCount(UPDATED_RUNNING_STOCK_COUNT);
    }

    @BeforeEach
    void initTest() {
        inventoryItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInventoryItem != null) {
            inventoryItemRepository.delete(insertedInventoryItem);
            inventoryItemSearchRepository.delete(insertedInventoryItem);
            insertedInventoryItem = null;
        }
    }

    @Test
    @Transactional
    void createInventoryItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);
        var returnedInventoryItemDTO = om.readValue(
            restInventoryItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryItemDTO.class
        );

        // Validate the InventoryItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInventoryItem = inventoryItemMapper.toEntity(returnedInventoryItemDTO);
        assertInventoryItemUpdatableFieldsEquals(returnedInventoryItem, getPersistedInventoryItem(returnedInventoryItem));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedInventoryItem = returnedInventoryItem;
    }

    @Test
    @Transactional
    void createInventoryItemWithExistingId() throws Exception {
        // Create the InventoryItem with an existing ID
        inventoryItem.setId(1L);
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllInventoryItems() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].inventoryItemCode").value(hasItem(DEFAULT_INVENTORY_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].runningStockCount").value(hasItem(sameNumber(DEFAULT_RUNNING_STOCK_COUNT))));
    }

    @Test
    @Transactional
    void getInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get the inventoryItem
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryItem.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.inventoryItemCode").value(DEFAULT_INVENTORY_ITEM_CODE))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.runningStockCount").value(sameNumber(DEFAULT_RUNNING_STOCK_COUNT)));
    }

    @Test
    @Transactional
    void getInventoryItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        Long id = inventoryItem.getId();

        defaultInventoryItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchCode equals to
        defaultInventoryItemFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchCode in
        defaultInventoryItemFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchCode is not null
        defaultInventoryItemFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchCode contains
        defaultInventoryItemFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchCode does not contain
        defaultInventoryItemFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchId equals to
        defaultInventoryItemFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchId in
        defaultInventoryItemFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchId is not null
        defaultInventoryItemFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchId contains
        defaultInventoryItemFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where branchId does not contain
        defaultInventoryItemFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByInventoryItemCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where inventoryItemCode equals to
        defaultInventoryItemFiltering(
            "inventoryItemCode.equals=" + DEFAULT_INVENTORY_ITEM_CODE,
            "inventoryItemCode.equals=" + UPDATED_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByInventoryItemCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where inventoryItemCode in
        defaultInventoryItemFiltering(
            "inventoryItemCode.in=" + DEFAULT_INVENTORY_ITEM_CODE + "," + UPDATED_INVENTORY_ITEM_CODE,
            "inventoryItemCode.in=" + UPDATED_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByInventoryItemCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where inventoryItemCode is not null
        defaultInventoryItemFiltering("inventoryItemCode.specified=true", "inventoryItemCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByInventoryItemCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where inventoryItemCode contains
        defaultInventoryItemFiltering(
            "inventoryItemCode.contains=" + DEFAULT_INVENTORY_ITEM_CODE,
            "inventoryItemCode.contains=" + UPDATED_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByInventoryItemCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where inventoryItemCode does not contain
        defaultInventoryItemFiltering(
            "inventoryItemCode.doesNotContain=" + UPDATED_INVENTORY_ITEM_CODE,
            "inventoryItemCode.doesNotContain=" + DEFAULT_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where itemName equals to
        defaultInventoryItemFiltering("itemName.equals=" + DEFAULT_ITEM_NAME, "itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where itemName in
        defaultInventoryItemFiltering("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME, "itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where itemName is not null
        defaultInventoryItemFiltering("itemName.specified=true", "itemName.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByItemNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where itemName contains
        defaultInventoryItemFiltering("itemName.contains=" + DEFAULT_ITEM_NAME, "itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where itemName does not contain
        defaultInventoryItemFiltering("itemName.doesNotContain=" + UPDATED_ITEM_NAME, "itemName.doesNotContain=" + DEFAULT_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where category equals to
        defaultInventoryItemFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where category in
        defaultInventoryItemFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where category is not null
        defaultInventoryItemFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where category contains
        defaultInventoryItemFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where category does not contain
        defaultInventoryItemFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity equals to
        defaultInventoryItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity in
        defaultInventoryItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is not null
        defaultInventoryItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is greater than or equal to
        defaultInventoryItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is less than or equal to
        defaultInventoryItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is less than
        defaultInventoryItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is greater than
        defaultInventoryItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitPrice equals to
        defaultInventoryItemFiltering("unitPrice.equals=" + DEFAULT_UNIT_PRICE, "unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitPrice in
        defaultInventoryItemFiltering(
            "unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE,
            "unitPrice.in=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitPrice is not null
        defaultInventoryItemFiltering("unitPrice.specified=true", "unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitPrice is greater than or equal to
        defaultInventoryItemFiltering(
            "unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitPrice is less than or equal to
        defaultInventoryItemFiltering("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE, "unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitPrice is less than
        defaultInventoryItemFiltering("unitPrice.lessThan=" + UPDATED_UNIT_PRICE, "unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where unitPrice is greater than
        defaultInventoryItemFiltering("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE, "unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRunningStockCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where runningStockCount equals to
        defaultInventoryItemFiltering(
            "runningStockCount.equals=" + DEFAULT_RUNNING_STOCK_COUNT,
            "runningStockCount.equals=" + UPDATED_RUNNING_STOCK_COUNT
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRunningStockCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where runningStockCount in
        defaultInventoryItemFiltering(
            "runningStockCount.in=" + DEFAULT_RUNNING_STOCK_COUNT + "," + UPDATED_RUNNING_STOCK_COUNT,
            "runningStockCount.in=" + UPDATED_RUNNING_STOCK_COUNT
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRunningStockCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where runningStockCount is not null
        defaultInventoryItemFiltering("runningStockCount.specified=true", "runningStockCount.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRunningStockCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where runningStockCount is greater than or equal to
        defaultInventoryItemFiltering(
            "runningStockCount.greaterThanOrEqual=" + DEFAULT_RUNNING_STOCK_COUNT,
            "runningStockCount.greaterThanOrEqual=" + UPDATED_RUNNING_STOCK_COUNT
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRunningStockCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where runningStockCount is less than or equal to
        defaultInventoryItemFiltering(
            "runningStockCount.lessThanOrEqual=" + DEFAULT_RUNNING_STOCK_COUNT,
            "runningStockCount.lessThanOrEqual=" + SMALLER_RUNNING_STOCK_COUNT
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRunningStockCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where runningStockCount is less than
        defaultInventoryItemFiltering(
            "runningStockCount.lessThan=" + UPDATED_RUNNING_STOCK_COUNT,
            "runningStockCount.lessThan=" + DEFAULT_RUNNING_STOCK_COUNT
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByRunningStockCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where runningStockCount is greater than
        defaultInventoryItemFiltering(
            "runningStockCount.greaterThan=" + SMALLER_RUNNING_STOCK_COUNT,
            "runningStockCount.greaterThan=" + DEFAULT_RUNNING_STOCK_COUNT
        );
    }

    private void defaultInventoryItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryItemShouldBeFound(shouldBeFound);
        defaultInventoryItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryItemShouldBeFound(String filter) throws Exception {
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].inventoryItemCode").value(hasItem(DEFAULT_INVENTORY_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].runningStockCount").value(hasItem(sameNumber(DEFAULT_RUNNING_STOCK_COUNT))));

        // Check, that the count call also returns 1
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryItemShouldNotBeFound(String filter) throws Exception {
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventoryItem() throws Exception {
        // Get the inventoryItem
        restInventoryItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItemSearchRepository.save(inventoryItem);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());

        // Update the inventoryItem
        InventoryItem updatedInventoryItem = inventoryItemRepository.findById(inventoryItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventoryItem are not directly saved in db
        em.detach(updatedInventoryItem);
        updatedInventoryItem
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .category(UPDATED_CATEGORY)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .runningStockCount(UPDATED_RUNNING_STOCK_COUNT);
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(updatedInventoryItem);

        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryItemToMatchAllProperties(updatedInventoryItem);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InventoryItem> inventoryItemSearchList = Streamable.of(inventoryItemSearchRepository.findAll()).toList();
                InventoryItem testInventoryItemSearch = inventoryItemSearchList.get(searchDatabaseSizeAfter - 1);

                assertInventoryItemAllPropertiesEquals(testInventoryItemSearch, updatedInventoryItem);
            });
    }

    @Test
    @Transactional
    void putNonExistingInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateInventoryItemWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem using partial update
        InventoryItem partialUpdatedInventoryItem = new InventoryItem();
        partialUpdatedInventoryItem.setId(inventoryItem.getId());

        partialUpdatedInventoryItem
            .branchCode(UPDATED_BRANCH_CODE)
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .unitPrice(UPDATED_UNIT_PRICE)
            .runningStockCount(UPDATED_RUNNING_STOCK_COUNT);

        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItem))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventoryItem, inventoryItem),
            getPersistedInventoryItem(inventoryItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryItemWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem using partial update
        InventoryItem partialUpdatedInventoryItem = new InventoryItem();
        partialUpdatedInventoryItem.setId(inventoryItem.getId());

        partialUpdatedInventoryItem
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .category(UPDATED_CATEGORY)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .runningStockCount(UPDATED_RUNNING_STOCK_COUNT);

        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItem))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemUpdatableFieldsEquals(partialUpdatedInventoryItem, getPersistedInventoryItem(partialUpdatedInventoryItem));
    }

    @Test
    @Transactional
    void patchNonExistingInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);
        inventoryItemRepository.save(inventoryItem);
        inventoryItemSearchRepository.save(inventoryItem);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the inventoryItem
        restInventoryItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inventoryItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);
        inventoryItemSearchRepository.save(inventoryItem);

        // Search the inventoryItem
        restInventoryItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + inventoryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].inventoryItemCode").value(hasItem(DEFAULT_INVENTORY_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].runningStockCount").value(hasItem(sameNumber(DEFAULT_RUNNING_STOCK_COUNT))));
    }

    protected long getRepositoryCount() {
        return inventoryItemRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected InventoryItem getPersistedInventoryItem(InventoryItem inventoryItem) {
        return inventoryItemRepository.findById(inventoryItem.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryItemToMatchAllProperties(InventoryItem expectedInventoryItem) {
        assertInventoryItemAllPropertiesEquals(expectedInventoryItem, getPersistedInventoryItem(expectedInventoryItem));
    }

    protected void assertPersistedInventoryItemToMatchUpdatableProperties(InventoryItem expectedInventoryItem) {
        assertInventoryItemAllUpdatablePropertiesEquals(expectedInventoryItem, getPersistedInventoryItem(expectedInventoryItem));
    }
}
