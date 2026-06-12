package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.ExpenseCategoryAsserts.*;
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
import com.gvsolutions.repository.ExpenseCategoryRepository;
import com.gvsolutions.repository.search.ExpenseCategorySearchRepository;
import com.gvsolutions.service.dto.ExpenseCategoryDTO;
import com.gvsolutions.service.mapper.ExpenseCategoryMapper;
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
 * Integration tests for the {@link ExpenseCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/expense-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/expense-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private ExpenseCategoryMapper expenseCategoryMapper;

    @Autowired
    private ExpenseCategorySearchRepository expenseCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseCategoryMockMvc;

    private ExpenseCategory expenseCategory;

    private ExpenseCategory insertedExpenseCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseCategory createEntity() {
        return new ExpenseCategory()
            .categoryCode(DEFAULT_CATEGORY_CODE)
            .categoryName(DEFAULT_CATEGORY_NAME)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseCategory createUpdatedEntity() {
        return new ExpenseCategory()
            .categoryCode(UPDATED_CATEGORY_CODE)
            .categoryName(UPDATED_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        expenseCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExpenseCategory != null) {
            expenseCategoryRepository.delete(insertedExpenseCategory);
            expenseCategorySearchRepository.delete(insertedExpenseCategory);
            insertedExpenseCategory = null;
        }
    }

    @Test
    @Transactional
    void createExpenseCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        // Create the ExpenseCategory
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);
        var returnedExpenseCategoryDTO = om.readValue(
            restExpenseCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExpenseCategoryDTO.class
        );

        // Validate the ExpenseCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExpenseCategory = expenseCategoryMapper.toEntity(returnedExpenseCategoryDTO);
        assertExpenseCategoryUpdatableFieldsEquals(returnedExpenseCategory, getPersistedExpenseCategory(returnedExpenseCategory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedExpenseCategory = returnedExpenseCategory;
    }

    @Test
    @Transactional
    void createExpenseCategoryWithExistingId() throws Exception {
        // Create the ExpenseCategory with an existing ID
        expenseCategory.setId(1L);
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllExpenseCategories() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList
        restExpenseCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getExpenseCategory() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get the expenseCategory
        restExpenseCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryCode").value(DEFAULT_CATEGORY_CODE))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getExpenseCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        Long id = expenseCategory.getId();

        defaultExpenseCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExpenseCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExpenseCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryCode equals to
        defaultExpenseCategoryFiltering("categoryCode.equals=" + DEFAULT_CATEGORY_CODE, "categoryCode.equals=" + UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryCode in
        defaultExpenseCategoryFiltering(
            "categoryCode.in=" + DEFAULT_CATEGORY_CODE + "," + UPDATED_CATEGORY_CODE,
            "categoryCode.in=" + UPDATED_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryCode is not null
        defaultExpenseCategoryFiltering("categoryCode.specified=true", "categoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryCode contains
        defaultExpenseCategoryFiltering("categoryCode.contains=" + DEFAULT_CATEGORY_CODE, "categoryCode.contains=" + UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryCode does not contain
        defaultExpenseCategoryFiltering(
            "categoryCode.doesNotContain=" + UPDATED_CATEGORY_CODE,
            "categoryCode.doesNotContain=" + DEFAULT_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryName equals to
        defaultExpenseCategoryFiltering("categoryName.equals=" + DEFAULT_CATEGORY_NAME, "categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryName in
        defaultExpenseCategoryFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryName is not null
        defaultExpenseCategoryFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryName contains
        defaultExpenseCategoryFiltering("categoryName.contains=" + DEFAULT_CATEGORY_NAME, "categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where categoryName does not contain
        defaultExpenseCategoryFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where description equals to
        defaultExpenseCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where description in
        defaultExpenseCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where description is not null
        defaultExpenseCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where description contains
        defaultExpenseCategoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExpenseCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        // Get all the expenseCategoryList where description does not contain
        defaultExpenseCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultExpenseCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExpenseCategoryShouldBeFound(shouldBeFound);
        defaultExpenseCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseCategoryShouldBeFound(String filter) throws Exception {
        restExpenseCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restExpenseCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseCategoryShouldNotBeFound(String filter) throws Exception {
        restExpenseCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpenseCategory() throws Exception {
        // Get the expenseCategory
        restExpenseCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseCategory() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseCategorySearchRepository.save(expenseCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());

        // Update the expenseCategory
        ExpenseCategory updatedExpenseCategory = expenseCategoryRepository.findById(expenseCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpenseCategory are not directly saved in db
        em.detach(updatedExpenseCategory);
        updatedExpenseCategory.categoryCode(UPDATED_CATEGORY_CODE).categoryName(UPDATED_CATEGORY_NAME).description(UPDATED_DESCRIPTION);
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(updatedExpenseCategory);

        restExpenseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpenseCategoryToMatchAllProperties(updatedExpenseCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ExpenseCategory> expenseCategorySearchList = Streamable.of(expenseCategorySearchRepository.findAll()).toList();
                ExpenseCategory testExpenseCategorySearch = expenseCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertExpenseCategoryAllPropertiesEquals(testExpenseCategorySearch, updatedExpenseCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingExpenseCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        expenseCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseCategory
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        expenseCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseCategory
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        expenseCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseCategory
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateExpenseCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseCategory using partial update
        ExpenseCategory partialUpdatedExpenseCategory = new ExpenseCategory();
        partialUpdatedExpenseCategory.setId(expenseCategory.getId());

        partialUpdatedExpenseCategory.categoryCode(UPDATED_CATEGORY_CODE).description(UPDATED_DESCRIPTION);

        restExpenseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExpenseCategory, expenseCategory),
            getPersistedExpenseCategory(expenseCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateExpenseCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseCategory using partial update
        ExpenseCategory partialUpdatedExpenseCategory = new ExpenseCategory();
        partialUpdatedExpenseCategory.setId(expenseCategory.getId());

        partialUpdatedExpenseCategory
            .categoryCode(UPDATED_CATEGORY_CODE)
            .categoryName(UPDATED_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION);

        restExpenseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseCategoryUpdatableFieldsEquals(
            partialUpdatedExpenseCategory,
            getPersistedExpenseCategory(partialUpdatedExpenseCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExpenseCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        expenseCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseCategory
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        expenseCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseCategory
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        expenseCategory.setId(longCount.incrementAndGet());

        // Create the ExpenseCategory
        ExpenseCategoryDTO expenseCategoryDTO = expenseCategoryMapper.toDto(expenseCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expenseCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteExpenseCategory() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);
        expenseCategoryRepository.save(expenseCategory);
        expenseCategorySearchRepository.save(expenseCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the expenseCategory
        restExpenseCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(expenseCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchExpenseCategory() throws Exception {
        // Initialize the database
        insertedExpenseCategory = expenseCategoryRepository.saveAndFlush(expenseCategory);
        expenseCategorySearchRepository.save(expenseCategory);

        // Search the expenseCategory
        restExpenseCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + expenseCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    protected long getRepositoryCount() {
        return expenseCategoryRepository.count();
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

    protected ExpenseCategory getPersistedExpenseCategory(ExpenseCategory expenseCategory) {
        return expenseCategoryRepository.findById(expenseCategory.getId()).orElseThrow();
    }

    protected void assertPersistedExpenseCategoryToMatchAllProperties(ExpenseCategory expectedExpenseCategory) {
        assertExpenseCategoryAllPropertiesEquals(expectedExpenseCategory, getPersistedExpenseCategory(expectedExpenseCategory));
    }

    protected void assertPersistedExpenseCategoryToMatchUpdatableProperties(ExpenseCategory expectedExpenseCategory) {
        assertExpenseCategoryAllUpdatablePropertiesEquals(expectedExpenseCategory, getPersistedExpenseCategory(expectedExpenseCategory));
    }
}
