package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.AssetDepreciationHistoryAsserts.*;
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
import com.gvsolutions.domain.AssetDepreciationHistory;
import com.gvsolutions.repository.AssetDepreciationHistoryRepository;
import com.gvsolutions.repository.search.AssetDepreciationHistorySearchRepository;
import com.gvsolutions.service.dto.AssetDepreciationHistoryDTO;
import com.gvsolutions.service.mapper.AssetDepreciationHistoryMapper;
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
 * Integration tests for the {@link AssetDepreciationHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetDepreciationHistoryResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_REGISTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_REGISTER_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEPRECIATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPRECIATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPRECIATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALUE_AFTER_DEPRECIATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE_AFTER_DEPRECIATION = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE_AFTER_DEPRECIATION = new BigDecimal(1 - 1);

    private static final String DEFAULT_PROCESSED_BY = "AAAAAAAAAA";
    private static final String UPDATED_PROCESSED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/asset-depreciation-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/asset-depreciation-histories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssetDepreciationHistoryRepository assetDepreciationHistoryRepository;

    @Autowired
    private AssetDepreciationHistoryMapper assetDepreciationHistoryMapper;

    @Autowired
    private AssetDepreciationHistorySearchRepository assetDepreciationHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetDepreciationHistoryMockMvc;

    private AssetDepreciationHistory assetDepreciationHistory;

    private AssetDepreciationHistory insertedAssetDepreciationHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDepreciationHistory createEntity() {
        return new AssetDepreciationHistory()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .assetRegisterCode(DEFAULT_ASSET_REGISTER_CODE)
            .depreciationDate(DEFAULT_DEPRECIATION_DATE)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .valueAfterDepreciation(DEFAULT_VALUE_AFTER_DEPRECIATION)
            .processedBy(DEFAULT_PROCESSED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetDepreciationHistory createUpdatedEntity() {
        return new AssetDepreciationHistory()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetRegisterCode(UPDATED_ASSET_REGISTER_CODE)
            .depreciationDate(UPDATED_DEPRECIATION_DATE)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .valueAfterDepreciation(UPDATED_VALUE_AFTER_DEPRECIATION)
            .processedBy(UPDATED_PROCESSED_BY);
    }

    @BeforeEach
    void initTest() {
        assetDepreciationHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAssetDepreciationHistory != null) {
            assetDepreciationHistoryRepository.delete(insertedAssetDepreciationHistory);
            assetDepreciationHistorySearchRepository.delete(insertedAssetDepreciationHistory);
            insertedAssetDepreciationHistory = null;
        }
    }

    @Test
    @Transactional
    void createAssetDepreciationHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        // Create the AssetDepreciationHistory
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);
        var returnedAssetDepreciationHistoryDTO = om.readValue(
            restAssetDepreciationHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssetDepreciationHistoryDTO.class
        );

        // Validate the AssetDepreciationHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssetDepreciationHistory = assetDepreciationHistoryMapper.toEntity(returnedAssetDepreciationHistoryDTO);
        assertAssetDepreciationHistoryUpdatableFieldsEquals(
            returnedAssetDepreciationHistory,
            getPersistedAssetDepreciationHistory(returnedAssetDepreciationHistory)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAssetDepreciationHistory = returnedAssetDepreciationHistory;
    }

    @Test
    @Transactional
    void createAssetDepreciationHistoryWithExistingId() throws Exception {
        // Create the AssetDepreciationHistory with an existing ID
        assetDepreciationHistory.setId(1L);
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetDepreciationHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistories() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList
        restAssetDepreciationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDepreciationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetRegisterCode").value(hasItem(DEFAULT_ASSET_REGISTER_CODE)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].valueAfterDepreciation").value(hasItem(sameNumber(DEFAULT_VALUE_AFTER_DEPRECIATION))))
            .andExpect(jsonPath("$.[*].processedBy").value(hasItem(DEFAULT_PROCESSED_BY)));
    }

    @Test
    @Transactional
    void getAssetDepreciationHistory() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get the assetDepreciationHistory
        restAssetDepreciationHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, assetDepreciationHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetDepreciationHistory.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.assetRegisterCode").value(DEFAULT_ASSET_REGISTER_CODE))
            .andExpect(jsonPath("$.depreciationDate").value(DEFAULT_DEPRECIATION_DATE.toString()))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.valueAfterDepreciation").value(sameNumber(DEFAULT_VALUE_AFTER_DEPRECIATION)))
            .andExpect(jsonPath("$.processedBy").value(DEFAULT_PROCESSED_BY));
    }

    @Test
    @Transactional
    void getAssetDepreciationHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        Long id = assetDepreciationHistory.getId();

        defaultAssetDepreciationHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAssetDepreciationHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAssetDepreciationHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchCode equals to
        defaultAssetDepreciationHistoryFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchCode in
        defaultAssetDepreciationHistoryFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchCode is not null
        defaultAssetDepreciationHistoryFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchCode contains
        defaultAssetDepreciationHistoryFiltering(
            "branchCode.contains=" + DEFAULT_BRANCH_CODE,
            "branchCode.contains=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchCode does not contain
        defaultAssetDepreciationHistoryFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchId equals to
        defaultAssetDepreciationHistoryFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchId in
        defaultAssetDepreciationHistoryFiltering(
            "branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID,
            "branchId.in=" + UPDATED_BRANCH_ID
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchId is not null
        defaultAssetDepreciationHistoryFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchId contains
        defaultAssetDepreciationHistoryFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where branchId does not contain
        defaultAssetDepreciationHistoryFiltering(
            "branchId.doesNotContain=" + UPDATED_BRANCH_ID,
            "branchId.doesNotContain=" + DEFAULT_BRANCH_ID
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByAssetRegisterCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where assetRegisterCode equals to
        defaultAssetDepreciationHistoryFiltering(
            "assetRegisterCode.equals=" + DEFAULT_ASSET_REGISTER_CODE,
            "assetRegisterCode.equals=" + UPDATED_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByAssetRegisterCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where assetRegisterCode in
        defaultAssetDepreciationHistoryFiltering(
            "assetRegisterCode.in=" + DEFAULT_ASSET_REGISTER_CODE + "," + UPDATED_ASSET_REGISTER_CODE,
            "assetRegisterCode.in=" + UPDATED_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByAssetRegisterCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where assetRegisterCode is not null
        defaultAssetDepreciationHistoryFiltering("assetRegisterCode.specified=true", "assetRegisterCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByAssetRegisterCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where assetRegisterCode contains
        defaultAssetDepreciationHistoryFiltering(
            "assetRegisterCode.contains=" + DEFAULT_ASSET_REGISTER_CODE,
            "assetRegisterCode.contains=" + UPDATED_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByAssetRegisterCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where assetRegisterCode does not contain
        defaultAssetDepreciationHistoryFiltering(
            "assetRegisterCode.doesNotContain=" + UPDATED_ASSET_REGISTER_CODE,
            "assetRegisterCode.doesNotContain=" + DEFAULT_ASSET_REGISTER_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationDate equals to
        defaultAssetDepreciationHistoryFiltering(
            "depreciationDate.equals=" + DEFAULT_DEPRECIATION_DATE,
            "depreciationDate.equals=" + UPDATED_DEPRECIATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationDate in
        defaultAssetDepreciationHistoryFiltering(
            "depreciationDate.in=" + DEFAULT_DEPRECIATION_DATE + "," + UPDATED_DEPRECIATION_DATE,
            "depreciationDate.in=" + UPDATED_DEPRECIATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationDate is not null
        defaultAssetDepreciationHistoryFiltering("depreciationDate.specified=true", "depreciationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationDate is greater than or equal to
        defaultAssetDepreciationHistoryFiltering(
            "depreciationDate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_DATE,
            "depreciationDate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationDate is less than or equal to
        defaultAssetDepreciationHistoryFiltering(
            "depreciationDate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_DATE,
            "depreciationDate.lessThanOrEqual=" + SMALLER_DEPRECIATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationDate is less than
        defaultAssetDepreciationHistoryFiltering(
            "depreciationDate.lessThan=" + UPDATED_DEPRECIATION_DATE,
            "depreciationDate.lessThan=" + DEFAULT_DEPRECIATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationDate is greater than
        defaultAssetDepreciationHistoryFiltering(
            "depreciationDate.greaterThan=" + SMALLER_DEPRECIATION_DATE,
            "depreciationDate.greaterThan=" + DEFAULT_DEPRECIATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationAmount equals to
        defaultAssetDepreciationHistoryFiltering(
            "depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT,
            "depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationAmount in
        defaultAssetDepreciationHistoryFiltering(
            "depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT,
            "depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationAmount is not null
        defaultAssetDepreciationHistoryFiltering("depreciationAmount.specified=true", "depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationAmount is greater than or equal to
        defaultAssetDepreciationHistoryFiltering(
            "depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT,
            "depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationAmount is less than or equal to
        defaultAssetDepreciationHistoryFiltering(
            "depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT,
            "depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationAmount is less than
        defaultAssetDepreciationHistoryFiltering(
            "depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT,
            "depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where depreciationAmount is greater than
        defaultAssetDepreciationHistoryFiltering(
            "depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT,
            "depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByValueAfterDepreciationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where valueAfterDepreciation equals to
        defaultAssetDepreciationHistoryFiltering(
            "valueAfterDepreciation.equals=" + DEFAULT_VALUE_AFTER_DEPRECIATION,
            "valueAfterDepreciation.equals=" + UPDATED_VALUE_AFTER_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByValueAfterDepreciationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where valueAfterDepreciation in
        defaultAssetDepreciationHistoryFiltering(
            "valueAfterDepreciation.in=" + DEFAULT_VALUE_AFTER_DEPRECIATION + "," + UPDATED_VALUE_AFTER_DEPRECIATION,
            "valueAfterDepreciation.in=" + UPDATED_VALUE_AFTER_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByValueAfterDepreciationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where valueAfterDepreciation is not null
        defaultAssetDepreciationHistoryFiltering("valueAfterDepreciation.specified=true", "valueAfterDepreciation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByValueAfterDepreciationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where valueAfterDepreciation is greater than or equal to
        defaultAssetDepreciationHistoryFiltering(
            "valueAfterDepreciation.greaterThanOrEqual=" + DEFAULT_VALUE_AFTER_DEPRECIATION,
            "valueAfterDepreciation.greaterThanOrEqual=" + UPDATED_VALUE_AFTER_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByValueAfterDepreciationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where valueAfterDepreciation is less than or equal to
        defaultAssetDepreciationHistoryFiltering(
            "valueAfterDepreciation.lessThanOrEqual=" + DEFAULT_VALUE_AFTER_DEPRECIATION,
            "valueAfterDepreciation.lessThanOrEqual=" + SMALLER_VALUE_AFTER_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByValueAfterDepreciationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where valueAfterDepreciation is less than
        defaultAssetDepreciationHistoryFiltering(
            "valueAfterDepreciation.lessThan=" + UPDATED_VALUE_AFTER_DEPRECIATION,
            "valueAfterDepreciation.lessThan=" + DEFAULT_VALUE_AFTER_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByValueAfterDepreciationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where valueAfterDepreciation is greater than
        defaultAssetDepreciationHistoryFiltering(
            "valueAfterDepreciation.greaterThan=" + SMALLER_VALUE_AFTER_DEPRECIATION,
            "valueAfterDepreciation.greaterThan=" + DEFAULT_VALUE_AFTER_DEPRECIATION
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByProcessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where processedBy equals to
        defaultAssetDepreciationHistoryFiltering(
            "processedBy.equals=" + DEFAULT_PROCESSED_BY,
            "processedBy.equals=" + UPDATED_PROCESSED_BY
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByProcessedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where processedBy in
        defaultAssetDepreciationHistoryFiltering(
            "processedBy.in=" + DEFAULT_PROCESSED_BY + "," + UPDATED_PROCESSED_BY,
            "processedBy.in=" + UPDATED_PROCESSED_BY
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByProcessedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where processedBy is not null
        defaultAssetDepreciationHistoryFiltering("processedBy.specified=true", "processedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByProcessedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where processedBy contains
        defaultAssetDepreciationHistoryFiltering(
            "processedBy.contains=" + DEFAULT_PROCESSED_BY,
            "processedBy.contains=" + UPDATED_PROCESSED_BY
        );
    }

    @Test
    @Transactional
    void getAllAssetDepreciationHistoriesByProcessedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        // Get all the assetDepreciationHistoryList where processedBy does not contain
        defaultAssetDepreciationHistoryFiltering(
            "processedBy.doesNotContain=" + UPDATED_PROCESSED_BY,
            "processedBy.doesNotContain=" + DEFAULT_PROCESSED_BY
        );
    }

    private void defaultAssetDepreciationHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAssetDepreciationHistoryShouldBeFound(shouldBeFound);
        defaultAssetDepreciationHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetDepreciationHistoryShouldBeFound(String filter) throws Exception {
        restAssetDepreciationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDepreciationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetRegisterCode").value(hasItem(DEFAULT_ASSET_REGISTER_CODE)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].valueAfterDepreciation").value(hasItem(sameNumber(DEFAULT_VALUE_AFTER_DEPRECIATION))))
            .andExpect(jsonPath("$.[*].processedBy").value(hasItem(DEFAULT_PROCESSED_BY)));

        // Check, that the count call also returns 1
        restAssetDepreciationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetDepreciationHistoryShouldNotBeFound(String filter) throws Exception {
        restAssetDepreciationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetDepreciationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetDepreciationHistory() throws Exception {
        // Get the assetDepreciationHistory
        restAssetDepreciationHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssetDepreciationHistory() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        assetDepreciationHistorySearchRepository.save(assetDepreciationHistory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());

        // Update the assetDepreciationHistory
        AssetDepreciationHistory updatedAssetDepreciationHistory = assetDepreciationHistoryRepository
            .findById(assetDepreciationHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAssetDepreciationHistory are not directly saved in db
        em.detach(updatedAssetDepreciationHistory);
        updatedAssetDepreciationHistory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetRegisterCode(UPDATED_ASSET_REGISTER_CODE)
            .depreciationDate(UPDATED_DEPRECIATION_DATE)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .valueAfterDepreciation(UPDATED_VALUE_AFTER_DEPRECIATION)
            .processedBy(UPDATED_PROCESSED_BY);
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(updatedAssetDepreciationHistory);

        restAssetDepreciationHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDepreciationHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssetDepreciationHistoryToMatchAllProperties(updatedAssetDepreciationHistory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AssetDepreciationHistory> assetDepreciationHistorySearchList = Streamable.of(
                    assetDepreciationHistorySearchRepository.findAll()
                ).toList();
                AssetDepreciationHistory testAssetDepreciationHistorySearch = assetDepreciationHistorySearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertAssetDepreciationHistoryAllPropertiesEquals(testAssetDepreciationHistorySearch, updatedAssetDepreciationHistory);
            });
    }

    @Test
    @Transactional
    void putNonExistingAssetDepreciationHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assetDepreciationHistory.setId(longCount.incrementAndGet());

        // Create the AssetDepreciationHistory
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDepreciationHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDepreciationHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetDepreciationHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assetDepreciationHistory.setId(longCount.incrementAndGet());

        // Create the AssetDepreciationHistory
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDepreciationHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetDepreciationHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assetDepreciationHistory.setId(longCount.incrementAndGet());

        // Create the AssetDepreciationHistory
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDepreciationHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetDepreciationHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAssetDepreciationHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetDepreciationHistory using partial update
        AssetDepreciationHistory partialUpdatedAssetDepreciationHistory = new AssetDepreciationHistory();
        partialUpdatedAssetDepreciationHistory.setId(assetDepreciationHistory.getId());

        partialUpdatedAssetDepreciationHistory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .depreciationDate(UPDATED_DEPRECIATION_DATE)
            .valueAfterDepreciation(UPDATED_VALUE_AFTER_DEPRECIATION)
            .processedBy(UPDATED_PROCESSED_BY);

        restAssetDepreciationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDepreciationHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetDepreciationHistory))
            )
            .andExpect(status().isOk());

        // Validate the AssetDepreciationHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetDepreciationHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssetDepreciationHistory, assetDepreciationHistory),
            getPersistedAssetDepreciationHistory(assetDepreciationHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssetDepreciationHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetDepreciationHistory using partial update
        AssetDepreciationHistory partialUpdatedAssetDepreciationHistory = new AssetDepreciationHistory();
        partialUpdatedAssetDepreciationHistory.setId(assetDepreciationHistory.getId());

        partialUpdatedAssetDepreciationHistory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetRegisterCode(UPDATED_ASSET_REGISTER_CODE)
            .depreciationDate(UPDATED_DEPRECIATION_DATE)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .valueAfterDepreciation(UPDATED_VALUE_AFTER_DEPRECIATION)
            .processedBy(UPDATED_PROCESSED_BY);

        restAssetDepreciationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetDepreciationHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetDepreciationHistory))
            )
            .andExpect(status().isOk());

        // Validate the AssetDepreciationHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetDepreciationHistoryUpdatableFieldsEquals(
            partialUpdatedAssetDepreciationHistory,
            getPersistedAssetDepreciationHistory(partialUpdatedAssetDepreciationHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAssetDepreciationHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assetDepreciationHistory.setId(longCount.incrementAndGet());

        // Create the AssetDepreciationHistory
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetDepreciationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetDepreciationHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetDepreciationHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assetDepreciationHistory.setId(longCount.incrementAndGet());

        // Create the AssetDepreciationHistory
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDepreciationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetDepreciationHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assetDepreciationHistory.setId(longCount.incrementAndGet());

        // Create the AssetDepreciationHistory
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = assetDepreciationHistoryMapper.toDto(assetDepreciationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetDepreciationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assetDepreciationHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetDepreciationHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAssetDepreciationHistory() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);
        assetDepreciationHistoryRepository.save(assetDepreciationHistory);
        assetDepreciationHistorySearchRepository.save(assetDepreciationHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assetDepreciationHistory
        restAssetDepreciationHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetDepreciationHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetDepreciationHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAssetDepreciationHistory() throws Exception {
        // Initialize the database
        insertedAssetDepreciationHistory = assetDepreciationHistoryRepository.saveAndFlush(assetDepreciationHistory);
        assetDepreciationHistorySearchRepository.save(assetDepreciationHistory);

        // Search the assetDepreciationHistory
        restAssetDepreciationHistoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetDepreciationHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetDepreciationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetRegisterCode").value(hasItem(DEFAULT_ASSET_REGISTER_CODE)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].valueAfterDepreciation").value(hasItem(sameNumber(DEFAULT_VALUE_AFTER_DEPRECIATION))))
            .andExpect(jsonPath("$.[*].processedBy").value(hasItem(DEFAULT_PROCESSED_BY)));
    }

    protected long getRepositoryCount() {
        return assetDepreciationHistoryRepository.count();
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

    protected AssetDepreciationHistory getPersistedAssetDepreciationHistory(AssetDepreciationHistory assetDepreciationHistory) {
        return assetDepreciationHistoryRepository.findById(assetDepreciationHistory.getId()).orElseThrow();
    }

    protected void assertPersistedAssetDepreciationHistoryToMatchAllProperties(AssetDepreciationHistory expectedAssetDepreciationHistory) {
        assertAssetDepreciationHistoryAllPropertiesEquals(
            expectedAssetDepreciationHistory,
            getPersistedAssetDepreciationHistory(expectedAssetDepreciationHistory)
        );
    }

    protected void assertPersistedAssetDepreciationHistoryToMatchUpdatableProperties(
        AssetDepreciationHistory expectedAssetDepreciationHistory
    ) {
        assertAssetDepreciationHistoryAllUpdatablePropertiesEquals(
            expectedAssetDepreciationHistory,
            getPersistedAssetDepreciationHistory(expectedAssetDepreciationHistory)
        );
    }
}
