package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.AssetSubCategoryAsserts.*;
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
import com.gvsolutions.domain.AssetSubCategory;
import com.gvsolutions.repository.AssetSubCategoryRepository;
import com.gvsolutions.repository.search.AssetSubCategorySearchRepository;
import com.gvsolutions.service.AssetSubCategoryService;
import com.gvsolutions.service.dto.AssetSubCategoryDTO;
import com.gvsolutions.service.mapper.AssetSubCategoryMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssetSubCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetSubCategoryResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_SUB_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_SUB_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_SUB_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_SUB_CATEGORY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/asset-sub-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/asset-sub-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssetSubCategoryRepository assetSubCategoryRepository;

    @Mock
    private AssetSubCategoryRepository assetSubCategoryRepositoryMock;

    @Autowired
    private AssetSubCategoryMapper assetSubCategoryMapper;

    @Mock
    private AssetSubCategoryService assetSubCategoryServiceMock;

    @Autowired
    private AssetSubCategorySearchRepository assetSubCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetSubCategoryMockMvc;

    private AssetSubCategory assetSubCategory;

    private AssetSubCategory insertedAssetSubCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetSubCategory createEntity() {
        return new AssetSubCategory()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .assetCategoryCode(DEFAULT_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(DEFAULT_ASSET_SUB_CATEGORY_CODE)
            .assetSubCategoryName(DEFAULT_ASSET_SUB_CATEGORY_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetSubCategory createUpdatedEntity() {
        return new AssetSubCategory()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(UPDATED_ASSET_SUB_CATEGORY_CODE)
            .assetSubCategoryName(UPDATED_ASSET_SUB_CATEGORY_NAME)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        assetSubCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAssetSubCategory != null) {
            assetSubCategoryRepository.delete(insertedAssetSubCategory);
            assetSubCategorySearchRepository.delete(insertedAssetSubCategory);
            insertedAssetSubCategory = null;
        }
    }

    @Test
    @Transactional
    void createAssetSubCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        // Create the AssetSubCategory
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);
        var returnedAssetSubCategoryDTO = om.readValue(
            restAssetSubCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetSubCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssetSubCategoryDTO.class
        );

        // Validate the AssetSubCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssetSubCategory = assetSubCategoryMapper.toEntity(returnedAssetSubCategoryDTO);
        assertAssetSubCategoryUpdatableFieldsEquals(returnedAssetSubCategory, getPersistedAssetSubCategory(returnedAssetSubCategory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAssetSubCategory = returnedAssetSubCategory;
    }

    @Test
    @Transactional
    void createAssetSubCategoryWithExistingId() throws Exception {
        // Create the AssetSubCategory with an existing ID
        assetSubCategory.setId(1L);
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetSubCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetSubCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAssetSubCategories() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList
        restAssetSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryCode").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryName").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetSubCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetSubCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetSubCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetSubCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetSubCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetSubCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetSubCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(assetSubCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAssetSubCategory() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get the assetSubCategory
        restAssetSubCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, assetSubCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetSubCategory.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.assetCategoryCode").value(DEFAULT_ASSET_CATEGORY_CODE))
            .andExpect(jsonPath("$.assetSubCategoryCode").value(DEFAULT_ASSET_SUB_CATEGORY_CODE))
            .andExpect(jsonPath("$.assetSubCategoryName").value(DEFAULT_ASSET_SUB_CATEGORY_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getAssetSubCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        Long id = assetSubCategory.getId();

        defaultAssetSubCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAssetSubCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAssetSubCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchCode equals to
        defaultAssetSubCategoryFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchCode in
        defaultAssetSubCategoryFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchCode is not null
        defaultAssetSubCategoryFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchCode contains
        defaultAssetSubCategoryFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchCode does not contain
        defaultAssetSubCategoryFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchId equals to
        defaultAssetSubCategoryFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchId in
        defaultAssetSubCategoryFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchId is not null
        defaultAssetSubCategoryFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchId contains
        defaultAssetSubCategoryFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where branchId does not contain
        defaultAssetSubCategoryFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetCategoryCode equals to
        defaultAssetSubCategoryFiltering(
            "assetCategoryCode.equals=" + DEFAULT_ASSET_CATEGORY_CODE,
            "assetCategoryCode.equals=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetCategoryCode in
        defaultAssetSubCategoryFiltering(
            "assetCategoryCode.in=" + DEFAULT_ASSET_CATEGORY_CODE + "," + UPDATED_ASSET_CATEGORY_CODE,
            "assetCategoryCode.in=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetCategoryCode is not null
        defaultAssetSubCategoryFiltering("assetCategoryCode.specified=true", "assetCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetCategoryCode contains
        defaultAssetSubCategoryFiltering(
            "assetCategoryCode.contains=" + DEFAULT_ASSET_CATEGORY_CODE,
            "assetCategoryCode.contains=" + UPDATED_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetCategoryCode does not contain
        defaultAssetSubCategoryFiltering(
            "assetCategoryCode.doesNotContain=" + UPDATED_ASSET_CATEGORY_CODE,
            "assetCategoryCode.doesNotContain=" + DEFAULT_ASSET_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryCode equals to
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryCode.equals=" + DEFAULT_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.equals=" + UPDATED_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryCode in
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryCode.in=" + DEFAULT_ASSET_SUB_CATEGORY_CODE + "," + UPDATED_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.in=" + UPDATED_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryCode is not null
        defaultAssetSubCategoryFiltering("assetSubCategoryCode.specified=true", "assetSubCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryCode contains
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryCode.contains=" + DEFAULT_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.contains=" + UPDATED_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryCode does not contain
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryCode.doesNotContain=" + UPDATED_ASSET_SUB_CATEGORY_CODE,
            "assetSubCategoryCode.doesNotContain=" + DEFAULT_ASSET_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryName equals to
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryName.equals=" + DEFAULT_ASSET_SUB_CATEGORY_NAME,
            "assetSubCategoryName.equals=" + UPDATED_ASSET_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryName in
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryName.in=" + DEFAULT_ASSET_SUB_CATEGORY_NAME + "," + UPDATED_ASSET_SUB_CATEGORY_NAME,
            "assetSubCategoryName.in=" + UPDATED_ASSET_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryName is not null
        defaultAssetSubCategoryFiltering("assetSubCategoryName.specified=true", "assetSubCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryName contains
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryName.contains=" + DEFAULT_ASSET_SUB_CATEGORY_NAME,
            "assetSubCategoryName.contains=" + UPDATED_ASSET_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByAssetSubCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where assetSubCategoryName does not contain
        defaultAssetSubCategoryFiltering(
            "assetSubCategoryName.doesNotContain=" + UPDATED_ASSET_SUB_CATEGORY_NAME,
            "assetSubCategoryName.doesNotContain=" + DEFAULT_ASSET_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where isActive equals to
        defaultAssetSubCategoryFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where isActive in
        defaultAssetSubCategoryFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        // Get all the assetSubCategoryList where isActive is not null
        defaultAssetSubCategoryFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetSubCategoriesByCategoryIsEqualToSomething() throws Exception {
        AssetCategory category;
        if (TestUtil.findAll(em, AssetCategory.class).isEmpty()) {
            assetSubCategoryRepository.saveAndFlush(assetSubCategory);
            category = AssetCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, AssetCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        assetSubCategory.setCategory(category);
        assetSubCategoryRepository.saveAndFlush(assetSubCategory);
        Long categoryId = category.getId();
        // Get all the assetSubCategoryList where category equals to categoryId
        defaultAssetSubCategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the assetSubCategoryList where category equals to (categoryId + 1)
        defaultAssetSubCategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultAssetSubCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAssetSubCategoryShouldBeFound(shouldBeFound);
        defaultAssetSubCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetSubCategoryShouldBeFound(String filter) throws Exception {
        restAssetSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryCode").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryName").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restAssetSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetSubCategoryShouldNotBeFound(String filter) throws Exception {
        restAssetSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetSubCategory() throws Exception {
        // Get the assetSubCategory
        restAssetSubCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssetSubCategory() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        assetSubCategorySearchRepository.save(assetSubCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());

        // Update the assetSubCategory
        AssetSubCategory updatedAssetSubCategory = assetSubCategoryRepository.findById(assetSubCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAssetSubCategory are not directly saved in db
        em.detach(updatedAssetSubCategory);
        updatedAssetSubCategory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(UPDATED_ASSET_SUB_CATEGORY_CODE)
            .assetSubCategoryName(UPDATED_ASSET_SUB_CATEGORY_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(updatedAssetSubCategory);

        restAssetSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetSubCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetSubCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssetSubCategoryToMatchAllProperties(updatedAssetSubCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AssetSubCategory> assetSubCategorySearchList = Streamable.of(assetSubCategorySearchRepository.findAll()).toList();
                AssetSubCategory testAssetSubCategorySearch = assetSubCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertAssetSubCategoryAllPropertiesEquals(testAssetSubCategorySearch, updatedAssetSubCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingAssetSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assetSubCategory.setId(longCount.incrementAndGet());

        // Create the AssetSubCategory
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetSubCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assetSubCategory.setId(longCount.incrementAndGet());

        // Create the AssetSubCategory
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assetSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assetSubCategory.setId(longCount.incrementAndGet());

        // Create the AssetSubCategory
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetSubCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assetSubCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAssetSubCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetSubCategory using partial update
        AssetSubCategory partialUpdatedAssetSubCategory = new AssetSubCategory();
        partialUpdatedAssetSubCategory.setId(assetSubCategory.getId());

        partialUpdatedAssetSubCategory.branchCode(UPDATED_BRANCH_CODE);

        restAssetSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetSubCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetSubCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssetSubCategory, assetSubCategory),
            getPersistedAssetSubCategory(assetSubCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssetSubCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assetSubCategory using partial update
        AssetSubCategory partialUpdatedAssetSubCategory = new AssetSubCategory();
        partialUpdatedAssetSubCategory.setId(assetSubCategory.getId());

        partialUpdatedAssetSubCategory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .assetCategoryCode(UPDATED_ASSET_CATEGORY_CODE)
            .assetSubCategoryCode(UPDATED_ASSET_SUB_CATEGORY_CODE)
            .assetSubCategoryName(UPDATED_ASSET_SUB_CATEGORY_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restAssetSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssetSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetSubCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssetSubCategoryUpdatableFieldsEquals(
            partialUpdatedAssetSubCategory,
            getPersistedAssetSubCategory(partialUpdatedAssetSubCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAssetSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assetSubCategory.setId(longCount.incrementAndGet());

        // Create the AssetSubCategory
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetSubCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assetSubCategory.setId(longCount.incrementAndGet());

        // Create the AssetSubCategory
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assetSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assetSubCategory.setId(longCount.incrementAndGet());

        // Create the AssetSubCategory
        AssetSubCategoryDTO assetSubCategoryDTO = assetSubCategoryMapper.toDto(assetSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetSubCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assetSubCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAssetSubCategory() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);
        assetSubCategoryRepository.save(assetSubCategory);
        assetSubCategorySearchRepository.save(assetSubCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assetSubCategory
        restAssetSubCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetSubCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAssetSubCategory() throws Exception {
        // Initialize the database
        insertedAssetSubCategory = assetSubCategoryRepository.saveAndFlush(assetSubCategory);
        assetSubCategorySearchRepository.save(assetSubCategory);

        // Search the assetSubCategory
        restAssetSubCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetSubCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].assetCategoryCode").value(hasItem(DEFAULT_ASSET_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryCode").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].assetSubCategoryName").value(hasItem(DEFAULT_ASSET_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    protected long getRepositoryCount() {
        return assetSubCategoryRepository.count();
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

    protected AssetSubCategory getPersistedAssetSubCategory(AssetSubCategory assetSubCategory) {
        return assetSubCategoryRepository.findById(assetSubCategory.getId()).orElseThrow();
    }

    protected void assertPersistedAssetSubCategoryToMatchAllProperties(AssetSubCategory expectedAssetSubCategory) {
        assertAssetSubCategoryAllPropertiesEquals(expectedAssetSubCategory, getPersistedAssetSubCategory(expectedAssetSubCategory));
    }

    protected void assertPersistedAssetSubCategoryToMatchUpdatableProperties(AssetSubCategory expectedAssetSubCategory) {
        assertAssetSubCategoryAllUpdatablePropertiesEquals(
            expectedAssetSubCategory,
            getPersistedAssetSubCategory(expectedAssetSubCategory)
        );
    }
}
