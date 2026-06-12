package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.BranchAsserts.*;
import static com.gvsolutions.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gvsolutions.IntegrationTest;
import com.gvsolutions.domain.Branch;
import com.gvsolutions.repository.BranchRepository;
import com.gvsolutions.repository.search.BranchSearchRepository;
import com.gvsolutions.service.dto.BranchDTO;
import com.gvsolutions.service.mapper.BranchMapper;
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
 * Integration tests for the {@link BranchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/branches/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private BranchSearchRepository branchSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchMockMvc;

    private Branch branch;

    private Branch insertedBranch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createEntity() {
        return new Branch()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchName(DEFAULT_BRANCH_NAME)
            .location(DEFAULT_LOCATION)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createUpdatedEntity() {
        return new Branch()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .location(UPDATED_LOCATION)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        branch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBranch != null) {
            branchRepository.delete(insertedBranch);
            branchSearchRepository.delete(insertedBranch);
            insertedBranch = null;
        }
    }

    @Test
    @Transactional
    void createBranch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);
        var returnedBranchDTO = om.readValue(
            restBranchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BranchDTO.class
        );

        // Validate the Branch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBranch = branchMapper.toEntity(returnedBranchDTO);
        assertBranchUpdatableFieldsEquals(returnedBranch, getPersistedBranch(returnedBranch));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedBranch = returnedBranch;
    }

    @Test
    @Transactional
    void createBranchWithExistingId() throws Exception {
        // Create the Branch with an existing ID
        branch.setId(1L);
        BranchDTO branchDTO = branchMapper.toDto(branch);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllBranches() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getBranch() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getBranchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        Long id = branch.getId();

        defaultBranchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBranchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBranchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode equals to
        defaultBranchFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode in
        defaultBranchFiltering("branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE, "branchCode.in=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode is not null
        defaultBranchFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode contains
        defaultBranchFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchCode does not contain
        defaultBranchFiltering("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE, "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName equals to
        defaultBranchFiltering("branchName.equals=" + DEFAULT_BRANCH_NAME, "branchName.equals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName in
        defaultBranchFiltering("branchName.in=" + DEFAULT_BRANCH_NAME + "," + UPDATED_BRANCH_NAME, "branchName.in=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName is not null
        defaultBranchFiltering("branchName.specified=true", "branchName.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByBranchNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName contains
        defaultBranchFiltering("branchName.contains=" + DEFAULT_BRANCH_NAME, "branchName.contains=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName does not contain
        defaultBranchFiltering("branchName.doesNotContain=" + UPDATED_BRANCH_NAME, "branchName.doesNotContain=" + DEFAULT_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBranchesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where location equals to
        defaultBranchFiltering("location.equals=" + DEFAULT_LOCATION, "location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllBranchesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where location in
        defaultBranchFiltering("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION, "location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllBranchesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where location is not null
        defaultBranchFiltering("location.specified=true", "location.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where location contains
        defaultBranchFiltering("location.contains=" + DEFAULT_LOCATION, "location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllBranchesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where location does not contain
        defaultBranchFiltering("location.doesNotContain=" + UPDATED_LOCATION, "location.doesNotContain=" + DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void getAllBranchesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where phoneNumber equals to
        defaultBranchFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllBranchesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where phoneNumber in
        defaultBranchFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBranchesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where phoneNumber is not null
        defaultBranchFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where phoneNumber contains
        defaultBranchFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllBranchesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where phoneNumber does not contain
        defaultBranchFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllBranchesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where isActive equals to
        defaultBranchFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBranchesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where isActive in
        defaultBranchFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBranchesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        // Get all the branchList where isActive is not null
        defaultBranchFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultBranchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBranchShouldBeFound(shouldBeFound);
        defaultBranchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchShouldBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchShouldNotBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBranch() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        branchSearchRepository.save(branch);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());

        // Update the branch
        Branch updatedBranch = branchRepository.findById(branch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBranch are not directly saved in db
        em.detach(updatedBranch);
        updatedBranch
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .location(UPDATED_LOCATION)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .isActive(UPDATED_IS_ACTIVE);
        BranchDTO branchDTO = branchMapper.toDto(updatedBranch);

        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchDTO))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBranchToMatchAllProperties(updatedBranch);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Branch> branchSearchList = Streamable.of(branchSearchRepository.findAll()).toList();
                Branch testBranchSearch = branchSearchList.get(searchDatabaseSizeAfter - 1);

                assertBranchAllPropertiesEquals(testBranchSearch, updatedBranch);
            });
    }

    @Test
    @Transactional
    void putNonExistingBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        branch.setId(longCount.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        branch.setId(longCount.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        branch.setId(longCount.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch.location(UPDATED_LOCATION).phoneNumber(UPDATED_PHONE_NUMBER).isActive(UPDATED_IS_ACTIVE);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBranchUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBranch, branch), getPersistedBranch(branch));
    }

    @Test
    @Transactional
    void fullUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .location(UPDATED_LOCATION)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .isActive(UPDATED_IS_ACTIVE);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBranchUpdatableFieldsEquals(partialUpdatedBranch, getPersistedBranch(partialUpdatedBranch));
    }

    @Test
    @Transactional
    void patchNonExistingBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        branch.setId(longCount.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        branch.setId(longCount.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        branch.setId(longCount.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(branchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteBranch() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);
        branchRepository.save(branch);
        branchSearchRepository.save(branch);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the branch
        restBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, branch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(branchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchBranch() throws Exception {
        // Initialize the database
        insertedBranch = branchRepository.saveAndFlush(branch);
        branchSearchRepository.save(branch);

        // Search the branch
        restBranchMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    protected long getRepositoryCount() {
        return branchRepository.count();
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

    protected Branch getPersistedBranch(Branch branch) {
        return branchRepository.findById(branch.getId()).orElseThrow();
    }

    protected void assertPersistedBranchToMatchAllProperties(Branch expectedBranch) {
        assertBranchAllPropertiesEquals(expectedBranch, getPersistedBranch(expectedBranch));
    }

    protected void assertPersistedBranchToMatchUpdatableProperties(Branch expectedBranch) {
        assertBranchAllUpdatablePropertiesEquals(expectedBranch, getPersistedBranch(expectedBranch));
    }
}
