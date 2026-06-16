package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.ExpenseSubCategoryAsserts.*;
import static com.gvsolutions.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gvsolutions.IntegrationTest;
import com.gvsolutions.domain.ExpenseCategory;
import com.gvsolutions.domain.ExpenseSubCategory;
import com.gvsolutions.repository.ExpenseSubCategoryRepository;
import com.gvsolutions.repository.search.ExpenseSubCategorySearchRepository;
import com.gvsolutions.service.ExpenseSubCategoryService;
import com.gvsolutions.service.dto.ExpenseSubCategoryDTO;
import com.gvsolutions.service.mapper.ExpenseSubCategoryMapper;
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
 * Integration tests for the {@link ExpenseSubCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExpenseSubCategoryResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/expense-sub-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/expense-sub-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpenseSubCategoryRepository expenseSubCategoryRepository;

    @Mock
    private ExpenseSubCategoryRepository expenseSubCategoryRepositoryMock;

    @Autowired
    private ExpenseSubCategoryMapper expenseSubCategoryMapper;

    @Mock
    private ExpenseSubCategoryService expenseSubCategoryServiceMock;

    @Autowired
    private ExpenseSubCategorySearchRepository expenseSubCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseSubCategoryMockMvc;

    private ExpenseSubCategory expenseSubCategory;

    private ExpenseSubCategory insertedExpenseSubCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseSubCategory createEntity() {
        return new ExpenseSubCategory()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .categoryCode(DEFAULT_CATEGORY_CODE)
            .subCategoryCode(DEFAULT_SUB_CATEGORY_CODE)
            .subCategoryName(DEFAULT_SUB_CATEGORY_NAME)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseSubCategory createUpdatedEntity() {
        return new ExpenseSubCategory()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .categoryCode(UPDATED_CATEGORY_CODE)
            .subCategoryCode(UPDATED_SUB_CATEGORY_CODE)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        expenseSubCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExpenseSubCategory != null) {
            expenseSubCategoryRepository.delete(insertedExpenseSubCategory);
            expenseSubCategorySearchRepository.delete(insertedExpenseSubCategory);
            insertedExpenseSubCategory = null;
        }
    }

    @Test
    @Transactional
    void createExpenseSubCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        // Create the ExpenseSubCategory
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);
        var returnedExpenseSubCategoryDTO = om.readValue(
            restExpenseSubCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseSubCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExpenseSubCategoryDTO.class
        );

        // Validate the ExpenseSubCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExpenseSubCategory = expenseSubCategoryMapper.toEntity(returnedExpenseSubCategoryDTO);
        assertExpenseSubCategoryUpdatableFieldsEquals(
            returnedExpenseSubCategory,
            getPersistedExpenseSubCategory(returnedExpenseSubCategory)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedExpenseSubCategory = returnedExpenseSubCategory;
    }

    @Test
    @Transactional
    void createExpenseSubCategoryWithExistingId() throws Exception {
        // Create the ExpenseSubCategory with an existing ID
        expenseSubCategory.setId(1L);
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseSubCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseSubCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategories() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList
        restExpenseSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryCode").value(hasItem(DEFAULT_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryName").value(hasItem(DEFAULT_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExpenseSubCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(expenseSubCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExpenseSubCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(expenseSubCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExpenseSubCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(expenseSubCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExpenseSubCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(expenseSubCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExpenseSubCategory() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get the expenseSubCategory
        restExpenseSubCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseSubCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseSubCategory.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.categoryCode").value(DEFAULT_CATEGORY_CODE))
            .andExpect(jsonPath("$.subCategoryCode").value(DEFAULT_SUB_CATEGORY_CODE))
            .andExpect(jsonPath("$.subCategoryName").value(DEFAULT_SUB_CATEGORY_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getExpenseSubCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        Long id = expenseSubCategory.getId();

        defaultExpenseSubCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExpenseSubCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExpenseSubCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchCode equals to
        defaultExpenseSubCategoryFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchCode in
        defaultExpenseSubCategoryFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchCode is not null
        defaultExpenseSubCategoryFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchCode contains
        defaultExpenseSubCategoryFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchCode does not contain
        defaultExpenseSubCategoryFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchId equals to
        defaultExpenseSubCategoryFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchId in
        defaultExpenseSubCategoryFiltering(
            "branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID,
            "branchId.in=" + UPDATED_BRANCH_ID
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchId is not null
        defaultExpenseSubCategoryFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchId contains
        defaultExpenseSubCategoryFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where branchId does not contain
        defaultExpenseSubCategoryFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where categoryCode equals to
        defaultExpenseSubCategoryFiltering("categoryCode.equals=" + DEFAULT_CATEGORY_CODE, "categoryCode.equals=" + UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where categoryCode in
        defaultExpenseSubCategoryFiltering(
            "categoryCode.in=" + DEFAULT_CATEGORY_CODE + "," + UPDATED_CATEGORY_CODE,
            "categoryCode.in=" + UPDATED_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where categoryCode is not null
        defaultExpenseSubCategoryFiltering("categoryCode.specified=true", "categoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where categoryCode contains
        defaultExpenseSubCategoryFiltering(
            "categoryCode.contains=" + DEFAULT_CATEGORY_CODE,
            "categoryCode.contains=" + UPDATED_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where categoryCode does not contain
        defaultExpenseSubCategoryFiltering(
            "categoryCode.doesNotContain=" + UPDATED_CATEGORY_CODE,
            "categoryCode.doesNotContain=" + DEFAULT_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryCode equals to
        defaultExpenseSubCategoryFiltering(
            "subCategoryCode.equals=" + DEFAULT_SUB_CATEGORY_CODE,
            "subCategoryCode.equals=" + UPDATED_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryCode in
        defaultExpenseSubCategoryFiltering(
            "subCategoryCode.in=" + DEFAULT_SUB_CATEGORY_CODE + "," + UPDATED_SUB_CATEGORY_CODE,
            "subCategoryCode.in=" + UPDATED_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryCode is not null
        defaultExpenseSubCategoryFiltering("subCategoryCode.specified=true", "subCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryCode contains
        defaultExpenseSubCategoryFiltering(
            "subCategoryCode.contains=" + DEFAULT_SUB_CATEGORY_CODE,
            "subCategoryCode.contains=" + UPDATED_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryCode does not contain
        defaultExpenseSubCategoryFiltering(
            "subCategoryCode.doesNotContain=" + UPDATED_SUB_CATEGORY_CODE,
            "subCategoryCode.doesNotContain=" + DEFAULT_SUB_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryName equals to
        defaultExpenseSubCategoryFiltering(
            "subCategoryName.equals=" + DEFAULT_SUB_CATEGORY_NAME,
            "subCategoryName.equals=" + UPDATED_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryName in
        defaultExpenseSubCategoryFiltering(
            "subCategoryName.in=" + DEFAULT_SUB_CATEGORY_NAME + "," + UPDATED_SUB_CATEGORY_NAME,
            "subCategoryName.in=" + UPDATED_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryName is not null
        defaultExpenseSubCategoryFiltering("subCategoryName.specified=true", "subCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryName contains
        defaultExpenseSubCategoryFiltering(
            "subCategoryName.contains=" + DEFAULT_SUB_CATEGORY_NAME,
            "subCategoryName.contains=" + UPDATED_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesBySubCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where subCategoryName does not contain
        defaultExpenseSubCategoryFiltering(
            "subCategoryName.doesNotContain=" + UPDATED_SUB_CATEGORY_NAME,
            "subCategoryName.doesNotContain=" + DEFAULT_SUB_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where isActive equals to
        defaultExpenseSubCategoryFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where isActive in
        defaultExpenseSubCategoryFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        // Get all the expenseSubCategoryList where isActive is not null
        defaultExpenseSubCategoryFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseSubCategoriesByCategoryIsEqualToSomething() throws Exception {
        ExpenseCategory category;
        if (TestUtil.findAll(em, ExpenseCategory.class).isEmpty()) {
            expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);
            category = ExpenseCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, ExpenseCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        expenseSubCategory.setCategory(category);
        expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);
        Long categoryId = category.getId();
        // Get all the expenseSubCategoryList where category equals to categoryId
        defaultExpenseSubCategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the expenseSubCategoryList where category equals to (categoryId + 1)
        defaultExpenseSubCategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultExpenseSubCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExpenseSubCategoryShouldBeFound(shouldBeFound);
        defaultExpenseSubCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseSubCategoryShouldBeFound(String filter) throws Exception {
        restExpenseSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryCode").value(hasItem(DEFAULT_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryName").value(hasItem(DEFAULT_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restExpenseSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseSubCategoryShouldNotBeFound(String filter) throws Exception {
        restExpenseSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpenseSubCategory() throws Exception {
        // Get the expenseSubCategory
        restExpenseSubCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseSubCategory() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseSubCategorySearchRepository.save(expenseSubCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());

        // Update the expenseSubCategory
        ExpenseSubCategory updatedExpenseSubCategory = expenseSubCategoryRepository.findById(expenseSubCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpenseSubCategory are not directly saved in db
        em.detach(updatedExpenseSubCategory);
        updatedExpenseSubCategory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .categoryCode(UPDATED_CATEGORY_CODE)
            .subCategoryCode(UPDATED_SUB_CATEGORY_CODE)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .isActive(UPDATED_IS_ACTIVE);
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(updatedExpenseSubCategory);

        restExpenseSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseSubCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseSubCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpenseSubCategoryToMatchAllProperties(updatedExpenseSubCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ExpenseSubCategory> expenseSubCategorySearchList = Streamable.of(
                    expenseSubCategorySearchRepository.findAll()
                ).toList();
                ExpenseSubCategory testExpenseSubCategorySearch = expenseSubCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertExpenseSubCategoryAllPropertiesEquals(testExpenseSubCategorySearch, updatedExpenseSubCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingExpenseSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        expenseSubCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseSubCategory
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseSubCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        expenseSubCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseSubCategory
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        expenseSubCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseSubCategory
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseSubCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseSubCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateExpenseSubCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseSubCategory using partial update
        ExpenseSubCategory partialUpdatedExpenseSubCategory = new ExpenseSubCategory();
        partialUpdatedExpenseSubCategory.setId(expenseSubCategory.getId());

        partialUpdatedExpenseSubCategory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .subCategoryCode(UPDATED_SUB_CATEGORY_CODE)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restExpenseSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseSubCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseSubCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExpenseSubCategory, expenseSubCategory),
            getPersistedExpenseSubCategory(expenseSubCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateExpenseSubCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseSubCategory using partial update
        ExpenseSubCategory partialUpdatedExpenseSubCategory = new ExpenseSubCategory();
        partialUpdatedExpenseSubCategory.setId(expenseSubCategory.getId());

        partialUpdatedExpenseSubCategory
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .categoryCode(UPDATED_CATEGORY_CODE)
            .subCategoryCode(UPDATED_SUB_CATEGORY_CODE)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .isActive(UPDATED_IS_ACTIVE);

        restExpenseSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseSubCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseSubCategoryUpdatableFieldsEquals(
            partialUpdatedExpenseSubCategory,
            getPersistedExpenseSubCategory(partialUpdatedExpenseSubCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExpenseSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        expenseSubCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseSubCategory
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseSubCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        expenseSubCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseSubCategory
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseSubCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        expenseSubCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseSubCategory
        ExpenseSubCategoryDTO expenseSubCategoryDTO = expenseSubCategoryMapper.toDto(expenseSubCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseSubCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expenseSubCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseSubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteExpenseSubCategory() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);
        expenseSubCategoryRepository.save(expenseSubCategory);
        expenseSubCategorySearchRepository.save(expenseSubCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the expenseSubCategory
        restExpenseSubCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseSubCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseSubCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchExpenseSubCategory() throws Exception {
        // Initialize the database
        insertedExpenseSubCategory = expenseSubCategoryRepository.saveAndFlush(expenseSubCategory);
        expenseSubCategorySearchRepository.save(expenseSubCategory);

        // Search the expenseSubCategory
        restExpenseSubCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + expenseSubCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryCode").value(hasItem(DEFAULT_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryName").value(hasItem(DEFAULT_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    protected long getRepositoryCount() {
        return expenseSubCategoryRepository.count();
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

    protected ExpenseSubCategory getPersistedExpenseSubCategory(ExpenseSubCategory expenseSubCategory) {
        return expenseSubCategoryRepository.findById(expenseSubCategory.getId()).orElseThrow();
    }

    protected void assertPersistedExpenseSubCategoryToMatchAllProperties(ExpenseSubCategory expectedExpenseSubCategory) {
        assertExpenseSubCategoryAllPropertiesEquals(expectedExpenseSubCategory, getPersistedExpenseSubCategory(expectedExpenseSubCategory));
    }

    protected void assertPersistedExpenseSubCategoryToMatchUpdatableProperties(ExpenseSubCategory expectedExpenseSubCategory) {
        assertExpenseSubCategoryAllUpdatablePropertiesEquals(
            expectedExpenseSubCategory,
            getPersistedExpenseSubCategory(expectedExpenseSubCategory)
        );
    }
}
