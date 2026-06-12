package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.AssetCategoryAsserts.*;
import static com.gvsolutions.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gvsolutions.IntegrationTest;
import com.gvsolutions.domain.AssetCategory;
import com.gvsolutions.repository.AssetCategoryRepository;
import com.gvsolutions.repository.search.AssetCategorySearchRepository;
import com.gvsolutions.service.dto.AssetCategoryDTO;
import com.gvsolutions.service.mapper.AssetCategoryMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AssetCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetCategoryResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/asset-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/asset-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssetCategoryRepository assetCategoryRepository;

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Autowired
    private AssetCategorySearchRepository assetCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetCategoryMockMvc;

    private AssetCategory assetCategory;

    private AssetCategory insertedAssetCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetCategory createEntity() {
        return new AssetCategory()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .assetCategoryCode(DEFAULT_ASSET_CATEGORY_CODE)
            .assetCategoryName(DEFAULT_ASSET_CATEGORY_NAME)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetCategory createUpdatedEntity() {
        return new AssetCategory()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        assetCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAssetCategory != null) {
            assetCategoryRepository.delete(insertedAssetCategory);
            assetCategorySearchRepository.delete(insertedAssetCategory);
            insertedAssetCategory = null;
        }
    }

    @Test
    @Transactional
    void createAssetCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);
        var returnedAssetCategoryDTO = om.readValue(
            restAssetCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssetCategoryDTO.class
        );

        // Validate the AssetCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssetCategory = assetCategoryMapper.toEntity(returnedAssetCategoryDTO);
        assertAssetCategoryUpdatableFieldsEquals(returnedAssetCategory, getPersistedAssetCategory(returnedAssetCategory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAssetCategory = returnedAssetCategory;
    }

    @Test
    @Transactional
    void createAssetCategoryWithExistingId() throws Exception {
        // Create the AssetCategory with an existing ID
        assetCategory.setId(1L);
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAssetCategories() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAssetCategory() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get the assetCategory
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, assetCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetCategory.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.assetCategoryCode").value(DEFAULT_ASSET_CATEGORY_CODE))
            .andExpect(jsonPath("$.assetCategoryName").value(DEFAULT_ASSET_CATEGORY_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getAssetCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        Long id = assetCategory.getId();

        defaultAssetCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAssetCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAssetCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchCode equals to
        defaultAssetCategoryFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchCode in
        defaultAssetCategoryFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchCode is not null
        defaultAssetCategoryFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchCode contains
        defaultAssetCategoryFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchCode does not contain
        defaultAssetCategoryFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchId equals to
        defaultAssetCategoryFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchId in
        defaultAssetCategoryFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchId is not null
        defaultAssetCategoryFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchId contains
        defaultAssetCategoryFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where branchId does not contain
        defaultAssetCategoryFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryCode equals to
        defaultAssetCategoryFiltering(
            "assetCategoryCode.equals=" + DEFAULT_ASSET_CATEGORY_CODE,
            "assetCategoryCode.equals=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryCode in
        defaultAssetCategoryFiltering(
            "assetCategoryCode.in=" + DEFAULT_ASSET_CATEGORY_CODE + "," + UPDATED_ASSET_CATEGORY_CODE,
            "assetCategoryCode.in=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryCode is not null
        defaultAssetCategoryFiltering("assetCategoryCode.specified=true", "assetCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryCode contains
        defaultAssetCategoryFiltering(
            "assetCategoryCode.contains=" + DEFAULT_ASSET_CATEGORY_CODE,
            "assetCategoryCode.contains=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryCode does not contain
        defaultAssetCategoryFiltering(
            "assetCategoryCode.doesNotContain=" + UPDATED_ASSET_CATEGORY_CODE,
            "assetCategoryCode.doesNotContain=" + DEFAULT_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName equals to
        defaultAssetCategoryFiltering(
            "assetCategoryName.equals=" + DEFAULT_ASSET_CATEGORY_NAME,
            "assetCategoryName.equals=" + UPDATED_ASSET_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName in
        defaultAssetCategoryFiltering(
            "assetCategoryName.in=" + DEFAULT_ASSET_CATEGORY_NAME + "," + UPDATED_ASSET_CATEGORY_NAME,
            "assetCategoryName.in=" + UPDATED_ASSET_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName is not null
        defaultAssetCategoryFiltering("assetCategoryName.specified=true", "assetCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName contains
        defaultAssetCategoryFiltering(
            "assetCategoryName.contains=" + DEFAULT_ASSET_CATEGORY_NAME,
            "assetCategoryName.contains=" + UPDATED_ASSET_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName does not contain
        defaultAssetCategoryFiltering(
            "assetCategoryName.doesNotContain=" + UPDATED_ASSET_CATEGORY_NAME,
            "assetCategoryName.doesNotContain=" + DEFAULT_ASSET_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description equals to
        defaultAssetCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description in
        defaultAssetCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description is not null
        defaultAssetCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description contains
        defaultAssetCategoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description does not contain
        defaultAssetCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultAssetCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAssetCategoryShouldBeFound(shouldBeFound);
        defaultAssetCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetCategoryShouldBeFound(String filter) throws Exception {
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetCategoryShouldNotBeFound(String filter) throws Exception {
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetCategory() throws Exception {
        // Get the assetCategory
        restAssetCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssetCategory() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        assetCategorySearchRepository.save(assetCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());

        // Update the assetCategory
        AssetCategory updatedAssetCategory = assetCategoryRepository.findById(assetCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAssetCategory are not directly saved in db
        em.detach(updatedAssetCategory);
        updatedAssetCategory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION);
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(updatedAssetCategory);

        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssetCategoryToMatchAllProperties(updatedAssetCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AssetCategory> assetCategorySearchList = Streamable.of(assetCategorySearchRepository.findAll()).toList();
                AssetCategory testAssetCategorySearch = assetCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertAssetCategoryAllPropertiesEquals(testAssetCategorySearch, updatedAssetCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingAssetCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assetCategory.setId(longCount.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assetCategory.setId(longCount.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assetCategory.setId(longCount.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAssetCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetCategory using partial update
        AssetCategory partialUpdatedAssetCategory = new AssetCategory();
        partialUpdatedAssetCategory.setId(assetCategory.getId());

        partialUpdatedAssetCategory
            .branchId(UPDATED_BRANCH_ID)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .description(UPDATED_DESCRIPTION);

        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssetCategory, assetCategory),
            getPersistedAssetCategory(assetCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssetCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetCategory using partial update
        AssetCategory partialUpdatedAssetCategory = new AssetCategory();
        partialUpdatedAssetCategory.setId(assetCategory.getId());

        partialUpdatedAssetCategory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION);

        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetCategoryUpdatableFieldsEquals(partialUpdatedAssetCategory, getPersistedAssetCategory(partialUpdatedAssetCategory));
    }

    @Test
    @Transactional
    void patchNonExistingAssetCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assetCategory.setId(longCount.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assetCategory.setId(longCount.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assetCategory.setId(longCount.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assetCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAssetCategory() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);
        assetCategoryRepository.save(assetCategory);
        assetCategorySearchRepository.save(assetCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assetCategory
        restAssetCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAssetCategory() throws Exception {
        // Initialize the database
        insertedAssetCategory = assetCategoryRepository.saveAndFlush(assetCategory);
        assetCategorySearchRepository.save(assetCategory);

        // Search the assetCategory
        restAssetCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    protected long getRepositoryCount() {
        return assetCategoryRepository.count();
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

    protected AssetCategory getPersistedAssetCategory(AssetCategory assetCategory) {
        return assetCategoryRepository.findById(assetCategory.getId()).orElseThrow();
    }

    protected void assertPersistedAssetCategoryToMatchAllProperties(AssetCategory expectedAssetCategory) {
        assertAssetCategoryAllPropertiesEquals(expectedAssetCategory, getPersistedAssetCategory(expectedAssetCategory));
    }

    protected void assertPersistedAssetCategoryToMatchUpdatableProperties(AssetCategory expectedAssetCategory) {
        assertAssetCategoryAllUpdatablePropertiesEquals(expectedAssetCategory, getPersistedAssetCategory(expectedAssetCategory));
    }
}
