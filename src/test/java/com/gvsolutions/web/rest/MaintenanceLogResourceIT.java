package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.MaintenanceLogAsserts.*;
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
import com.gvsolutions.domain.MaintenanceLog;
import com.gvsolutions.domain.enumeration.MaintenanceLogType;
import com.gvsolutions.repository.MaintenanceLogRepository;
import com.gvsolutions.repository.search.MaintenanceLogSearchRepository;
import com.gvsolutions.service.MaintenanceLogService;
import com.gvsolutions.service.dto.MaintenanceLogDTO;
import com.gvsolutions.service.mapper.MaintenanceLogMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MaintenanceLogResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MaintenanceLogResourceIT {

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MAINTENANCE_LOG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MAINTENANCE_LOG_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LOG_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOG_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LOG_DATE = LocalDate.ofEpochDay(-1L);

    private static final MaintenanceLogType DEFAULT_LOG_TYPE = MaintenanceLogType.REPAIR;
    private static final MaintenanceLogType UPDATED_LOG_TYPE = MaintenanceLogType.REPLACEMENT;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_COST = new BigDecimal(1 - 1);

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NEXT_SERVICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_SERVICE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_NEXT_SERVICE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/maintenance-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/maintenance-logs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;

    @Mock
    private MaintenanceLogRepository maintenanceLogRepositoryMock;

    @Autowired
    private MaintenanceLogMapper maintenanceLogMapper;

    @Mock
    private MaintenanceLogService maintenanceLogServiceMock;

    @Autowired
    private MaintenanceLogSearchRepository maintenanceLogSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintenanceLogMockMvc;

    private MaintenanceLog maintenanceLog;

    private MaintenanceLog insertedMaintenanceLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaintenanceLog createEntity() {
        return new MaintenanceLog()
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .maintenanceLogCode(DEFAULT_MAINTENANCE_LOG_CODE)
            .logDate(DEFAULT_LOG_DATE)
            .logType(DEFAULT_LOG_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .cost(DEFAULT_COST)
            .vendor(DEFAULT_VENDOR)
            .nextServiceDate(DEFAULT_NEXT_SERVICE_DATE)
            .note(DEFAULT_NOTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaintenanceLog createUpdatedEntity() {
        return new MaintenanceLog()
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .maintenanceLogCode(UPDATED_MAINTENANCE_LOG_CODE)
            .logDate(UPDATED_LOG_DATE)
            .logType(UPDATED_LOG_TYPE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .vendor(UPDATED_VENDOR)
            .nextServiceDate(UPDATED_NEXT_SERVICE_DATE)
            .note(UPDATED_NOTE);
    }

    @BeforeEach
    void initTest() {
        maintenanceLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMaintenanceLog != null) {
            maintenanceLogRepository.delete(insertedMaintenanceLog);
            maintenanceLogSearchRepository.delete(insertedMaintenanceLog);
            insertedMaintenanceLog = null;
        }
    }

    @Test
    @Transactional
    void createMaintenanceLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        // Create the MaintenanceLog
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);
        var returnedMaintenanceLogDTO = om.readValue(
            restMaintenanceLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaintenanceLogDTO.class
        );

        // Validate the MaintenanceLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaintenanceLog = maintenanceLogMapper.toEntity(returnedMaintenanceLogDTO);
        assertMaintenanceLogUpdatableFieldsEquals(returnedMaintenanceLog, getPersistedMaintenanceLog(returnedMaintenanceLog));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMaintenanceLog = returnedMaintenanceLog;
    }

    @Test
    @Transactional
    void createMaintenanceLogWithExistingId() throws Exception {
        // Create the MaintenanceLog with an existing ID
        maintenanceLog.setId(1L);
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintenanceLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogs() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList
        restMaintenanceLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenanceLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].maintenanceLogCode").value(hasItem(DEFAULT_MAINTENANCE_LOG_CODE)))
            .andExpect(jsonPath("$.[*].logDate").value(hasItem(DEFAULT_LOG_DATE.toString())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].nextServiceDate").value(hasItem(DEFAULT_NEXT_SERVICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaintenanceLogsWithEagerRelationshipsIsEnabled() throws Exception {
        when(maintenanceLogServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaintenanceLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(maintenanceLogServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaintenanceLogsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(maintenanceLogServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaintenanceLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(maintenanceLogRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMaintenanceLog() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get the maintenanceLog
        restMaintenanceLogMockMvc
            .perform(get(ENTITY_API_URL_ID, maintenanceLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintenanceLog.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.maintenanceLogCode").value(DEFAULT_MAINTENANCE_LOG_CODE))
            .andExpect(jsonPath("$.logDate").value(DEFAULT_LOG_DATE.toString()))
            .andExpect(jsonPath("$.logType").value(DEFAULT_LOG_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR))
            .andExpect(jsonPath("$.nextServiceDate").value(DEFAULT_NEXT_SERVICE_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getMaintenanceLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        Long id = maintenanceLog.getId();

        defaultMaintenanceLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaintenanceLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaintenanceLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchCode equals to
        defaultMaintenanceLogFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchCode in
        defaultMaintenanceLogFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchCode is not null
        defaultMaintenanceLogFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchCode contains
        defaultMaintenanceLogFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchCode does not contain
        defaultMaintenanceLogFiltering(
            "branchCode.doesNotContain=" + UPDATED_BRANCH_CODE,
            "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchId equals to
        defaultMaintenanceLogFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchId in
        defaultMaintenanceLogFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchId is not null
        defaultMaintenanceLogFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchId contains
        defaultMaintenanceLogFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where branchId does not contain
        defaultMaintenanceLogFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByMaintenanceLogCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where maintenanceLogCode equals to
        defaultMaintenanceLogFiltering(
            "maintenanceLogCode.equals=" + DEFAULT_MAINTENANCE_LOG_CODE,
            "maintenanceLogCode.equals=" + UPDATED_MAINTENANCE_LOG_CODE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByMaintenanceLogCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where maintenanceLogCode in
        defaultMaintenanceLogFiltering(
            "maintenanceLogCode.in=" + DEFAULT_MAINTENANCE_LOG_CODE + "," + UPDATED_MAINTENANCE_LOG_CODE,
            "maintenanceLogCode.in=" + UPDATED_MAINTENANCE_LOG_CODE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByMaintenanceLogCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where maintenanceLogCode is not null
        defaultMaintenanceLogFiltering("maintenanceLogCode.specified=true", "maintenanceLogCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByMaintenanceLogCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where maintenanceLogCode contains
        defaultMaintenanceLogFiltering(
            "maintenanceLogCode.contains=" + DEFAULT_MAINTENANCE_LOG_CODE,
            "maintenanceLogCode.contains=" + UPDATED_MAINTENANCE_LOG_CODE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByMaintenanceLogCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where maintenanceLogCode does not contain
        defaultMaintenanceLogFiltering(
            "maintenanceLogCode.doesNotContain=" + UPDATED_MAINTENANCE_LOG_CODE,
            "maintenanceLogCode.doesNotContain=" + DEFAULT_MAINTENANCE_LOG_CODE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logDate equals to
        defaultMaintenanceLogFiltering("logDate.equals=" + DEFAULT_LOG_DATE, "logDate.equals=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logDate in
        defaultMaintenanceLogFiltering("logDate.in=" + DEFAULT_LOG_DATE + "," + UPDATED_LOG_DATE, "logDate.in=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logDate is not null
        defaultMaintenanceLogFiltering("logDate.specified=true", "logDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logDate is greater than or equal to
        defaultMaintenanceLogFiltering("logDate.greaterThanOrEqual=" + DEFAULT_LOG_DATE, "logDate.greaterThanOrEqual=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logDate is less than or equal to
        defaultMaintenanceLogFiltering("logDate.lessThanOrEqual=" + DEFAULT_LOG_DATE, "logDate.lessThanOrEqual=" + SMALLER_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logDate is less than
        defaultMaintenanceLogFiltering("logDate.lessThan=" + UPDATED_LOG_DATE, "logDate.lessThan=" + DEFAULT_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logDate is greater than
        defaultMaintenanceLogFiltering("logDate.greaterThan=" + SMALLER_LOG_DATE, "logDate.greaterThan=" + DEFAULT_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logType equals to
        defaultMaintenanceLogFiltering("logType.equals=" + DEFAULT_LOG_TYPE, "logType.equals=" + UPDATED_LOG_TYPE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logType in
        defaultMaintenanceLogFiltering("logType.in=" + DEFAULT_LOG_TYPE + "," + UPDATED_LOG_TYPE, "logType.in=" + UPDATED_LOG_TYPE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByLogTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where logType is not null
        defaultMaintenanceLogFiltering("logType.specified=true", "logType.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where description equals to
        defaultMaintenanceLogFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where description in
        defaultMaintenanceLogFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where description is not null
        defaultMaintenanceLogFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where description contains
        defaultMaintenanceLogFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where description does not contain
        defaultMaintenanceLogFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where cost equals to
        defaultMaintenanceLogFiltering("cost.equals=" + DEFAULT_COST, "cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where cost in
        defaultMaintenanceLogFiltering("cost.in=" + DEFAULT_COST + "," + UPDATED_COST, "cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where cost is not null
        defaultMaintenanceLogFiltering("cost.specified=true", "cost.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where cost is greater than or equal to
        defaultMaintenanceLogFiltering("cost.greaterThanOrEqual=" + DEFAULT_COST, "cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where cost is less than or equal to
        defaultMaintenanceLogFiltering("cost.lessThanOrEqual=" + DEFAULT_COST, "cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where cost is less than
        defaultMaintenanceLogFiltering("cost.lessThan=" + UPDATED_COST, "cost.lessThan=" + DEFAULT_COST);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where cost is greater than
        defaultMaintenanceLogFiltering("cost.greaterThan=" + SMALLER_COST, "cost.greaterThan=" + DEFAULT_COST);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByVendorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where vendor equals to
        defaultMaintenanceLogFiltering("vendor.equals=" + DEFAULT_VENDOR, "vendor.equals=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByVendorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where vendor in
        defaultMaintenanceLogFiltering("vendor.in=" + DEFAULT_VENDOR + "," + UPDATED_VENDOR, "vendor.in=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByVendorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where vendor is not null
        defaultMaintenanceLogFiltering("vendor.specified=true", "vendor.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByVendorContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where vendor contains
        defaultMaintenanceLogFiltering("vendor.contains=" + DEFAULT_VENDOR, "vendor.contains=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByVendorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where vendor does not contain
        defaultMaintenanceLogFiltering("vendor.doesNotContain=" + UPDATED_VENDOR, "vendor.doesNotContain=" + DEFAULT_VENDOR);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNextServiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where nextServiceDate equals to
        defaultMaintenanceLogFiltering(
            "nextServiceDate.equals=" + DEFAULT_NEXT_SERVICE_DATE,
            "nextServiceDate.equals=" + UPDATED_NEXT_SERVICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNextServiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where nextServiceDate in
        defaultMaintenanceLogFiltering(
            "nextServiceDate.in=" + DEFAULT_NEXT_SERVICE_DATE + "," + UPDATED_NEXT_SERVICE_DATE,
            "nextServiceDate.in=" + UPDATED_NEXT_SERVICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNextServiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where nextServiceDate is not null
        defaultMaintenanceLogFiltering("nextServiceDate.specified=true", "nextServiceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNextServiceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where nextServiceDate is greater than or equal to
        defaultMaintenanceLogFiltering(
            "nextServiceDate.greaterThanOrEqual=" + DEFAULT_NEXT_SERVICE_DATE,
            "nextServiceDate.greaterThanOrEqual=" + UPDATED_NEXT_SERVICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNextServiceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where nextServiceDate is less than or equal to
        defaultMaintenanceLogFiltering(
            "nextServiceDate.lessThanOrEqual=" + DEFAULT_NEXT_SERVICE_DATE,
            "nextServiceDate.lessThanOrEqual=" + SMALLER_NEXT_SERVICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNextServiceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where nextServiceDate is less than
        defaultMaintenanceLogFiltering(
            "nextServiceDate.lessThan=" + UPDATED_NEXT_SERVICE_DATE,
            "nextServiceDate.lessThan=" + DEFAULT_NEXT_SERVICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNextServiceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where nextServiceDate is greater than
        defaultMaintenanceLogFiltering(
            "nextServiceDate.greaterThan=" + SMALLER_NEXT_SERVICE_DATE,
            "nextServiceDate.greaterThan=" + DEFAULT_NEXT_SERVICE_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where note equals to
        defaultMaintenanceLogFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where note in
        defaultMaintenanceLogFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where note is not null
        defaultMaintenanceLogFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where note contains
        defaultMaintenanceLogFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        // Get all the maintenanceLogList where note does not contain
        defaultMaintenanceLogFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllMaintenanceLogsByAssetIsEqualToSomething() throws Exception {
        AssetRegister asset;
        if (TestUtil.findAll(em, AssetRegister.class).isEmpty()) {
            maintenanceLogRepository.saveAndFlush(maintenanceLog);
            asset = AssetRegisterResourceIT.createEntity();
        } else {
            asset = TestUtil.findAll(em, AssetRegister.class).get(0);
        }
        em.persist(asset);
        em.flush();
        maintenanceLog.setAsset(asset);
        maintenanceLogRepository.saveAndFlush(maintenanceLog);
        Long assetId = asset.getId();
        // Get all the maintenanceLogList where asset equals to assetId
        defaultMaintenanceLogShouldBeFound("assetId.equals=" + assetId);

        // Get all the maintenanceLogList where asset equals to (assetId + 1)
        defaultMaintenanceLogShouldNotBeFound("assetId.equals=" + (assetId + 1));
    }

    private void defaultMaintenanceLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaintenanceLogShouldBeFound(shouldBeFound);
        defaultMaintenanceLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaintenanceLogShouldBeFound(String filter) throws Exception {
        restMaintenanceLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenanceLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].maintenanceLogCode").value(hasItem(DEFAULT_MAINTENANCE_LOG_CODE)))
            .andExpect(jsonPath("$.[*].logDate").value(hasItem(DEFAULT_LOG_DATE.toString())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].nextServiceDate").value(hasItem(DEFAULT_NEXT_SERVICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restMaintenanceLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaintenanceLogShouldNotBeFound(String filter) throws Exception {
        restMaintenanceLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaintenanceLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaintenanceLog() throws Exception {
        // Get the maintenanceLog
        restMaintenanceLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaintenanceLog() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        maintenanceLogSearchRepository.save(maintenanceLog);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());

        // Update the maintenanceLog
        MaintenanceLog updatedMaintenanceLog = maintenanceLogRepository.findById(maintenanceLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaintenanceLog are not directly saved in db
        em.detach(updatedMaintenanceLog);
        updatedMaintenanceLog
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .maintenanceLogCode(UPDATED_MAINTENANCE_LOG_CODE)
            .logDate(UPDATED_LOG_DATE)
            .logType(UPDATED_LOG_TYPE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .vendor(UPDATED_VENDOR)
            .nextServiceDate(UPDATED_NEXT_SERVICE_DATE)
            .note(UPDATED_NOTE);
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(updatedMaintenanceLog);

        restMaintenanceLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maintenanceLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaintenanceLogToMatchAllProperties(updatedMaintenanceLog);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MaintenanceLog> maintenanceLogSearchList = Streamable.of(maintenanceLogSearchRepository.findAll()).toList();
                MaintenanceLog testMaintenanceLogSearch = maintenanceLogSearchList.get(searchDatabaseSizeAfter - 1);

                assertMaintenanceLogAllPropertiesEquals(testMaintenanceLogSearch, updatedMaintenanceLog);
            });
    }

    @Test
    @Transactional
    void putNonExistingMaintenanceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        maintenanceLog.setId(longCount.incrementAndGet());

        // Create the MaintenanceLog
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maintenanceLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaintenanceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        maintenanceLog.setId(longCount.incrementAndGet());

        // Create the MaintenanceLog
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maintenanceLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaintenanceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        maintenanceLog.setId(longCount.incrementAndGet());

        // Create the MaintenanceLog
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMaintenanceLogWithPatch() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maintenanceLog using partial update
        MaintenanceLog partialUpdatedMaintenanceLog = new MaintenanceLog();
        partialUpdatedMaintenanceLog.setId(maintenanceLog.getId());

        partialUpdatedMaintenanceLog
            .maintenanceLogCode(UPDATED_MAINTENANCE_LOG_CODE)
            .logDate(UPDATED_LOG_DATE)
            .description(UPDATED_DESCRIPTION);

        restMaintenanceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenanceLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaintenanceLog))
            )
            .andExpect(status().isOk());

        // Validate the MaintenanceLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaintenanceLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaintenanceLog, maintenanceLog),
            getPersistedMaintenanceLog(maintenanceLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaintenanceLogWithPatch() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maintenanceLog using partial update
        MaintenanceLog partialUpdatedMaintenanceLog = new MaintenanceLog();
        partialUpdatedMaintenanceLog.setId(maintenanceLog.getId());

        partialUpdatedMaintenanceLog
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .maintenanceLogCode(UPDATED_MAINTENANCE_LOG_CODE)
            .logDate(UPDATED_LOG_DATE)
            .logType(UPDATED_LOG_TYPE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .vendor(UPDATED_VENDOR)
            .nextServiceDate(UPDATED_NEXT_SERVICE_DATE)
            .note(UPDATED_NOTE);

        restMaintenanceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenanceLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaintenanceLog))
            )
            .andExpect(status().isOk());

        // Validate the MaintenanceLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaintenanceLogUpdatableFieldsEquals(partialUpdatedMaintenanceLog, getPersistedMaintenanceLog(partialUpdatedMaintenanceLog));
    }

    @Test
    @Transactional
    void patchNonExistingMaintenanceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        maintenanceLog.setId(longCount.incrementAndGet());

        // Create the MaintenanceLog
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maintenanceLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maintenanceLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaintenanceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        maintenanceLog.setId(longCount.incrementAndGet());

        // Create the MaintenanceLog
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maintenanceLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaintenanceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        maintenanceLog.setId(longCount.incrementAndGet());

        // Create the MaintenanceLog
        MaintenanceLogDTO maintenanceLogDTO = maintenanceLogMapper.toDto(maintenanceLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(maintenanceLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaintenanceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMaintenanceLog() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);
        maintenanceLogRepository.save(maintenanceLog);
        maintenanceLogSearchRepository.save(maintenanceLog);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the maintenanceLog
        restMaintenanceLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, maintenanceLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(maintenanceLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMaintenanceLog() throws Exception {
        // Initialize the database
        insertedMaintenanceLog = maintenanceLogRepository.saveAndFlush(maintenanceLog);
        maintenanceLogSearchRepository.save(maintenanceLog);

        // Search the maintenanceLog
        restMaintenanceLogMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + maintenanceLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenanceLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].maintenanceLogCode").value(hasItem(DEFAULT_MAINTENANCE_LOG_CODE)))
            .andExpect(jsonPath("$.[*].logDate").value(hasItem(DEFAULT_LOG_DATE.toString())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].nextServiceDate").value(hasItem(DEFAULT_NEXT_SERVICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    protected long getRepositoryCount() {
        return maintenanceLogRepository.count();
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

    protected MaintenanceLog getPersistedMaintenanceLog(MaintenanceLog maintenanceLog) {
        return maintenanceLogRepository.findById(maintenanceLog.getId()).orElseThrow();
    }

    protected void assertPersistedMaintenanceLogToMatchAllProperties(MaintenanceLog expectedMaintenanceLog) {
        assertMaintenanceLogAllPropertiesEquals(expectedMaintenanceLog, getPersistedMaintenanceLog(expectedMaintenanceLog));
    }

    protected void assertPersistedMaintenanceLogToMatchUpdatableProperties(MaintenanceLog expectedMaintenanceLog) {
        assertMaintenanceLogAllUpdatablePropertiesEquals(expectedMaintenanceLog, getPersistedMaintenanceLog(expectedMaintenanceLog));
    }
}
