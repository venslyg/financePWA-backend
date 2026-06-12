package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.AssetRegisterAsserts.*;
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
import com.gvsolutions.domain.AssetRegister;
import com.gvsolutions.repository.AssetRegisterRepository;
import com.gvsolutions.repository.search.AssetRegisterSearchRepository;
import com.gvsolutions.service.dto.AssetRegisterDTO;
import com.gvsolutions.service.mapper.AssetRegisterMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AssetRegisterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetRegisterResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_REGISTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_REGISTER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_SUB_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_SUB_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PURCHASE_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_PURCHASE_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_PURCHASE_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CURRENT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CURRENT_VALUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DEPRECIATION_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_RATE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCUMULATED_DEPRECIATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCUMULATED_DEPRECIATION = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCUMULATED_DEPRECIATION = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/asset-registers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/asset-registers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssetRegisterRepository assetRegisterRepository;

    @Autowired
    private AssetRegisterMapper assetRegisterMapper;

    @Autowired
    private AssetRegisterSearchRepository assetRegisterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetRegisterMockMvc;

    private AssetRegister assetRegister;

    private AssetRegister insertedAssetRegister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetRegister createEntity() {
        return new AssetRegister()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .assetRegisterCode(DEFAULT_ASSET_REGISTER_CODE)
            .assetCategoryCode(DEFAULT_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(DEFAULT_ASSET_SUB_CATEGORY_CODE)
            .assetName(DEFAULT_ASSET_NAME)
            .category(DEFAULT_CATEGORY)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .purchaseCost(DEFAULT_PURCHASE_COST)
            .currentValue(DEFAULT_CURRENT_VALUE)
            .depreciationRate(DEFAULT_DEPRECIATION_RATE)
            .accumulatedDepreciation(DEFAULT_ACCUMULATED_DEPRECIATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetRegister createUpdatedEntity() {
        return new AssetRegister()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetRegisterCode(UPDATED_ASSET_REGISTER_CODE)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(UPDATED_ASSET_SUB_CATEGORY_CODE)
            .assetName(UPDATED_ASSET_NAME)
            .category(UPDATED_CATEGORY)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchaseCost(UPDATED_PURCHASE_COST)
            .currentValue(UPDATED_CURRENT_VALUE)
            .depreciationRate(UPDATED_DEPRECIATION_RATE)
            .accumulatedDepreciation(UPDATED_ACCUMULATED_DEPRECIATION);
    }

    @BeforeEach
    void initTest() {
        assetRegister = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAssetRegister != null) {
            assetRegisterRepository.delete(insertedAssetRegister);
            assetRegisterSearchRepository.delete(insertedAssetRegister);
            insertedAssetRegister = null;
        }
    }

    @Test
    @Transactional
    void createAssetRegister() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        // Create the AssetRegister
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);
        var returnedAssetRegisterDTO = om.readValue(
            restAssetRegisterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetRegisterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssetRegisterDTO.class
        );

        // Validate the AssetRegister in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssetRegister = assetRegisterMapper.toEntity(returnedAssetRegisterDTO);
        assertAssetRegisterUpdatableFieldsEquals(returnedAssetRegister, getPersistedAssetRegister(returnedAssetRegister));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAssetRegister = returnedAssetRegister;
    }

    @Test
    @Transactional
    void createAssetRegisterWithExistingId() throws Exception {
        // Create the AssetRegister with an existing ID
        assetRegister.setId(1L);
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetRegisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetRegisterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAssetRegisters() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList
        restAssetRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetRegisterCode").value(hasItem(DEFAULT_ASSET_REGISTER_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryCode").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetName").value(hasItem(DEFAULT_ASSET_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchaseCost").value(hasItem(sameNumber(DEFAULT_PURCHASE_COST))))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(sameNumber(DEFAULT_CURRENT_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationRate").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_RATE))))
            .andExpect(jsonPath("$.[*].accumulatedDepreciation").value(hasItem(sameNumber(DEFAULT_ACCUMULATED_DEPRECIATION))));
    }

    @Test
    @Transactional
    void getAssetRegister() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get the assetRegister
        restAssetRegisterMockMvc
            .perform(get(ENTITY_API_URL_ID, assetRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetRegister.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.assetRegisterCode").value(DEFAULT_ASSET_REGISTER_CODE))
            .andExpect(jsonPath("$.assetCategoryCode").value(DEFAULT_ASSET_CATEGORY_CODE))
            .andExpect(jsonPath("$.assetSubCategoryCode").value(DEFAULT_ASSET_SUB_CATEGORY_CODE))
            .andExpect(jsonPath("$.assetName").value(DEFAULT_ASSET_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.purchaseCost").value(sameNumber(DEFAULT_PURCHASE_COST)))
            .andExpect(jsonPath("$.currentValue").value(sameNumber(DEFAULT_CURRENT_VALUE)))
            .andExpect(jsonPath("$.depreciationRate").value(sameNumber(DEFAULT_DEPRECIATION_RATE)))
            .andExpect(jsonPath("$.accumulatedDepreciation").value(sameNumber(DEFAULT_ACCUMULATED_DEPRECIATION)));
    }

    @Test
    @Transactional
    void getAssetRegistersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        Long id = assetRegister.getId();

        defaultAssetRegisterFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAssetRegisterFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAssetRegisterFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchCode equals to
        defaultAssetRegisterFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchCode in
        defaultAssetRegisterFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchCode is not null
        defaultAssetRegisterFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchCode contains
        defaultAssetRegisterFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchCode does not contain
        defaultAssetRegisterFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchId equals to
        defaultAssetRegisterFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchId in
        defaultAssetRegisterFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchId is not null
        defaultAssetRegisterFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchId contains
        defaultAssetRegisterFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where branchId does not contain
        defaultAssetRegisterFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetRegisterCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetRegisterCode equals to
        defaultAssetRegisterFiltering(
            "assetRegisterCode.equals=" + DEFAULT_ASSET_REGISTER_CODE,
            "assetRegisterCode.equals=" + UPDATED_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetRegisterCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetRegisterCode in
        defaultAssetRegisterFiltering(
            "assetRegisterCode.in=" + DEFAULT_ASSET_REGISTER_CODE + "," + UPDATED_ASSET_REGISTER_CODE,
            "assetRegisterCode.in=" + UPDATED_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetRegisterCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetRegisterCode is not null
        defaultAssetRegisterFiltering("assetRegisterCode.specified=true", "assetRegisterCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetRegisterCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetRegisterCode contains
        defaultAssetRegisterFiltering(
            "assetRegisterCode.contains=" + DEFAULT_ASSET_REGISTER_CODE,
            "assetRegisterCode.contains=" + UPDATED_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetRegisterCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetRegisterCode does not contain
        defaultAssetRegisterFiltering(
            "assetRegisterCode.doesNotContain=" + UPDATED_ASSET_REGISTER_CODE,
            "assetRegisterCode.doesNotContain=" + DEFAULT_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetCategoryCode equals to
        defaultAssetRegisterFiltering(
            "assetCategoryCode.equals=" + DEFAULT_ASSET_CATEGORY_CODE,
            "assetCategoryCode.equals=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetCategoryCode in
        defaultAssetRegisterFiltering(
            "assetCategoryCode.in=" + DEFAULT_ASSET_CATEGORY_CODE + "," + UPDATED_ASSET_CATEGORY_CODE,
            "assetCategoryCode.in=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetCategoryCode is not null
        defaultAssetRegisterFiltering("assetCategoryCode.specified=true", "assetCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetCategoryCode contains
        defaultAssetRegisterFiltering(
            "assetCategoryCode.contains=" + DEFAULT_ASSET_CATEGORY_CODE,
            "assetCategoryCode.contains=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetCategoryCode does not contain
        defaultAssetRegisterFiltering(
            "assetCategoryCode.doesNotContain=" + UPDATED_ASSET_CATEGORY_CODE,
            "assetCategoryCode.doesNotContain=" + DEFAULT_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetSubCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetSubCategoryCode equals to
        defaultAssetRegisterFiltering(
            "assetSubCategoryCode.equals=" + DEFAULT_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.equals=" + UPDATED_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetSubCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetSubCategoryCode in
        defaultAssetRegisterFiltering(
            "assetSubCategoryCode.in=" + DEFAULT_ASSET_SUB_CATEGORY_CODE + "," + UPDATED_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.in=" + UPDATED_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetSubCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetSubCategoryCode is not null
        defaultAssetRegisterFiltering("assetSubCategoryCode.specified=true", "assetSubCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetSubCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetSubCategoryCode contains
        defaultAssetRegisterFiltering(
            "assetSubCategoryCode.contains=" + DEFAULT_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.contains=" + UPDATED_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetSubCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetSubCategoryCode does not contain
        defaultAssetRegisterFiltering(
            "assetSubCategoryCode.doesNotContain=" + UPDATED_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.doesNotContain=" + DEFAULT_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetName equals to
        defaultAssetRegisterFiltering("assetName.equals=" + DEFAULT_ASSET_NAME, "assetName.equals=" + UPDATED_ASSET_NAME);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetName in
        defaultAssetRegisterFiltering(
            "assetName.in=" + DEFAULT_ASSET_NAME + "," + UPDATED_ASSET_NAME,
            "assetName.in=" + UPDATED_ASSET_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetName is not null
        defaultAssetRegisterFiltering("assetName.specified=true", "assetName.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetName contains
        defaultAssetRegisterFiltering("assetName.contains=" + DEFAULT_ASSET_NAME, "assetName.contains=" + UPDATED_ASSET_NAME);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAssetNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where assetName does not contain
        defaultAssetRegisterFiltering("assetName.doesNotContain=" + UPDATED_ASSET_NAME, "assetName.doesNotContain=" + DEFAULT_ASSET_NAME);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where category equals to
        defaultAssetRegisterFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where category in
        defaultAssetRegisterFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where category is not null
        defaultAssetRegisterFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where category contains
        defaultAssetRegisterFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where category does not contain
        defaultAssetRegisterFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseDate equals to
        defaultAssetRegisterFiltering("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE, "purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseDate in
        defaultAssetRegisterFiltering(
            "purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE,
            "purchaseDate.in=" + UPDATED_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseDate is not null
        defaultAssetRegisterFiltering("purchaseDate.specified=true", "purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseDate is greater than or equal to
        defaultAssetRegisterFiltering(
            "purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE,
            "purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseDate is less than or equal to
        defaultAssetRegisterFiltering(
            "purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE,
            "purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseDate is less than
        defaultAssetRegisterFiltering("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE, "purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseDate is greater than
        defaultAssetRegisterFiltering(
            "purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE,
            "purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseCost equals to
        defaultAssetRegisterFiltering("purchaseCost.equals=" + DEFAULT_PURCHASE_COST, "purchaseCost.equals=" + UPDATED_PURCHASE_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseCost in
        defaultAssetRegisterFiltering(
            "purchaseCost.in=" + DEFAULT_PURCHASE_COST + "," + UPDATED_PURCHASE_COST,
            "purchaseCost.in=" + UPDATED_PURCHASE_COST
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseCost is not null
        defaultAssetRegisterFiltering("purchaseCost.specified=true", "purchaseCost.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseCost is greater than or equal to
        defaultAssetRegisterFiltering(
            "purchaseCost.greaterThanOrEqual=" + DEFAULT_PURCHASE_COST,
            "purchaseCost.greaterThanOrEqual=" + UPDATED_PURCHASE_COST
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseCost is less than or equal to
        defaultAssetRegisterFiltering(
            "purchaseCost.lessThanOrEqual=" + DEFAULT_PURCHASE_COST,
            "purchaseCost.lessThanOrEqual=" + SMALLER_PURCHASE_COST
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseCost is less than
        defaultAssetRegisterFiltering("purchaseCost.lessThan=" + UPDATED_PURCHASE_COST, "purchaseCost.lessThan=" + DEFAULT_PURCHASE_COST);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByPurchaseCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where purchaseCost is greater than
        defaultAssetRegisterFiltering(
            "purchaseCost.greaterThan=" + SMALLER_PURCHASE_COST,
            "purchaseCost.greaterThan=" + DEFAULT_PURCHASE_COST
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCurrentValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where currentValue equals to
        defaultAssetRegisterFiltering("currentValue.equals=" + DEFAULT_CURRENT_VALUE, "currentValue.equals=" + UPDATED_CURRENT_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCurrentValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where currentValue in
        defaultAssetRegisterFiltering(
            "currentValue.in=" + DEFAULT_CURRENT_VALUE + "," + UPDATED_CURRENT_VALUE,
            "currentValue.in=" + UPDATED_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCurrentValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where currentValue is not null
        defaultAssetRegisterFiltering("currentValue.specified=true", "currentValue.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCurrentValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where currentValue is greater than or equal to
        defaultAssetRegisterFiltering(
            "currentValue.greaterThanOrEqual=" + DEFAULT_CURRENT_VALUE,
            "currentValue.greaterThanOrEqual=" + UPDATED_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCurrentValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where currentValue is less than or equal to
        defaultAssetRegisterFiltering(
            "currentValue.lessThanOrEqual=" + DEFAULT_CURRENT_VALUE,
            "currentValue.lessThanOrEqual=" + SMALLER_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCurrentValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where currentValue is less than
        defaultAssetRegisterFiltering("currentValue.lessThan=" + UPDATED_CURRENT_VALUE, "currentValue.lessThan=" + DEFAULT_CURRENT_VALUE);
    }

    @Test
    @Transactional
    void getAllAssetRegistersByCurrentValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where currentValue is greater than
        defaultAssetRegisterFiltering(
            "currentValue.greaterThan=" + SMALLER_CURRENT_VALUE,
            "currentValue.greaterThan=" + DEFAULT_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByDepreciationRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where depreciationRate equals to
        defaultAssetRegisterFiltering(
            "depreciationRate.equals=" + DEFAULT_DEPRECIATION_RATE,
            "depreciationRate.equals=" + UPDATED_DEPRECIATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByDepreciationRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where depreciationRate in
        defaultAssetRegisterFiltering(
            "depreciationRate.in=" + DEFAULT_DEPRECIATION_RATE + "," + UPDATED_DEPRECIATION_RATE,
            "depreciationRate.in=" + UPDATED_DEPRECIATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByDepreciationRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where depreciationRate is not null
        defaultAssetRegisterFiltering("depreciationRate.specified=true", "depreciationRate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByDepreciationRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where depreciationRate is greater than or equal to
        defaultAssetRegisterFiltering(
            "depreciationRate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_RATE,
            "depreciationRate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByDepreciationRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where depreciationRate is less than or equal to
        defaultAssetRegisterFiltering(
            "depreciationRate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_RATE,
            "depreciationRate.lessThanOrEqual=" + SMALLER_DEPRECIATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByDepreciationRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where depreciationRate is less than
        defaultAssetRegisterFiltering(
            "depreciationRate.lessThan=" + UPDATED_DEPRECIATION_RATE,
            "depreciationRate.lessThan=" + DEFAULT_DEPRECIATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByDepreciationRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where depreciationRate is greater than
        defaultAssetRegisterFiltering(
            "depreciationRate.greaterThan=" + SMALLER_DEPRECIATION_RATE,
            "depreciationRate.greaterThan=" + DEFAULT_DEPRECIATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAccumulatedDepreciationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where accumulatedDepreciation equals to
        defaultAssetRegisterFiltering(
            "accumulatedDepreciation.equals=" + DEFAULT_ACCUMULATED_DEPRECIATION,
            "accumulatedDepreciation.equals=" + UPDATED_ACCUMULATED_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAccumulatedDepreciationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where accumulatedDepreciation in
        defaultAssetRegisterFiltering(
            "accumulatedDepreciation.in=" + DEFAULT_ACCUMULATED_DEPRECIATION + "," + UPDATED_ACCUMULATED_DEPRECIATION,
            "accumulatedDepreciation.in=" + UPDATED_ACCUMULATED_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAccumulatedDepreciationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where accumulatedDepreciation is not null
        defaultAssetRegisterFiltering("accumulatedDepreciation.specified=true", "accumulatedDepreciation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAccumulatedDepreciationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where accumulatedDepreciation is greater than or equal to
        defaultAssetRegisterFiltering(
            "accumulatedDepreciation.greaterThanOrEqual=" + DEFAULT_ACCUMULATED_DEPRECIATION,
            "accumulatedDepreciation.greaterThanOrEqual=" + UPDATED_ACCUMULATED_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAccumulatedDepreciationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where accumulatedDepreciation is less than or equal to
        defaultAssetRegisterFiltering(
            "accumulatedDepreciation.lessThanOrEqual=" + DEFAULT_ACCUMULATED_DEPRECIATION,
            "accumulatedDepreciation.lessThanOrEqual=" + SMALLER_ACCUMULATED_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAccumulatedDepreciationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where accumulatedDepreciation is less than
        defaultAssetRegisterFiltering(
            "accumulatedDepreciation.lessThan=" + UPDATED_ACCUMULATED_DEPRECIATION,
            "accumulatedDepreciation.lessThan=" + DEFAULT_ACCUMULATED_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetRegistersByAccumulatedDepreciationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        // Get all the assetRegisterList where accumulatedDepreciation is greater than
        defaultAssetRegisterFiltering(
            "accumulatedDepreciation.greaterThan=" + SMALLER_ACCUMULATED_DEPRECIATION,
            "accumulatedDepreciation.greaterThan=" + DEFAULT_ACCUMULATED_DEPRECIATION
        );
    }

    private void defaultAssetRegisterFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAssetRegisterShouldBeFound(shouldBeFound);
        defaultAssetRegisterShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetRegisterShouldBeFound(String filter) throws Exception {
        restAssetRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetRegisterCode").value(hasItem(DEFAULT_ASSET_REGISTER_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryCode").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetName").value(hasItem(DEFAULT_ASSET_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchaseCost").value(hasItem(sameNumber(DEFAULT_PURCHASE_COST))))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(sameNumber(DEFAULT_CURRENT_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationRate").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_RATE))))
            .andExpect(jsonPath("$.[*].accumulatedDepreciation").value(hasItem(sameNumber(DEFAULT_ACCUMULATED_DEPRECIATION))));

        // Check, that the count call also returns 1
        restAssetRegisterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetRegisterShouldNotBeFound(String filter) throws Exception {
        restAssetRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetRegisterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetRegister() throws Exception {
        // Get the assetRegister
        restAssetRegisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssetRegister() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        assetRegisterSearchRepository.save(assetRegister);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());

        // Update the assetRegister
        AssetRegister updatedAssetRegister = assetRegisterRepository.findById(assetRegister.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAssetRegister are not directly saved in db
        em.detach(updatedAssetRegister);
        updatedAssetRegister
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetRegisterCode(UPDATED_ASSET_REGISTER_CODE)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(UPDATED_ASSET_SUB_CATEGORY_CODE)
            .assetName(UPDATED_ASSET_NAME)
            .category(UPDATED_CATEGORY)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchaseCost(UPDATED_PURCHASE_COST)
            .currentValue(UPDATED_CURRENT_VALUE)
            .depreciationRate(UPDATED_DEPRECIATION_RATE)
            .accumulatedDepreciation(UPDATED_ACCUMULATED_DEPRECIATION);
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(updatedAssetRegister);

        restAssetRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetRegisterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetRegisterDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssetRegisterToMatchAllProperties(updatedAssetRegister);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AssetRegister> assetRegisterSearchList = Streamable.of(assetRegisterSearchRepository.findAll()).toList();
                AssetRegister testAssetRegisterSearch = assetRegisterSearchList.get(searchDatabaseSizeAfter - 1);

                assertAssetRegisterAllPropertiesEquals(testAssetRegisterSearch, updatedAssetRegister);
            });
    }

    @Test
    @Transactional
    void putNonExistingAssetRegister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assetRegister.setId(longCount.incrementAndGet());

        // Create the AssetRegister
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetRegisterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetRegister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assetRegister.setId(longCount.incrementAndGet());

        // Create the AssetRegister
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetRegister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assetRegister.setId(longCount.incrementAndGet());

        // Create the AssetRegister
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegisterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetRegisterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAssetRegisterWithPatch() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetRegister using partial update
        AssetRegister partialUpdatedAssetRegister = new AssetRegister();
        partialUpdatedAssetRegister.setId(assetRegister.getId());

        partialUpdatedAssetRegister
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetSubCategoryCode(UPDATED_ASSET_SUB_CATEGORY_CODE)
            .category(UPDATED_CATEGORY)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .depreciationRate(UPDATED_DEPRECIATION_RATE)
            .accumulatedDepreciation(UPDATED_ACCUMULATED_DEPRECIATION);

        restAssetRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetRegister))
            )
            .andExpect(status().isOk());

        // Validate the AssetRegister in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetRegisterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssetRegister, assetRegister),
            getPersistedAssetRegister(assetRegister)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssetRegisterWithPatch() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetRegister using partial update
        AssetRegister partialUpdatedAssetRegister = new AssetRegister();
        partialUpdatedAssetRegister.setId(assetRegister.getId());

        partialUpdatedAssetRegister
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetRegisterCode(UPDATED_ASSET_REGISTER_CODE)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(UPDATED_ASSET_SUB_CATEGORY_CODE)
            .assetName(UPDATED_ASSET_NAME)
            .category(UPDATED_CATEGORY)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchaseCost(UPDATED_PURCHASE_COST)
            .currentValue(UPDATED_CURRENT_VALUE)
            .depreciationRate(UPDATED_DEPRECIATION_RATE)
            .accumulatedDepreciation(UPDATED_ACCUMULATED_DEPRECIATION);

        restAssetRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetRegister))
            )
            .andExpect(status().isOk());

        // Validate the AssetRegister in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetRegisterUpdatableFieldsEquals(partialUpdatedAssetRegister, getPersistedAssetRegister(partialUpdatedAssetRegister));
    }

    @Test
    @Transactional
    void patchNonExistingAssetRegister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assetRegister.setId(longCount.incrementAndGet());

        // Create the AssetRegister
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetRegisterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetRegister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assetRegister.setId(longCount.incrementAndGet());

        // Create the AssetRegister
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetRegister() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assetRegister.setId(longCount.incrementAndGet());

        // Create the AssetRegister
        AssetRegisterDTO assetRegisterDTO = assetRegisterMapper.toDto(assetRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetRegisterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assetRegisterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetRegister in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAssetRegister() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);
        assetRegisterRepository.save(assetRegister);
        assetRegisterSearchRepository.save(assetRegister);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assetRegister
        restAssetRegisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetRegister.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetRegisterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAssetRegister() throws Exception {
        // Initialize the database
        insertedAssetRegister = assetRegisterRepository.saveAndFlush(assetRegister);
        assetRegisterSearchRepository.save(assetRegister);

        // Search the assetRegister
        restAssetRegisterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetRegisterCode").value(hasItem(DEFAULT_ASSET_REGISTER_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryCode").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetName").value(hasItem(DEFAULT_ASSET_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchaseCost").value(hasItem(sameNumber(DEFAULT_PURCHASE_COST))))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(sameNumber(DEFAULT_CURRENT_VALUE))))
            .andExpect(jsonPath("$.[*].depreciationRate").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_RATE))))
            .andExpect(jsonPath("$.[*].accumulatedDepreciation").value(hasItem(sameNumber(DEFAULT_ACCUMULATED_DEPRECIATION))));
    }

    protected long getRepositoryCount() {
        return assetRegisterRepository.count();
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

    protected AssetRegister getPersistedAssetRegister(AssetRegister assetRegister) {
        return assetRegisterRepository.findById(assetRegister.getId()).orElseThrow();
    }

    protected void assertPersistedAssetRegisterToMatchAllProperties(AssetRegister expectedAssetRegister) {
        assertAssetRegisterAllPropertiesEquals(expectedAssetRegister, getPersistedAssetRegister(expectedAssetRegister));
    }

    protected void assertPersistedAssetRegisterToMatchUpdatableProperties(AssetRegister expectedAssetRegister) {
        assertAssetRegisterAllUpdatablePropertiesEquals(expectedAssetRegister, getPersistedAssetRegister(expectedAssetRegister));
    }
}
