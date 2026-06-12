package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.AccountSetAsserts.*;
import static com.gvsolutions.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gvsolutions.IntegrationTest;
import com.gvsolutions.domain.AccountSet;
import com.gvsolutions.domain.enumeration.AccountType;
import com.gvsolutions.repository.AccountSetRepository;
import com.gvsolutions.repository.search.AccountSetSearchRepository;
import com.gvsolutions.service.dto.AccountSetDTO;
import com.gvsolutions.service.mapper.AccountSetMapper;
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
 * Integration tests for the {@link AccountSetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountSetResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.ASSET;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.INCOME;

    private static final String DEFAULT_SUB_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/account-sets/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountSetRepository accountSetRepository;

    @Autowired
    private AccountSetMapper accountSetMapper;

    @Autowired
    private AccountSetSearchRepository accountSetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountSetMockMvc;

    private AccountSet accountSet;

    private AccountSet insertedAccountSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountSet createEntity() {
        return new AccountSet()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .accountCode(DEFAULT_ACCOUNT_CODE)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .subCategory(DEFAULT_SUB_CATEGORY)
            .remark(DEFAULT_REMARK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountSet createUpdatedEntity() {
        return new AccountSet()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .subCategory(UPDATED_SUB_CATEGORY)
            .remark(UPDATED_REMARK);
    }

    @BeforeEach
    void initTest() {
        accountSet = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAccountSet != null) {
            accountSetRepository.delete(insertedAccountSet);
            accountSetSearchRepository.delete(insertedAccountSet);
            insertedAccountSet = null;
        }
    }

    @Test
    @Transactional
    void createAccountSet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        // Create the AccountSet
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);
        var returnedAccountSetDTO = om.readValue(
            restAccountSetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountSetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountSetDTO.class
        );

        // Validate the AccountSet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAccountSet = accountSetMapper.toEntity(returnedAccountSetDTO);
        assertAccountSetUpdatableFieldsEquals(returnedAccountSet, getPersistedAccountSet(returnedAccountSet));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccountSet = returnedAccountSet;
    }

    @Test
    @Transactional
    void createAccountSetWithExistingId() throws Exception {
        // Create the AccountSet with an existing ID
        accountSet.setId(1L);
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountSetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccountSets() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList
        restAccountSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subCategory").value(hasItem(DEFAULT_SUB_CATEGORY)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    @Test
    @Transactional
    void getAccountSet() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get the accountSet
        restAccountSetMockMvc
            .perform(get(ENTITY_API_URL_ID, accountSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountSet.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.accountCode").value(DEFAULT_ACCOUNT_CODE))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.subCategory").value(DEFAULT_SUB_CATEGORY))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK));
    }

    @Test
    @Transactional
    void getAccountSetsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        Long id = accountSet.getId();

        defaultAccountSetFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAccountSetFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAccountSetFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchCode equals to
        defaultAccountSetFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchCode in
        defaultAccountSetFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchCode is not null
        defaultAccountSetFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchCode contains
        defaultAccountSetFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchCode does not contain
        defaultAccountSetFiltering("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE, "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchId equals to
        defaultAccountSetFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchId in
        defaultAccountSetFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchId is not null
        defaultAccountSetFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchId contains
        defaultAccountSetFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAccountSetsByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where branchId does not contain
        defaultAccountSetFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountCode equals to
        defaultAccountSetFiltering("accountCode.equals=" + DEFAULT_ACCOUNT_CODE, "accountCode.equals=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountCode in
        defaultAccountSetFiltering(
            "accountCode.in=" + DEFAULT_ACCOUNT_CODE + "," + UPDATED_ACCOUNT_CODE,
            "accountCode.in=" + UPDATED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountCode is not null
        defaultAccountSetFiltering("accountCode.specified=true", "accountCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountCode contains
        defaultAccountSetFiltering("accountCode.contains=" + DEFAULT_ACCOUNT_CODE, "accountCode.contains=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountCode does not contain
        defaultAccountSetFiltering(
            "accountCode.doesNotContain=" + UPDATED_ACCOUNT_CODE,
            "accountCode.doesNotContain=" + DEFAULT_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountName equals to
        defaultAccountSetFiltering("accountName.equals=" + DEFAULT_ACCOUNT_NAME, "accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountName in
        defaultAccountSetFiltering(
            "accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME,
            "accountName.in=" + UPDATED_ACCOUNT_NAME
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountName is not null
        defaultAccountSetFiltering("accountName.specified=true", "accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountName contains
        defaultAccountSetFiltering("accountName.contains=" + DEFAULT_ACCOUNT_NAME, "accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountName does not contain
        defaultAccountSetFiltering(
            "accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME,
            "accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountType equals to
        defaultAccountSetFiltering("accountType.equals=" + DEFAULT_ACCOUNT_TYPE, "accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountType in
        defaultAccountSetFiltering(
            "accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE,
            "accountType.in=" + UPDATED_ACCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsByAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where accountType is not null
        defaultAccountSetFiltering("accountType.specified=true", "accountType.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountSetsBySubCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where subCategory equals to
        defaultAccountSetFiltering("subCategory.equals=" + DEFAULT_SUB_CATEGORY, "subCategory.equals=" + UPDATED_SUB_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAccountSetsBySubCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where subCategory in
        defaultAccountSetFiltering(
            "subCategory.in=" + DEFAULT_SUB_CATEGORY + "," + UPDATED_SUB_CATEGORY,
            "subCategory.in=" + UPDATED_SUB_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsBySubCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where subCategory is not null
        defaultAccountSetFiltering("subCategory.specified=true", "subCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountSetsBySubCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where subCategory contains
        defaultAccountSetFiltering("subCategory.contains=" + DEFAULT_SUB_CATEGORY, "subCategory.contains=" + UPDATED_SUB_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAccountSetsBySubCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where subCategory does not contain
        defaultAccountSetFiltering(
            "subCategory.doesNotContain=" + UPDATED_SUB_CATEGORY,
            "subCategory.doesNotContain=" + DEFAULT_SUB_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllAccountSetsByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where remark equals to
        defaultAccountSetFiltering("remark.equals=" + DEFAULT_REMARK, "remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllAccountSetsByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where remark in
        defaultAccountSetFiltering("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK, "remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllAccountSetsByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where remark is not null
        defaultAccountSetFiltering("remark.specified=true", "remark.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountSetsByRemarkContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where remark contains
        defaultAccountSetFiltering("remark.contains=" + DEFAULT_REMARK, "remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllAccountSetsByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        // Get all the accountSetList where remark does not contain
        defaultAccountSetFiltering("remark.doesNotContain=" + UPDATED_REMARK, "remark.doesNotContain=" + DEFAULT_REMARK);
    }

    private void defaultAccountSetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAccountSetShouldBeFound(shouldBeFound);
        defaultAccountSetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountSetShouldBeFound(String filter) throws Exception {
        restAccountSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subCategory").value(hasItem(DEFAULT_SUB_CATEGORY)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));

        // Check, that the count call also returns 1
        restAccountSetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountSetShouldNotBeFound(String filter) throws Exception {
        restAccountSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountSetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountSet() throws Exception {
        // Get the accountSet
        restAccountSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountSet() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountSetSearchRepository.save(accountSet);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());

        // Update the accountSet
        AccountSet updatedAccountSet = accountSetRepository.findById(accountSet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccountSet are not directly saved in db
        em.detach(updatedAccountSet);
        updatedAccountSet
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .subCategory(UPDATED_SUB_CATEGORY)
            .remark(UPDATED_REMARK);
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(updatedAccountSet);

        restAccountSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountSetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountSetDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountSetToMatchAllProperties(updatedAccountSet);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccountSet> accountSetSearchList = Streamable.of(accountSetSearchRepository.findAll()).toList();
                AccountSet testAccountSetSearch = accountSetSearchList.get(searchDatabaseSizeAfter - 1);

                assertAccountSetAllPropertiesEquals(testAccountSetSearch, updatedAccountSet);
            });
    }

    @Test
    @Transactional
    void putNonExistingAccountSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        accountSet.setId(longCount.incrementAndGet());

        // Create the AccountSet
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountSetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        accountSet.setId(longCount.incrementAndGet());

        // Create the AccountSet
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        accountSet.setId(longCount.incrementAndGet());

        // Create the AccountSet
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountSetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountSetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccountSetWithPatch() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountSet using partial update
        AccountSet partialUpdatedAccountSet = new AccountSet();
        partialUpdatedAccountSet.setId(accountSet.getId());

        partialUpdatedAccountSet.accountType(UPDATED_ACCOUNT_TYPE).subCategory(UPDATED_SUB_CATEGORY);

        restAccountSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountSet))
            )
            .andExpect(status().isOk());

        // Validate the AccountSet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountSetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccountSet, accountSet),
            getPersistedAccountSet(accountSet)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccountSetWithPatch() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountSet using partial update
        AccountSet partialUpdatedAccountSet = new AccountSet();
        partialUpdatedAccountSet.setId(accountSet.getId());

        partialUpdatedAccountSet
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .subCategory(UPDATED_SUB_CATEGORY)
            .remark(UPDATED_REMARK);

        restAccountSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountSet))
            )
            .andExpect(status().isOk());

        // Validate the AccountSet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountSetUpdatableFieldsEquals(partialUpdatedAccountSet, getPersistedAccountSet(partialUpdatedAccountSet));
    }

    @Test
    @Transactional
    void patchNonExistingAccountSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        accountSet.setId(longCount.incrementAndGet());

        // Create the AccountSet
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountSetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        accountSet.setId(longCount.incrementAndGet());

        // Create the AccountSet
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        accountSet.setId(longCount.incrementAndGet());

        // Create the AccountSet
        AccountSetDTO accountSetDTO = accountSetMapper.toDto(accountSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountSetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accountSetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccountSet() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);
        accountSetRepository.save(accountSet);
        accountSetSearchRepository.save(accountSet);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accountSet
        restAccountSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountSet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountSetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccountSet() throws Exception {
        // Initialize the database
        insertedAccountSet = accountSetRepository.saveAndFlush(accountSet);
        accountSetSearchRepository.save(accountSet);

        // Search the accountSet
        restAccountSetMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subCategory").value(hasItem(DEFAULT_SUB_CATEGORY)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    protected long getRepositoryCount() {
        return accountSetRepository.count();
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

    protected AccountSet getPersistedAccountSet(AccountSet accountSet) {
        return accountSetRepository.findById(accountSet.getId()).orElseThrow();
    }

    protected void assertPersistedAccountSetToMatchAllProperties(AccountSet expectedAccountSet) {
        assertAccountSetAllPropertiesEquals(expectedAccountSet, getPersistedAccountSet(expectedAccountSet));
    }

    protected void assertPersistedAccountSetToMatchUpdatableProperties(AccountSet expectedAccountSet) {
        assertAccountSetAllUpdatablePropertiesEquals(expectedAccountSet, getPersistedAccountSet(expectedAccountSet));
    }
}
