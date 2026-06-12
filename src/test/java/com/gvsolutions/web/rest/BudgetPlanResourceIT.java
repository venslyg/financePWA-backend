package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.BudgetPlanAsserts.*;
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
import com.gvsolutions.domain.BudgetPlan;
import com.gvsolutions.domain.enumeration.BudgetAlertStatus;
import com.gvsolutions.repository.BudgetPlanRepository;
import com.gvsolutions.repository.search.BudgetPlanSearchRepository;
import com.gvsolutions.service.dto.BudgetPlanDTO;
import com.gvsolutions.service.mapper.BudgetPlanMapper;
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
 * Integration tests for the {@link BudgetPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BudgetPlanResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BUDGET_PLAN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BUDGET_PLAN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final BigDecimal DEFAULT_ALLOCATED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ALLOCATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ALLOCATED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SPENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SPENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_SPENT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_REMAINING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REMAINING_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_REMAINING_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_USED_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_USED_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_USED_PERCENTAGE = new BigDecimal(1 - 1);

    private static final BudgetAlertStatus DEFAULT_ALERT_STATUS = BudgetAlertStatus.NORMAL;
    private static final BudgetAlertStatus UPDATED_ALERT_STATUS = BudgetAlertStatus.WARNING_80_PERCENT;

    private static final String ENTITY_API_URL = "/api/budget-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/budget-plans/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BudgetPlanRepository budgetPlanRepository;

    @Autowired
    private BudgetPlanMapper budgetPlanMapper;

    @Autowired
    private BudgetPlanSearchRepository budgetPlanSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBudgetPlanMockMvc;

    private BudgetPlan budgetPlan;

    private BudgetPlan insertedBudgetPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetPlan createEntity() {
        return new BudgetPlan()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .accountCode(DEFAULT_ACCOUNT_CODE)
            .budgetPlanCode(DEFAULT_BUDGET_PLAN_CODE)
            .departmentName(DEFAULT_DEPARTMENT_NAME)
            .year(DEFAULT_YEAR)
            .allocatedAmount(DEFAULT_ALLOCATED_AMOUNT)
            .spentAmount(DEFAULT_SPENT_AMOUNT)
            .remainingAmount(DEFAULT_REMAINING_AMOUNT)
            .usedPercentage(DEFAULT_USED_PERCENTAGE)
            .alertStatus(DEFAULT_ALERT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetPlan createUpdatedEntity() {
        return new BudgetPlan()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .budgetPlanCode(UPDATED_BUDGET_PLAN_CODE)
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .year(UPDATED_YEAR)
            .allocatedAmount(UPDATED_ALLOCATED_AMOUNT)
            .spentAmount(UPDATED_SPENT_AMOUNT)
            .remainingAmount(UPDATED_REMAINING_AMOUNT)
            .usedPercentage(UPDATED_USED_PERCENTAGE)
            .alertStatus(UPDATED_ALERT_STATUS);
    }

    @BeforeEach
    void initTest() {
        budgetPlan = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBudgetPlan != null) {
            budgetPlanRepository.delete(insertedBudgetPlan);
            budgetPlanSearchRepository.delete(insertedBudgetPlan);
            insertedBudgetPlan = null;
        }
    }

    @Test
    @Transactional
    void createBudgetPlan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        // Create the BudgetPlan
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);
        var returnedBudgetPlanDTO = om.readValue(
            restBudgetPlanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetPlanDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BudgetPlanDTO.class
        );

        // Validate the BudgetPlan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBudgetPlan = budgetPlanMapper.toEntity(returnedBudgetPlanDTO);
        assertBudgetPlanUpdatableFieldsEquals(returnedBudgetPlan, getPersistedBudgetPlan(returnedBudgetPlan));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedBudgetPlan = returnedBudgetPlan;
    }

    @Test
    @Transactional
    void createBudgetPlanWithExistingId() throws Exception {
        // Create the BudgetPlan with an existing ID
        budgetPlan.setId(1L);
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllBudgetPlans() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList
        restBudgetPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].budgetPlanCode").value(hasItem(DEFAULT_BUDGET_PLAN_CODE)))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].allocatedAmount").value(hasItem(sameNumber(DEFAULT_ALLOCATED_AMOUNT))))
            .andExpect(jsonPath("$.[*].spentAmount").value(hasItem(sameNumber(DEFAULT_SPENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].remainingAmount").value(hasItem(sameNumber(DEFAULT_REMAINING_AMOUNT))))
            .andExpect(jsonPath("$.[*].usedPercentage").value(hasItem(sameNumber(DEFAULT_USED_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].alertStatus").value(hasItem(DEFAULT_ALERT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getBudgetPlan() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get the budgetPlan
        restBudgetPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, budgetPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budgetPlan.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.accountCode").value(DEFAULT_ACCOUNT_CODE))
            .andExpect(jsonPath("$.budgetPlanCode").value(DEFAULT_BUDGET_PLAN_CODE))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.allocatedAmount").value(sameNumber(DEFAULT_ALLOCATED_AMOUNT)))
            .andExpect(jsonPath("$.spentAmount").value(sameNumber(DEFAULT_SPENT_AMOUNT)))
            .andExpect(jsonPath("$.remainingAmount").value(sameNumber(DEFAULT_REMAINING_AMOUNT)))
            .andExpect(jsonPath("$.usedPercentage").value(sameNumber(DEFAULT_USED_PERCENTAGE)))
            .andExpect(jsonPath("$.alertStatus").value(DEFAULT_ALERT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getBudgetPlansByIdFiltering() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        Long id = budgetPlan.getId();

        defaultBudgetPlanFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBudgetPlanFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBudgetPlanFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchCode equals to
        defaultBudgetPlanFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchCode in
        defaultBudgetPlanFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchCode is not null
        defaultBudgetPlanFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchCode contains
        defaultBudgetPlanFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchCode does not contain
        defaultBudgetPlanFiltering("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE, "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchId equals to
        defaultBudgetPlanFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchId in
        defaultBudgetPlanFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchId is not null
        defaultBudgetPlanFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchId contains
        defaultBudgetPlanFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where branchId does not contain
        defaultBudgetPlanFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAccountCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where accountCode equals to
        defaultBudgetPlanFiltering("accountCode.equals=" + DEFAULT_ACCOUNT_CODE, "accountCode.equals=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAccountCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where accountCode in
        defaultBudgetPlanFiltering(
            "accountCode.in=" + DEFAULT_ACCOUNT_CODE + "," + UPDATED_ACCOUNT_CODE,
            "accountCode.in=" + UPDATED_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAccountCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where accountCode is not null
        defaultBudgetPlanFiltering("accountCode.specified=true", "accountCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAccountCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where accountCode contains
        defaultBudgetPlanFiltering("accountCode.contains=" + DEFAULT_ACCOUNT_CODE, "accountCode.contains=" + UPDATED_ACCOUNT_CODE);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAccountCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where accountCode does not contain
        defaultBudgetPlanFiltering(
            "accountCode.doesNotContain=" + UPDATED_ACCOUNT_CODE,
            "accountCode.doesNotContain=" + DEFAULT_ACCOUNT_CODE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBudgetPlanCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where budgetPlanCode equals to
        defaultBudgetPlanFiltering(
            "budgetPlanCode.equals=" + DEFAULT_BUDGET_PLAN_CODE,
            "budgetPlanCode.equals=" + UPDATED_BUDGET_PLAN_CODE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBudgetPlanCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where budgetPlanCode in
        defaultBudgetPlanFiltering(
            "budgetPlanCode.in=" + DEFAULT_BUDGET_PLAN_CODE + "," + UPDATED_BUDGET_PLAN_CODE,
            "budgetPlanCode.in=" + UPDATED_BUDGET_PLAN_CODE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBudgetPlanCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where budgetPlanCode is not null
        defaultBudgetPlanFiltering("budgetPlanCode.specified=true", "budgetPlanCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBudgetPlanCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where budgetPlanCode contains
        defaultBudgetPlanFiltering(
            "budgetPlanCode.contains=" + DEFAULT_BUDGET_PLAN_CODE,
            "budgetPlanCode.contains=" + UPDATED_BUDGET_PLAN_CODE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByBudgetPlanCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where budgetPlanCode does not contain
        defaultBudgetPlanFiltering(
            "budgetPlanCode.doesNotContain=" + UPDATED_BUDGET_PLAN_CODE,
            "budgetPlanCode.doesNotContain=" + DEFAULT_BUDGET_PLAN_CODE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByDepartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where departmentName equals to
        defaultBudgetPlanFiltering("departmentName.equals=" + DEFAULT_DEPARTMENT_NAME, "departmentName.equals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByDepartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where departmentName in
        defaultBudgetPlanFiltering(
            "departmentName.in=" + DEFAULT_DEPARTMENT_NAME + "," + UPDATED_DEPARTMENT_NAME,
            "departmentName.in=" + UPDATED_DEPARTMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByDepartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where departmentName is not null
        defaultBudgetPlanFiltering("departmentName.specified=true", "departmentName.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByDepartmentNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where departmentName contains
        defaultBudgetPlanFiltering(
            "departmentName.contains=" + DEFAULT_DEPARTMENT_NAME,
            "departmentName.contains=" + UPDATED_DEPARTMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByDepartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where departmentName does not contain
        defaultBudgetPlanFiltering(
            "departmentName.doesNotContain=" + UPDATED_DEPARTMENT_NAME,
            "departmentName.doesNotContain=" + DEFAULT_DEPARTMENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where year equals to
        defaultBudgetPlanFiltering("year.equals=" + DEFAULT_YEAR, "year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where year in
        defaultBudgetPlanFiltering("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR, "year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where year is not null
        defaultBudgetPlanFiltering("year.specified=true", "year.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where year is greater than or equal to
        defaultBudgetPlanFiltering("year.greaterThanOrEqual=" + DEFAULT_YEAR, "year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where year is less than or equal to
        defaultBudgetPlanFiltering("year.lessThanOrEqual=" + DEFAULT_YEAR, "year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where year is less than
        defaultBudgetPlanFiltering("year.lessThan=" + UPDATED_YEAR, "year.lessThan=" + DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where year is greater than
        defaultBudgetPlanFiltering("year.greaterThan=" + SMALLER_YEAR, "year.greaterThan=" + DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAllocatedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where allocatedAmount equals to
        defaultBudgetPlanFiltering(
            "allocatedAmount.equals=" + DEFAULT_ALLOCATED_AMOUNT,
            "allocatedAmount.equals=" + UPDATED_ALLOCATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAllocatedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where allocatedAmount in
        defaultBudgetPlanFiltering(
            "allocatedAmount.in=" + DEFAULT_ALLOCATED_AMOUNT + "," + UPDATED_ALLOCATED_AMOUNT,
            "allocatedAmount.in=" + UPDATED_ALLOCATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAllocatedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where allocatedAmount is not null
        defaultBudgetPlanFiltering("allocatedAmount.specified=true", "allocatedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAllocatedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where allocatedAmount is greater than or equal to
        defaultBudgetPlanFiltering(
            "allocatedAmount.greaterThanOrEqual=" + DEFAULT_ALLOCATED_AMOUNT,
            "allocatedAmount.greaterThanOrEqual=" + UPDATED_ALLOCATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAllocatedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where allocatedAmount is less than or equal to
        defaultBudgetPlanFiltering(
            "allocatedAmount.lessThanOrEqual=" + DEFAULT_ALLOCATED_AMOUNT,
            "allocatedAmount.lessThanOrEqual=" + SMALLER_ALLOCATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAllocatedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where allocatedAmount is less than
        defaultBudgetPlanFiltering(
            "allocatedAmount.lessThan=" + UPDATED_ALLOCATED_AMOUNT,
            "allocatedAmount.lessThan=" + DEFAULT_ALLOCATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAllocatedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where allocatedAmount is greater than
        defaultBudgetPlanFiltering(
            "allocatedAmount.greaterThan=" + SMALLER_ALLOCATED_AMOUNT,
            "allocatedAmount.greaterThan=" + DEFAULT_ALLOCATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansBySpentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where spentAmount equals to
        defaultBudgetPlanFiltering("spentAmount.equals=" + DEFAULT_SPENT_AMOUNT, "spentAmount.equals=" + UPDATED_SPENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBudgetPlansBySpentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where spentAmount in
        defaultBudgetPlanFiltering(
            "spentAmount.in=" + DEFAULT_SPENT_AMOUNT + "," + UPDATED_SPENT_AMOUNT,
            "spentAmount.in=" + UPDATED_SPENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansBySpentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where spentAmount is not null
        defaultBudgetPlanFiltering("spentAmount.specified=true", "spentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansBySpentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where spentAmount is greater than or equal to
        defaultBudgetPlanFiltering(
            "spentAmount.greaterThanOrEqual=" + DEFAULT_SPENT_AMOUNT,
            "spentAmount.greaterThanOrEqual=" + UPDATED_SPENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansBySpentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where spentAmount is less than or equal to
        defaultBudgetPlanFiltering(
            "spentAmount.lessThanOrEqual=" + DEFAULT_SPENT_AMOUNT,
            "spentAmount.lessThanOrEqual=" + SMALLER_SPENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansBySpentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where spentAmount is less than
        defaultBudgetPlanFiltering("spentAmount.lessThan=" + UPDATED_SPENT_AMOUNT, "spentAmount.lessThan=" + DEFAULT_SPENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBudgetPlansBySpentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where spentAmount is greater than
        defaultBudgetPlanFiltering("spentAmount.greaterThan=" + SMALLER_SPENT_AMOUNT, "spentAmount.greaterThan=" + DEFAULT_SPENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByRemainingAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where remainingAmount equals to
        defaultBudgetPlanFiltering(
            "remainingAmount.equals=" + DEFAULT_REMAINING_AMOUNT,
            "remainingAmount.equals=" + UPDATED_REMAINING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByRemainingAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where remainingAmount in
        defaultBudgetPlanFiltering(
            "remainingAmount.in=" + DEFAULT_REMAINING_AMOUNT + "," + UPDATED_REMAINING_AMOUNT,
            "remainingAmount.in=" + UPDATED_REMAINING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByRemainingAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where remainingAmount is not null
        defaultBudgetPlanFiltering("remainingAmount.specified=true", "remainingAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByRemainingAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where remainingAmount is greater than or equal to
        defaultBudgetPlanFiltering(
            "remainingAmount.greaterThanOrEqual=" + DEFAULT_REMAINING_AMOUNT,
            "remainingAmount.greaterThanOrEqual=" + UPDATED_REMAINING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByRemainingAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where remainingAmount is less than or equal to
        defaultBudgetPlanFiltering(
            "remainingAmount.lessThanOrEqual=" + DEFAULT_REMAINING_AMOUNT,
            "remainingAmount.lessThanOrEqual=" + SMALLER_REMAINING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByRemainingAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where remainingAmount is less than
        defaultBudgetPlanFiltering(
            "remainingAmount.lessThan=" + UPDATED_REMAINING_AMOUNT,
            "remainingAmount.lessThan=" + DEFAULT_REMAINING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByRemainingAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where remainingAmount is greater than
        defaultBudgetPlanFiltering(
            "remainingAmount.greaterThan=" + SMALLER_REMAINING_AMOUNT,
            "remainingAmount.greaterThan=" + DEFAULT_REMAINING_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByUsedPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where usedPercentage equals to
        defaultBudgetPlanFiltering("usedPercentage.equals=" + DEFAULT_USED_PERCENTAGE, "usedPercentage.equals=" + UPDATED_USED_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByUsedPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where usedPercentage in
        defaultBudgetPlanFiltering(
            "usedPercentage.in=" + DEFAULT_USED_PERCENTAGE + "," + UPDATED_USED_PERCENTAGE,
            "usedPercentage.in=" + UPDATED_USED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByUsedPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where usedPercentage is not null
        defaultBudgetPlanFiltering("usedPercentage.specified=true", "usedPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetPlansByUsedPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where usedPercentage is greater than or equal to
        defaultBudgetPlanFiltering(
            "usedPercentage.greaterThanOrEqual=" + DEFAULT_USED_PERCENTAGE,
            "usedPercentage.greaterThanOrEqual=" + UPDATED_USED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByUsedPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where usedPercentage is less than or equal to
        defaultBudgetPlanFiltering(
            "usedPercentage.lessThanOrEqual=" + DEFAULT_USED_PERCENTAGE,
            "usedPercentage.lessThanOrEqual=" + SMALLER_USED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByUsedPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where usedPercentage is less than
        defaultBudgetPlanFiltering(
            "usedPercentage.lessThan=" + UPDATED_USED_PERCENTAGE,
            "usedPercentage.lessThan=" + DEFAULT_USED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByUsedPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where usedPercentage is greater than
        defaultBudgetPlanFiltering(
            "usedPercentage.greaterThan=" + SMALLER_USED_PERCENTAGE,
            "usedPercentage.greaterThan=" + DEFAULT_USED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAlertStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where alertStatus equals to
        defaultBudgetPlanFiltering("alertStatus.equals=" + DEFAULT_ALERT_STATUS, "alertStatus.equals=" + UPDATED_ALERT_STATUS);
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAlertStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where alertStatus in
        defaultBudgetPlanFiltering(
            "alertStatus.in=" + DEFAULT_ALERT_STATUS + "," + UPDATED_ALERT_STATUS,
            "alertStatus.in=" + UPDATED_ALERT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllBudgetPlansByAlertStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        // Get all the budgetPlanList where alertStatus is not null
        defaultBudgetPlanFiltering("alertStatus.specified=true", "alertStatus.specified=false");
    }

    private void defaultBudgetPlanFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBudgetPlanShouldBeFound(shouldBeFound);
        defaultBudgetPlanShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBudgetPlanShouldBeFound(String filter) throws Exception {
        restBudgetPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].budgetPlanCode").value(hasItem(DEFAULT_BUDGET_PLAN_CODE)))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].allocatedAmount").value(hasItem(sameNumber(DEFAULT_ALLOCATED_AMOUNT))))
            .andExpect(jsonPath("$.[*].spentAmount").value(hasItem(sameNumber(DEFAULT_SPENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].remainingAmount").value(hasItem(sameNumber(DEFAULT_REMAINING_AMOUNT))))
            .andExpect(jsonPath("$.[*].usedPercentage").value(hasItem(sameNumber(DEFAULT_USED_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].alertStatus").value(hasItem(DEFAULT_ALERT_STATUS.toString())));

        // Check, that the count call also returns 1
        restBudgetPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBudgetPlanShouldNotBeFound(String filter) throws Exception {
        restBudgetPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBudgetPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBudgetPlan() throws Exception {
        // Get the budgetPlan
        restBudgetPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBudgetPlan() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetPlanSearchRepository.save(budgetPlan);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());

        // Update the budgetPlan
        BudgetPlan updatedBudgetPlan = budgetPlanRepository.findById(budgetPlan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBudgetPlan are not directly saved in db
        em.detach(updatedBudgetPlan);
        updatedBudgetPlan
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .budgetPlanCode(UPDATED_BUDGET_PLAN_CODE)
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .year(UPDATED_YEAR)
            .allocatedAmount(UPDATED_ALLOCATED_AMOUNT)
            .spentAmount(UPDATED_SPENT_AMOUNT)
            .remainingAmount(UPDATED_REMAINING_AMOUNT)
            .usedPercentage(UPDATED_USED_PERCENTAGE)
            .alertStatus(UPDATED_ALERT_STATUS);
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(updatedBudgetPlan);

        restBudgetPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBudgetPlanToMatchAllProperties(updatedBudgetPlan);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<BudgetPlan> budgetPlanSearchList = Streamable.of(budgetPlanSearchRepository.findAll()).toList();
                BudgetPlan testBudgetPlanSearch = budgetPlanSearchList.get(searchDatabaseSizeAfter - 1);

                assertBudgetPlanAllPropertiesEquals(testBudgetPlanSearch, updatedBudgetPlan);
            });
    }

    @Test
    @Transactional
    void putNonExistingBudgetPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        budgetPlan.setId(longCount.incrementAndGet());

        // Create the BudgetPlan
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchBudgetPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        budgetPlan.setId(longCount.incrementAndGet());

        // Create the BudgetPlan
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBudgetPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        budgetPlan.setId(longCount.incrementAndGet());

        // Create the BudgetPlan
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateBudgetPlanWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetPlan using partial update
        BudgetPlan partialUpdatedBudgetPlan = new BudgetPlan();
        partialUpdatedBudgetPlan.setId(budgetPlan.getId());

        partialUpdatedBudgetPlan
            .branchCode(UPDATED_BRANCH_CODE)
            .year(UPDATED_YEAR)
            .allocatedAmount(UPDATED_ALLOCATED_AMOUNT)
            .usedPercentage(UPDATED_USED_PERCENTAGE)
            .alertStatus(UPDATED_ALERT_STATUS);

        restBudgetPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetPlan))
            )
            .andExpect(status().isOk());

        // Validate the BudgetPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetPlanUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBudgetPlan, budgetPlan),
            getPersistedBudgetPlan(budgetPlan)
        );
    }

    @Test
    @Transactional
    void fullUpdateBudgetPlanWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetPlan using partial update
        BudgetPlan partialUpdatedBudgetPlan = new BudgetPlan();
        partialUpdatedBudgetPlan.setId(budgetPlan.getId());

        partialUpdatedBudgetPlan
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .accountCode(UPDATED_ACCOUNT_CODE)
            .budgetPlanCode(UPDATED_BUDGET_PLAN_CODE)
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .year(UPDATED_YEAR)
            .allocatedAmount(UPDATED_ALLOCATED_AMOUNT)
            .spentAmount(UPDATED_SPENT_AMOUNT)
            .remainingAmount(UPDATED_REMAINING_AMOUNT)
            .usedPercentage(UPDATED_USED_PERCENTAGE)
            .alertStatus(UPDATED_ALERT_STATUS);

        restBudgetPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetPlan))
            )
            .andExpect(status().isOk());

        // Validate the BudgetPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetPlanUpdatableFieldsEquals(partialUpdatedBudgetPlan, getPersistedBudgetPlan(partialUpdatedBudgetPlan));
    }

    @Test
    @Transactional
    void patchNonExistingBudgetPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        budgetPlan.setId(longCount.incrementAndGet());

        // Create the BudgetPlan
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, budgetPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBudgetPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        budgetPlan.setId(longCount.incrementAndGet());

        // Create the BudgetPlan
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBudgetPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        budgetPlan.setId(longCount.incrementAndGet());

        // Create the BudgetPlan
        BudgetPlanDTO budgetPlanDTO = budgetPlanMapper.toDto(budgetPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(budgetPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteBudgetPlan() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);
        budgetPlanRepository.save(budgetPlan);
        budgetPlanSearchRepository.save(budgetPlan);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the budgetPlan
        restBudgetPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, budgetPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetPlanSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchBudgetPlan() throws Exception {
        // Initialize the database
        insertedBudgetPlan = budgetPlanRepository.saveAndFlush(budgetPlan);
        budgetPlanSearchRepository.save(budgetPlan);

        // Search the budgetPlan
        restBudgetPlanMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + budgetPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].accountCode").value(hasItem(DEFAULT_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].budgetPlanCode").value(hasItem(DEFAULT_BUDGET_PLAN_CODE)))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].allocatedAmount").value(hasItem(sameNumber(DEFAULT_ALLOCATED_AMOUNT))))
            .andExpect(jsonPath("$.[*].spentAmount").value(hasItem(sameNumber(DEFAULT_SPENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].remainingAmount").value(hasItem(sameNumber(DEFAULT_REMAINING_AMOUNT))))
            .andExpect(jsonPath("$.[*].usedPercentage").value(hasItem(sameNumber(DEFAULT_USED_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].alertStatus").value(hasItem(DEFAULT_ALERT_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return budgetPlanRepository.count();
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

    protected BudgetPlan getPersistedBudgetPlan(BudgetPlan budgetPlan) {
        return budgetPlanRepository.findById(budgetPlan.getId()).orElseThrow();
    }

    protected void assertPersistedBudgetPlanToMatchAllProperties(BudgetPlan expectedBudgetPlan) {
        assertBudgetPlanAllPropertiesEquals(expectedBudgetPlan, getPersistedBudgetPlan(expectedBudgetPlan));
    }

    protected void assertPersistedBudgetPlanToMatchUpdatableProperties(BudgetPlan expectedBudgetPlan) {
        assertBudgetPlanAllUpdatablePropertiesEquals(expectedBudgetPlan, getPersistedBudgetPlan(expectedBudgetPlan));
    }
}
