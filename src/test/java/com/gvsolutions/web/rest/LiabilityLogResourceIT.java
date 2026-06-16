package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.LiabilityLogAsserts.*;
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
import com.gvsolutions.domain.LiabilityLog;
import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.LiabilityType;
import com.gvsolutions.repository.LiabilityLogRepository;
import com.gvsolutions.repository.search.LiabilityLogSearchRepository;
import com.gvsolutions.service.dto.LiabilityLogDTO;
import com.gvsolutions.service.mapper.LiabilityLogMapper;
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
 * Integration tests for the {@link LiabilityLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LiabilityLogResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LIABILITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LIABILITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_FROM = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LiabilityType DEFAULT_LIABILITY_TYPE = LiabilityType.LONG_TERM;
    private static final LiabilityType UPDATED_LIABILITY_TYPE = LiabilityType.SHORT_TERM;

    private static final BigDecimal DEFAULT_TOTAL_LOAN_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_LOAN_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_LOAN_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_INTEREST_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_PERCENTAGE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MONTHLY_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTHLY_PAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTHLY_PAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PRINCIPAL_PAID = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRINCIPAL_PAID = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRINCIPAL_PAID = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_BALANCE_TO_PAY = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE_TO_PAY = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE_TO_PAY = new BigDecimal(1 - 1);

    private static final ApprovalStatus DEFAULT_STATUS = ApprovalStatus.APPROVED;
    private static final ApprovalStatus UPDATED_STATUS = ApprovalStatus.DECLINED;

    private static final String ENTITY_API_URL = "/api/liability-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/liability-logs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LiabilityLogRepository liabilityLogRepository;

    @Autowired
    private LiabilityLogMapper liabilityLogMapper;

    @Autowired
    private LiabilityLogSearchRepository liabilityLogSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLiabilityLogMockMvc;

    private LiabilityLog liabilityLog;

    private LiabilityLog insertedLiabilityLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LiabilityLog createEntity() {
        return new LiabilityLog()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .liabilityCode(DEFAULT_LIABILITY_CODE)
            .loanFrom(DEFAULT_LOAN_FROM)
            .description(DEFAULT_DESCRIPTION)
            .liabilityType(DEFAULT_LIABILITY_TYPE)
            .totalLoanAmount(DEFAULT_TOTAL_LOAN_AMOUNT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .interestPercentage(DEFAULT_INTEREST_PERCENTAGE)
            .monthlyPaymentAmount(DEFAULT_MONTHLY_PAYMENT_AMOUNT)
            .principalPaid(DEFAULT_PRINCIPAL_PAID)
            .balanceToPay(DEFAULT_BALANCE_TO_PAY)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LiabilityLog createUpdatedEntity() {
        return new LiabilityLog()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .liabilityCode(UPDATED_LIABILITY_CODE)
            .loanFrom(UPDATED_LOAN_FROM)
            .description(UPDATED_DESCRIPTION)
            .liabilityType(UPDATED_LIABILITY_TYPE)
            .totalLoanAmount(UPDATED_TOTAL_LOAN_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .interestPercentage(UPDATED_INTEREST_PERCENTAGE)
            .monthlyPaymentAmount(UPDATED_MONTHLY_PAYMENT_AMOUNT)
            .principalPaid(UPDATED_PRINCIPAL_PAID)
            .balanceToPay(UPDATED_BALANCE_TO_PAY)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        liabilityLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLiabilityLog != null) {
            liabilityLogRepository.delete(insertedLiabilityLog);
            liabilityLogSearchRepository.delete(insertedLiabilityLog);
            insertedLiabilityLog = null;
        }
    }

    @Test
    @Transactional
    void createLiabilityLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        // Create the LiabilityLog
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);
        var returnedLiabilityLogDTO = om.readValue(
            restLiabilityLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(liabilityLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LiabilityLogDTO.class
        );

        // Validate the LiabilityLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLiabilityLog = liabilityLogMapper.toEntity(returnedLiabilityLogDTO);
        assertLiabilityLogUpdatableFieldsEquals(returnedLiabilityLog, getPersistedLiabilityLog(returnedLiabilityLog));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedLiabilityLog = returnedLiabilityLog;
    }

    @Test
    @Transactional
    void createLiabilityLogWithExistingId() throws Exception {
        // Create the LiabilityLog with an existing ID
        liabilityLog.setId(1L);
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restLiabilityLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(liabilityLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllLiabilityLogs() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList
        restLiabilityLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liabilityLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].liabilityCode").value(hasItem(DEFAULT_LIABILITY_CODE)))
            .andExpect(jsonPath("$.[*].loanFrom").value(hasItem(DEFAULT_LOAN_FROM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].liabilityType").value(hasItem(DEFAULT_LIABILITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalLoanAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_LOAN_AMOUNT))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].interestPercentage").value(hasItem(sameNumber(DEFAULT_INTEREST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].monthlyPaymentAmount").value(hasItem(sameNumber(DEFAULT_MONTHLY_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].principalPaid").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAID))))
            .andExpect(jsonPath("$.[*].balanceToPay").value(hasItem(sameNumber(DEFAULT_BALANCE_TO_PAY))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getLiabilityLog() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get the liabilityLog
        restLiabilityLogMockMvc
            .perform(get(ENTITY_API_URL_ID, liabilityLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(liabilityLog.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.liabilityCode").value(DEFAULT_LIABILITY_CODE))
            .andExpect(jsonPath("$.loanFrom").value(DEFAULT_LOAN_FROM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.liabilityType").value(DEFAULT_LIABILITY_TYPE.toString()))
            .andExpect(jsonPath("$.totalLoanAmount").value(sameNumber(DEFAULT_TOTAL_LOAN_AMOUNT)))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.interestPercentage").value(sameNumber(DEFAULT_INTEREST_PERCENTAGE)))
            .andExpect(jsonPath("$.monthlyPaymentAmount").value(sameNumber(DEFAULT_MONTHLY_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.principalPaid").value(sameNumber(DEFAULT_PRINCIPAL_PAID)))
            .andExpect(jsonPath("$.balanceToPay").value(sameNumber(DEFAULT_BALANCE_TO_PAY)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getLiabilityLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        Long id = liabilityLog.getId();

        defaultLiabilityLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLiabilityLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLiabilityLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchCode equals to
        defaultLiabilityLogFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchCode in
        defaultLiabilityLogFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchCode is not null
        defaultLiabilityLogFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchCode contains
        defaultLiabilityLogFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchCode does not contain
        defaultLiabilityLogFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchId equals to
        defaultLiabilityLogFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchId in
        defaultLiabilityLogFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchId is not null
        defaultLiabilityLogFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchId contains
        defaultLiabilityLogFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where branchId does not contain
        defaultLiabilityLogFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityCode equals to
        defaultLiabilityLogFiltering("liabilityCode.equals=" + DEFAULT_LIABILITY_CODE, "liabilityCode.equals=" + UPDATED_LIABILITY_CODE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityCode in
        defaultLiabilityLogFiltering(
            "liabilityCode.in=" + DEFAULT_LIABILITY_CODE + "," + UPDATED_LIABILITY_CODE,
            "liabilityCode.in=" + UPDATED_LIABILITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityCode is not null
        defaultLiabilityLogFiltering("liabilityCode.specified=true", "liabilityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityCode contains
        defaultLiabilityLogFiltering(
            "liabilityCode.contains=" + DEFAULT_LIABILITY_CODE,
            "liabilityCode.contains=" + UPDATED_LIABILITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityCode does not contain
        defaultLiabilityLogFiltering(
            "liabilityCode.doesNotContain=" + UPDATED_LIABILITY_CODE,
            "liabilityCode.doesNotContain=" + DEFAULT_LIABILITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLoanFromIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where loanFrom equals to
        defaultLiabilityLogFiltering("loanFrom.equals=" + DEFAULT_LOAN_FROM, "loanFrom.equals=" + UPDATED_LOAN_FROM);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLoanFromIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where loanFrom in
        defaultLiabilityLogFiltering("loanFrom.in=" + DEFAULT_LOAN_FROM + "," + UPDATED_LOAN_FROM, "loanFrom.in=" + UPDATED_LOAN_FROM);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLoanFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where loanFrom is not null
        defaultLiabilityLogFiltering("loanFrom.specified=true", "loanFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLoanFromContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where loanFrom contains
        defaultLiabilityLogFiltering("loanFrom.contains=" + DEFAULT_LOAN_FROM, "loanFrom.contains=" + UPDATED_LOAN_FROM);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLoanFromNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where loanFrom does not contain
        defaultLiabilityLogFiltering("loanFrom.doesNotContain=" + UPDATED_LOAN_FROM, "loanFrom.doesNotContain=" + DEFAULT_LOAN_FROM);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where description equals to
        defaultLiabilityLogFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where description in
        defaultLiabilityLogFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where description is not null
        defaultLiabilityLogFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where description contains
        defaultLiabilityLogFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where description does not contain
        defaultLiabilityLogFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityType equals to
        defaultLiabilityLogFiltering("liabilityType.equals=" + DEFAULT_LIABILITY_TYPE, "liabilityType.equals=" + UPDATED_LIABILITY_TYPE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityType in
        defaultLiabilityLogFiltering(
            "liabilityType.in=" + DEFAULT_LIABILITY_TYPE + "," + UPDATED_LIABILITY_TYPE,
            "liabilityType.in=" + UPDATED_LIABILITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByLiabilityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where liabilityType is not null
        defaultLiabilityLogFiltering("liabilityType.specified=true", "liabilityType.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByTotalLoanAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where totalLoanAmount equals to
        defaultLiabilityLogFiltering(
            "totalLoanAmount.equals=" + DEFAULT_TOTAL_LOAN_AMOUNT,
            "totalLoanAmount.equals=" + UPDATED_TOTAL_LOAN_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByTotalLoanAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where totalLoanAmount in
        defaultLiabilityLogFiltering(
            "totalLoanAmount.in=" + DEFAULT_TOTAL_LOAN_AMOUNT + "," + UPDATED_TOTAL_LOAN_AMOUNT,
            "totalLoanAmount.in=" + UPDATED_TOTAL_LOAN_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByTotalLoanAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where totalLoanAmount is not null
        defaultLiabilityLogFiltering("totalLoanAmount.specified=true", "totalLoanAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByTotalLoanAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where totalLoanAmount is greater than or equal to
        defaultLiabilityLogFiltering(
            "totalLoanAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_LOAN_AMOUNT,
            "totalLoanAmount.greaterThanOrEqual=" + UPDATED_TOTAL_LOAN_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByTotalLoanAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where totalLoanAmount is less than or equal to
        defaultLiabilityLogFiltering(
            "totalLoanAmount.lessThanOrEqual=" + DEFAULT_TOTAL_LOAN_AMOUNT,
            "totalLoanAmount.lessThanOrEqual=" + SMALLER_TOTAL_LOAN_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByTotalLoanAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where totalLoanAmount is less than
        defaultLiabilityLogFiltering(
            "totalLoanAmount.lessThan=" + UPDATED_TOTAL_LOAN_AMOUNT,
            "totalLoanAmount.lessThan=" + DEFAULT_TOTAL_LOAN_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByTotalLoanAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where totalLoanAmount is greater than
        defaultLiabilityLogFiltering(
            "totalLoanAmount.greaterThan=" + SMALLER_TOTAL_LOAN_AMOUNT,
            "totalLoanAmount.greaterThan=" + DEFAULT_TOTAL_LOAN_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where startDate equals to
        defaultLiabilityLogFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where startDate in
        defaultLiabilityLogFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where startDate is not null
        defaultLiabilityLogFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where startDate is greater than or equal to
        defaultLiabilityLogFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where startDate is less than or equal to
        defaultLiabilityLogFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where startDate is less than
        defaultLiabilityLogFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where startDate is greater than
        defaultLiabilityLogFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where endDate equals to
        defaultLiabilityLogFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where endDate in
        defaultLiabilityLogFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where endDate is not null
        defaultLiabilityLogFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where endDate is greater than or equal to
        defaultLiabilityLogFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where endDate is less than or equal to
        defaultLiabilityLogFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where endDate is less than
        defaultLiabilityLogFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where endDate is greater than
        defaultLiabilityLogFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByInterestPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where interestPercentage equals to
        defaultLiabilityLogFiltering(
            "interestPercentage.equals=" + DEFAULT_INTEREST_PERCENTAGE,
            "interestPercentage.equals=" + UPDATED_INTEREST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByInterestPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where interestPercentage in
        defaultLiabilityLogFiltering(
            "interestPercentage.in=" + DEFAULT_INTEREST_PERCENTAGE + "," + UPDATED_INTEREST_PERCENTAGE,
            "interestPercentage.in=" + UPDATED_INTEREST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByInterestPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where interestPercentage is not null
        defaultLiabilityLogFiltering("interestPercentage.specified=true", "interestPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByInterestPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where interestPercentage is greater than or equal to
        defaultLiabilityLogFiltering(
            "interestPercentage.greaterThanOrEqual=" + DEFAULT_INTEREST_PERCENTAGE,
            "interestPercentage.greaterThanOrEqual=" + UPDATED_INTEREST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByInterestPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where interestPercentage is less than or equal to
        defaultLiabilityLogFiltering(
            "interestPercentage.lessThanOrEqual=" + DEFAULT_INTEREST_PERCENTAGE,
            "interestPercentage.lessThanOrEqual=" + SMALLER_INTEREST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByInterestPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where interestPercentage is less than
        defaultLiabilityLogFiltering(
            "interestPercentage.lessThan=" + UPDATED_INTEREST_PERCENTAGE,
            "interestPercentage.lessThan=" + DEFAULT_INTEREST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByInterestPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where interestPercentage is greater than
        defaultLiabilityLogFiltering(
            "interestPercentage.greaterThan=" + SMALLER_INTEREST_PERCENTAGE,
            "interestPercentage.greaterThan=" + DEFAULT_INTEREST_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByMonthlyPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where monthlyPaymentAmount equals to
        defaultLiabilityLogFiltering(
            "monthlyPaymentAmount.equals=" + DEFAULT_MONTHLY_PAYMENT_AMOUNT,
            "monthlyPaymentAmount.equals=" + UPDATED_MONTHLY_PAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByMonthlyPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where monthlyPaymentAmount in
        defaultLiabilityLogFiltering(
            "monthlyPaymentAmount.in=" + DEFAULT_MONTHLY_PAYMENT_AMOUNT + "," + UPDATED_MONTHLY_PAYMENT_AMOUNT,
            "monthlyPaymentAmount.in=" + UPDATED_MONTHLY_PAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByMonthlyPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where monthlyPaymentAmount is not null
        defaultLiabilityLogFiltering("monthlyPaymentAmount.specified=true", "monthlyPaymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByMonthlyPaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where monthlyPaymentAmount is greater than or equal to
        defaultLiabilityLogFiltering(
            "monthlyPaymentAmount.greaterThanOrEqual=" + DEFAULT_MONTHLY_PAYMENT_AMOUNT,
            "monthlyPaymentAmount.greaterThanOrEqual=" + UPDATED_MONTHLY_PAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByMonthlyPaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where monthlyPaymentAmount is less than or equal to
        defaultLiabilityLogFiltering(
            "monthlyPaymentAmount.lessThanOrEqual=" + DEFAULT_MONTHLY_PAYMENT_AMOUNT,
            "monthlyPaymentAmount.lessThanOrEqual=" + SMALLER_MONTHLY_PAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByMonthlyPaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where monthlyPaymentAmount is less than
        defaultLiabilityLogFiltering(
            "monthlyPaymentAmount.lessThan=" + UPDATED_MONTHLY_PAYMENT_AMOUNT,
            "monthlyPaymentAmount.lessThan=" + DEFAULT_MONTHLY_PAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByMonthlyPaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where monthlyPaymentAmount is greater than
        defaultLiabilityLogFiltering(
            "monthlyPaymentAmount.greaterThan=" + SMALLER_MONTHLY_PAYMENT_AMOUNT,
            "monthlyPaymentAmount.greaterThan=" + DEFAULT_MONTHLY_PAYMENT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByPrincipalPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where principalPaid equals to
        defaultLiabilityLogFiltering("principalPaid.equals=" + DEFAULT_PRINCIPAL_PAID, "principalPaid.equals=" + UPDATED_PRINCIPAL_PAID);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByPrincipalPaidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where principalPaid in
        defaultLiabilityLogFiltering(
            "principalPaid.in=" + DEFAULT_PRINCIPAL_PAID + "," + UPDATED_PRINCIPAL_PAID,
            "principalPaid.in=" + UPDATED_PRINCIPAL_PAID
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByPrincipalPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where principalPaid is not null
        defaultLiabilityLogFiltering("principalPaid.specified=true", "principalPaid.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByPrincipalPaidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where principalPaid is greater than or equal to
        defaultLiabilityLogFiltering(
            "principalPaid.greaterThanOrEqual=" + DEFAULT_PRINCIPAL_PAID,
            "principalPaid.greaterThanOrEqual=" + UPDATED_PRINCIPAL_PAID
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByPrincipalPaidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where principalPaid is less than or equal to
        defaultLiabilityLogFiltering(
            "principalPaid.lessThanOrEqual=" + DEFAULT_PRINCIPAL_PAID,
            "principalPaid.lessThanOrEqual=" + SMALLER_PRINCIPAL_PAID
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByPrincipalPaidIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where principalPaid is less than
        defaultLiabilityLogFiltering(
            "principalPaid.lessThan=" + UPDATED_PRINCIPAL_PAID,
            "principalPaid.lessThan=" + DEFAULT_PRINCIPAL_PAID
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByPrincipalPaidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where principalPaid is greater than
        defaultLiabilityLogFiltering(
            "principalPaid.greaterThan=" + SMALLER_PRINCIPAL_PAID,
            "principalPaid.greaterThan=" + DEFAULT_PRINCIPAL_PAID
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBalanceToPayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where balanceToPay equals to
        defaultLiabilityLogFiltering("balanceToPay.equals=" + DEFAULT_BALANCE_TO_PAY, "balanceToPay.equals=" + UPDATED_BALANCE_TO_PAY);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBalanceToPayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where balanceToPay in
        defaultLiabilityLogFiltering(
            "balanceToPay.in=" + DEFAULT_BALANCE_TO_PAY + "," + UPDATED_BALANCE_TO_PAY,
            "balanceToPay.in=" + UPDATED_BALANCE_TO_PAY
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBalanceToPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where balanceToPay is not null
        defaultLiabilityLogFiltering("balanceToPay.specified=true", "balanceToPay.specified=false");
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBalanceToPayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where balanceToPay is greater than or equal to
        defaultLiabilityLogFiltering(
            "balanceToPay.greaterThanOrEqual=" + DEFAULT_BALANCE_TO_PAY,
            "balanceToPay.greaterThanOrEqual=" + UPDATED_BALANCE_TO_PAY
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBalanceToPayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where balanceToPay is less than or equal to
        defaultLiabilityLogFiltering(
            "balanceToPay.lessThanOrEqual=" + DEFAULT_BALANCE_TO_PAY,
            "balanceToPay.lessThanOrEqual=" + SMALLER_BALANCE_TO_PAY
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBalanceToPayIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where balanceToPay is less than
        defaultLiabilityLogFiltering("balanceToPay.lessThan=" + UPDATED_BALANCE_TO_PAY, "balanceToPay.lessThan=" + DEFAULT_BALANCE_TO_PAY);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByBalanceToPayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where balanceToPay is greater than
        defaultLiabilityLogFiltering(
            "balanceToPay.greaterThan=" + SMALLER_BALANCE_TO_PAY,
            "balanceToPay.greaterThan=" + DEFAULT_BALANCE_TO_PAY
        );
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where status equals to
        defaultLiabilityLogFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where status in
        defaultLiabilityLogFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLiabilityLogsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        // Get all the liabilityLogList where status is not null
        defaultLiabilityLogFiltering("status.specified=true", "status.specified=false");
    }

    private void defaultLiabilityLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLiabilityLogShouldBeFound(shouldBeFound);
        defaultLiabilityLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLiabilityLogShouldBeFound(String filter) throws Exception {
        restLiabilityLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liabilityLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].liabilityCode").value(hasItem(DEFAULT_LIABILITY_CODE)))
            .andExpect(jsonPath("$.[*].loanFrom").value(hasItem(DEFAULT_LOAN_FROM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].liabilityType").value(hasItem(DEFAULT_LIABILITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalLoanAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_LOAN_AMOUNT))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].interestPercentage").value(hasItem(sameNumber(DEFAULT_INTEREST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].monthlyPaymentAmount").value(hasItem(sameNumber(DEFAULT_MONTHLY_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].principalPaid").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAID))))
            .andExpect(jsonPath("$.[*].balanceToPay").value(hasItem(sameNumber(DEFAULT_BALANCE_TO_PAY))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restLiabilityLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLiabilityLogShouldNotBeFound(String filter) throws Exception {
        restLiabilityLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLiabilityLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLiabilityLog() throws Exception {
        // Get the liabilityLog
        restLiabilityLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLiabilityLog() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        liabilityLogSearchRepository.save(liabilityLog);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());

        // Update the liabilityLog
        LiabilityLog updatedLiabilityLog = liabilityLogRepository.findById(liabilityLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLiabilityLog are not directly saved in db
        em.detach(updatedLiabilityLog);
        updatedLiabilityLog
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .liabilityCode(UPDATED_LIABILITY_CODE)
            .loanFrom(UPDATED_LOAN_FROM)
            .description(UPDATED_DESCRIPTION)
            .liabilityType(UPDATED_LIABILITY_TYPE)
            .totalLoanAmount(UPDATED_TOTAL_LOAN_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .interestPercentage(UPDATED_INTEREST_PERCENTAGE)
            .monthlyPaymentAmount(UPDATED_MONTHLY_PAYMENT_AMOUNT)
            .principalPaid(UPDATED_PRINCIPAL_PAID)
            .balanceToPay(UPDATED_BALANCE_TO_PAY)
            .status(UPDATED_STATUS);
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(updatedLiabilityLog);

        restLiabilityLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, liabilityLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(liabilityLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLiabilityLogToMatchAllProperties(updatedLiabilityLog);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<LiabilityLog> liabilityLogSearchList = Streamable.of(liabilityLogSearchRepository.findAll()).toList();
                LiabilityLog testLiabilityLogSearch = liabilityLogSearchList.get(searchDatabaseSizeAfter - 1);

                assertLiabilityLogAllPropertiesEquals(testLiabilityLogSearch, updatedLiabilityLog);
            });
    }

    @Test
    @Transactional
    void putNonExistingLiabilityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        liabilityLog.setId(longCount.incrementAndGet());

        // Create the LiabilityLog
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLiabilityLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, liabilityLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(liabilityLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchLiabilityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        liabilityLog.setId(longCount.incrementAndGet());

        // Create the LiabilityLog
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLiabilityLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(liabilityLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLiabilityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        liabilityLog.setId(longCount.incrementAndGet());

        // Create the LiabilityLog
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLiabilityLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(liabilityLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateLiabilityLogWithPatch() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the liabilityLog using partial update
        LiabilityLog partialUpdatedLiabilityLog = new LiabilityLog();
        partialUpdatedLiabilityLog.setId(liabilityLog.getId());

        partialUpdatedLiabilityLog
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .liabilityCode(UPDATED_LIABILITY_CODE)
            .loanFrom(UPDATED_LOAN_FROM)
            .description(UPDATED_DESCRIPTION)
            .liabilityType(UPDATED_LIABILITY_TYPE)
            .totalLoanAmount(UPDATED_TOTAL_LOAN_AMOUNT)
            .endDate(UPDATED_END_DATE)
            .monthlyPaymentAmount(UPDATED_MONTHLY_PAYMENT_AMOUNT)
            .principalPaid(UPDATED_PRINCIPAL_PAID)
            .status(UPDATED_STATUS);

        restLiabilityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLiabilityLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLiabilityLog))
            )
            .andExpect(status().isOk());

        // Validate the LiabilityLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLiabilityLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLiabilityLog, liabilityLog),
            getPersistedLiabilityLog(liabilityLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateLiabilityLogWithPatch() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the liabilityLog using partial update
        LiabilityLog partialUpdatedLiabilityLog = new LiabilityLog();
        partialUpdatedLiabilityLog.setId(liabilityLog.getId());

        partialUpdatedLiabilityLog
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .liabilityCode(UPDATED_LIABILITY_CODE)
            .loanFrom(UPDATED_LOAN_FROM)
            .description(UPDATED_DESCRIPTION)
            .liabilityType(UPDATED_LIABILITY_TYPE)
            .totalLoanAmount(UPDATED_TOTAL_LOAN_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .interestPercentage(UPDATED_INTEREST_PERCENTAGE)
            .monthlyPaymentAmount(UPDATED_MONTHLY_PAYMENT_AMOUNT)
            .principalPaid(UPDATED_PRINCIPAL_PAID)
            .balanceToPay(UPDATED_BALANCE_TO_PAY)
            .status(UPDATED_STATUS);

        restLiabilityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLiabilityLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLiabilityLog))
            )
            .andExpect(status().isOk());

        // Validate the LiabilityLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLiabilityLogUpdatableFieldsEquals(partialUpdatedLiabilityLog, getPersistedLiabilityLog(partialUpdatedLiabilityLog));
    }

    @Test
    @Transactional
    void patchNonExistingLiabilityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        liabilityLog.setId(longCount.incrementAndGet());

        // Create the LiabilityLog
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLiabilityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, liabilityLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(liabilityLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLiabilityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        liabilityLog.setId(longCount.incrementAndGet());

        // Create the LiabilityLog
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLiabilityLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(liabilityLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLiabilityLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        liabilityLog.setId(longCount.incrementAndGet());

        // Create the LiabilityLog
        LiabilityLogDTO liabilityLogDTO = liabilityLogMapper.toDto(liabilityLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLiabilityLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(liabilityLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LiabilityLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteLiabilityLog() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);
        liabilityLogRepository.save(liabilityLog);
        liabilityLogSearchRepository.save(liabilityLog);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the liabilityLog
        restLiabilityLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, liabilityLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilityLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchLiabilityLog() throws Exception {
        // Initialize the database
        insertedLiabilityLog = liabilityLogRepository.saveAndFlush(liabilityLog);
        liabilityLogSearchRepository.save(liabilityLog);

        // Search the liabilityLog
        restLiabilityLogMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + liabilityLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liabilityLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].liabilityCode").value(hasItem(DEFAULT_LIABILITY_CODE)))
            .andExpect(jsonPath("$.[*].loanFrom").value(hasItem(DEFAULT_LOAN_FROM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].liabilityType").value(hasItem(DEFAULT_LIABILITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalLoanAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_LOAN_AMOUNT))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].interestPercentage").value(hasItem(sameNumber(DEFAULT_INTEREST_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].monthlyPaymentAmount").value(hasItem(sameNumber(DEFAULT_MONTHLY_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].principalPaid").value(hasItem(sameNumber(DEFAULT_PRINCIPAL_PAID))))
            .andExpect(jsonPath("$.[*].balanceToPay").value(hasItem(sameNumber(DEFAULT_BALANCE_TO_PAY))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return liabilityLogRepository.count();
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

    protected LiabilityLog getPersistedLiabilityLog(LiabilityLog liabilityLog) {
        return liabilityLogRepository.findById(liabilityLog.getId()).orElseThrow();
    }

    protected void assertPersistedLiabilityLogToMatchAllProperties(LiabilityLog expectedLiabilityLog) {
        assertLiabilityLogAllPropertiesEquals(expectedLiabilityLog, getPersistedLiabilityLog(expectedLiabilityLog));
    }

    protected void assertPersistedLiabilityLogToMatchUpdatableProperties(LiabilityLog expectedLiabilityLog) {
        assertLiabilityLogAllUpdatablePropertiesEquals(expectedLiabilityLog, getPersistedLiabilityLog(expectedLiabilityLog));
    }
}
