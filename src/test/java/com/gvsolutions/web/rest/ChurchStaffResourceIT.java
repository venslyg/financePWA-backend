package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.ChurchStaffAsserts.*;
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
import com.gvsolutions.domain.ChurchStaff;
import com.gvsolutions.domain.enumeration.StaffType;
import com.gvsolutions.repository.ChurchStaffRepository;
import com.gvsolutions.repository.search.ChurchStaffSearchRepository;
import com.gvsolutions.service.dto.ChurchStaffDTO;
import com.gvsolutions.service.mapper.ChurchStaffMapper;
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
 * Integration tests for the {@link ChurchStaffResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChurchStaffResourceIT {

    private static final String DEFAULT_STAFF_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STAFF_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final StaffType DEFAULT_STAFF_TYPE = StaffType.PASTORAL;
    private static final StaffType UPDATED_STAFF_TYPE = StaffType.FULL_TIME;

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_HOURLY_RATE_OR_MONTHLY_SALARY = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/church-staffs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/church-staffs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChurchStaffRepository churchStaffRepository;

    @Autowired
    private ChurchStaffMapper churchStaffMapper;

    @Autowired
    private ChurchStaffSearchRepository churchStaffSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChurchStaffMockMvc;

    private ChurchStaff churchStaff;

    private ChurchStaff insertedChurchStaff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchStaff createEntity() {
        return new ChurchStaff()
            .staffCode(DEFAULT_STAFF_CODE)
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchId(DEFAULT_BRANCH_ID)
            .fullName(DEFAULT_FULL_NAME)
            .position(DEFAULT_POSITION)
            .staffType(DEFAULT_STAFF_TYPE)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .hourlyRateOrMonthlySalary(DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchStaff createUpdatedEntity() {
        return new ChurchStaff()
            .staffCode(UPDATED_STAFF_CODE)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .fullName(UPDATED_FULL_NAME)
            .position(UPDATED_POSITION)
            .staffType(UPDATED_STAFF_TYPE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .hourlyRateOrMonthlySalary(UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        churchStaff = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChurchStaff != null) {
            churchStaffRepository.delete(insertedChurchStaff);
            churchStaffSearchRepository.delete(insertedChurchStaff);
            insertedChurchStaff = null;
        }
    }

    @Test
    @Transactional
    void createChurchStaff() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        // Create the ChurchStaff
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);
        var returnedChurchStaffDTO = om.readValue(
            restChurchStaffMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(churchStaffDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChurchStaffDTO.class
        );

        // Validate the ChurchStaff in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChurchStaff = churchStaffMapper.toEntity(returnedChurchStaffDTO);
        assertChurchStaffUpdatableFieldsEquals(returnedChurchStaff, getPersistedChurchStaff(returnedChurchStaff));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedChurchStaff = returnedChurchStaff;
    }

    @Test
    @Transactional
    void createChurchStaffWithExistingId() throws Exception {
        // Create the ChurchStaff with an existing ID
        churchStaff.setId(1L);
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(churchStaffDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllChurchStaffs() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList
        restChurchStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchStaff.getId().intValue())))
            .andExpect(jsonPath("$.[*].staffCode").value(hasItem(DEFAULT_STAFF_CODE)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].staffType").value(hasItem(DEFAULT_STAFF_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].hourlyRateOrMonthlySalary").value(hasItem(sameNumber(DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getChurchStaff() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get the churchStaff
        restChurchStaffMockMvc
            .perform(get(ENTITY_API_URL_ID, churchStaff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(churchStaff.getId().intValue()))
            .andExpect(jsonPath("$.staffCode").value(DEFAULT_STAFF_CODE))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.staffType").value(DEFAULT_STAFF_TYPE.toString()))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.hourlyRateOrMonthlySalary").value(sameNumber(DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getChurchStaffsByIdFiltering() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        Long id = churchStaff.getId();

        defaultChurchStaffFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultChurchStaffFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultChurchStaffFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffCode equals to
        defaultChurchStaffFiltering("staffCode.equals=" + DEFAULT_STAFF_CODE, "staffCode.equals=" + UPDATED_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffCode in
        defaultChurchStaffFiltering("staffCode.in=" + DEFAULT_STAFF_CODE + "," + UPDATED_STAFF_CODE, "staffCode.in=" + UPDATED_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffCode is not null
        defaultChurchStaffFiltering("staffCode.specified=true", "staffCode.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffCode contains
        defaultChurchStaffFiltering("staffCode.contains=" + DEFAULT_STAFF_CODE, "staffCode.contains=" + UPDATED_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffCode does not contain
        defaultChurchStaffFiltering("staffCode.doesNotContain=" + UPDATED_STAFF_CODE, "staffCode.doesNotContain=" + DEFAULT_STAFF_CODE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchCode equals to
        defaultChurchStaffFiltering("branchCode.equals=" + DEFAULT_BRANCH_CODE, "branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchCode in
        defaultChurchStaffFiltering(
            "branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE,
            "branchCode.in=" + UPDATED_BRANCH_CODE
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchCode is not null
        defaultChurchStaffFiltering("branchCode.specified=true", "branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchCode contains
        defaultChurchStaffFiltering("branchCode.contains=" + DEFAULT_BRANCH_CODE, "branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchCode does not contain
        defaultChurchStaffFiltering("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE, "branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchId equals to
        defaultChurchStaffFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchId in
        defaultChurchStaffFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchId is not null
        defaultChurchStaffFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchIdContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchId contains
        defaultChurchStaffFiltering("branchId.contains=" + DEFAULT_BRANCH_ID, "branchId.contains=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByBranchIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where branchId does not contain
        defaultChurchStaffFiltering("branchId.doesNotContain=" + UPDATED_BRANCH_ID, "branchId.doesNotContain=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where fullName equals to
        defaultChurchStaffFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where fullName in
        defaultChurchStaffFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where fullName is not null
        defaultChurchStaffFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where fullName contains
        defaultChurchStaffFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where fullName does not contain
        defaultChurchStaffFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where position equals to
        defaultChurchStaffFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where position in
        defaultChurchStaffFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where position is not null
        defaultChurchStaffFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where position contains
        defaultChurchStaffFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where position does not contain
        defaultChurchStaffFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffType equals to
        defaultChurchStaffFiltering("staffType.equals=" + DEFAULT_STAFF_TYPE, "staffType.equals=" + UPDATED_STAFF_TYPE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffType in
        defaultChurchStaffFiltering("staffType.in=" + DEFAULT_STAFF_TYPE + "," + UPDATED_STAFF_TYPE, "staffType.in=" + UPDATED_STAFF_TYPE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByStaffTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where staffType is not null
        defaultChurchStaffFiltering("staffType.specified=true", "staffType.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where contactNumber equals to
        defaultChurchStaffFiltering("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER, "contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where contactNumber in
        defaultChurchStaffFiltering(
            "contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER,
            "contactNumber.in=" + UPDATED_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where contactNumber is not null
        defaultChurchStaffFiltering("contactNumber.specified=true", "contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where contactNumber contains
        defaultChurchStaffFiltering("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER, "contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where contactNumber does not contain
        defaultChurchStaffFiltering(
            "contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER,
            "contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByHourlyRateOrMonthlySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where hourlyRateOrMonthlySalary equals to
        defaultChurchStaffFiltering(
            "hourlyRateOrMonthlySalary.equals=" + DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY,
            "hourlyRateOrMonthlySalary.equals=" + UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByHourlyRateOrMonthlySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where hourlyRateOrMonthlySalary in
        defaultChurchStaffFiltering(
            "hourlyRateOrMonthlySalary.in=" + DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY + "," + UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY,
            "hourlyRateOrMonthlySalary.in=" + UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByHourlyRateOrMonthlySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where hourlyRateOrMonthlySalary is not null
        defaultChurchStaffFiltering("hourlyRateOrMonthlySalary.specified=true", "hourlyRateOrMonthlySalary.specified=false");
    }

    @Test
    @Transactional
    void getAllChurchStaffsByHourlyRateOrMonthlySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where hourlyRateOrMonthlySalary is greater than or equal to
        defaultChurchStaffFiltering(
            "hourlyRateOrMonthlySalary.greaterThanOrEqual=" + DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY,
            "hourlyRateOrMonthlySalary.greaterThanOrEqual=" + UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByHourlyRateOrMonthlySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where hourlyRateOrMonthlySalary is less than or equal to
        defaultChurchStaffFiltering(
            "hourlyRateOrMonthlySalary.lessThanOrEqual=" + DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY,
            "hourlyRateOrMonthlySalary.lessThanOrEqual=" + SMALLER_HOURLY_RATE_OR_MONTHLY_SALARY
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByHourlyRateOrMonthlySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where hourlyRateOrMonthlySalary is less than
        defaultChurchStaffFiltering(
            "hourlyRateOrMonthlySalary.lessThan=" + UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY,
            "hourlyRateOrMonthlySalary.lessThan=" + DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByHourlyRateOrMonthlySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where hourlyRateOrMonthlySalary is greater than
        defaultChurchStaffFiltering(
            "hourlyRateOrMonthlySalary.greaterThan=" + SMALLER_HOURLY_RATE_OR_MONTHLY_SALARY,
            "hourlyRateOrMonthlySalary.greaterThan=" + DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY
        );
    }

    @Test
    @Transactional
    void getAllChurchStaffsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where isActive equals to
        defaultChurchStaffFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where isActive in
        defaultChurchStaffFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllChurchStaffsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        // Get all the churchStaffList where isActive is not null
        defaultChurchStaffFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultChurchStaffFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultChurchStaffShouldBeFound(shouldBeFound);
        defaultChurchStaffShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChurchStaffShouldBeFound(String filter) throws Exception {
        restChurchStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchStaff.getId().intValue())))
            .andExpect(jsonPath("$.[*].staffCode").value(hasItem(DEFAULT_STAFF_CODE)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].staffType").value(hasItem(DEFAULT_STAFF_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].hourlyRateOrMonthlySalary").value(hasItem(sameNumber(DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restChurchStaffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChurchStaffShouldNotBeFound(String filter) throws Exception {
        restChurchStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChurchStaffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChurchStaff() throws Exception {
        // Get the churchStaff
        restChurchStaffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChurchStaff() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        churchStaffSearchRepository.save(churchStaff);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());

        // Update the churchStaff
        ChurchStaff updatedChurchStaff = churchStaffRepository.findById(churchStaff.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChurchStaff are not directly saved in db
        em.detach(updatedChurchStaff);
        updatedChurchStaff
            .staffCode(UPDATED_STAFF_CODE)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .fullName(UPDATED_FULL_NAME)
            .position(UPDATED_POSITION)
            .staffType(UPDATED_STAFF_TYPE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .hourlyRateOrMonthlySalary(UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY)
            .isActive(UPDATED_IS_ACTIVE);
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(updatedChurchStaff);

        restChurchStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, churchStaffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(churchStaffDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChurchStaffToMatchAllProperties(updatedChurchStaff);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ChurchStaff> churchStaffSearchList = Streamable.of(churchStaffSearchRepository.findAll()).toList();
                ChurchStaff testChurchStaffSearch = churchStaffSearchList.get(searchDatabaseSizeAfter - 1);

                assertChurchStaffAllPropertiesEquals(testChurchStaffSearch, updatedChurchStaff);
            });
    }

    @Test
    @Transactional
    void putNonExistingChurchStaff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        churchStaff.setId(longCount.incrementAndGet());

        // Create the ChurchStaff
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, churchStaffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(churchStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchChurchStaff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        churchStaff.setId(longCount.incrementAndGet());

        // Create the ChurchStaff
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(churchStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChurchStaff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        churchStaff.setId(longCount.incrementAndGet());

        // Create the ChurchStaff
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchStaffMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(churchStaffDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateChurchStaffWithPatch() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the churchStaff using partial update
        ChurchStaff partialUpdatedChurchStaff = new ChurchStaff();
        partialUpdatedChurchStaff.setId(churchStaff.getId());

        partialUpdatedChurchStaff
            .staffCode(UPDATED_STAFF_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .staffType(UPDATED_STAFF_TYPE)
            .isActive(UPDATED_IS_ACTIVE);

        restChurchStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChurchStaff.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChurchStaff))
            )
            .andExpect(status().isOk());

        // Validate the ChurchStaff in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChurchStaffUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChurchStaff, churchStaff),
            getPersistedChurchStaff(churchStaff)
        );
    }

    @Test
    @Transactional
    void fullUpdateChurchStaffWithPatch() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the churchStaff using partial update
        ChurchStaff partialUpdatedChurchStaff = new ChurchStaff();
        partialUpdatedChurchStaff.setId(churchStaff.getId());

        partialUpdatedChurchStaff
            .staffCode(UPDATED_STAFF_CODE)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchId(UPDATED_BRANCH_ID)
            .fullName(UPDATED_FULL_NAME)
            .position(UPDATED_POSITION)
            .staffType(UPDATED_STAFF_TYPE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .hourlyRateOrMonthlySalary(UPDATED_HOURLY_RATE_OR_MONTHLY_SALARY)
            .isActive(UPDATED_IS_ACTIVE);

        restChurchStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChurchStaff.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChurchStaff))
            )
            .andExpect(status().isOk());

        // Validate the ChurchStaff in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChurchStaffUpdatableFieldsEquals(partialUpdatedChurchStaff, getPersistedChurchStaff(partialUpdatedChurchStaff));
    }

    @Test
    @Transactional
    void patchNonExistingChurchStaff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        churchStaff.setId(longCount.incrementAndGet());

        // Create the ChurchStaff
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, churchStaffDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(churchStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChurchStaff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        churchStaff.setId(longCount.incrementAndGet());

        // Create the ChurchStaff
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(churchStaffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChurchStaff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        churchStaff.setId(longCount.incrementAndGet());

        // Create the ChurchStaff
        ChurchStaffDTO churchStaffDTO = churchStaffMapper.toDto(churchStaff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChurchStaffMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(churchStaffDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChurchStaff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteChurchStaff() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);
        churchStaffRepository.save(churchStaff);
        churchStaffSearchRepository.save(churchStaff);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the churchStaff
        restChurchStaffMockMvc
            .perform(delete(ENTITY_API_URL_ID, churchStaff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(churchStaffSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchChurchStaff() throws Exception {
        // Initialize the database
        insertedChurchStaff = churchStaffRepository.saveAndFlush(churchStaff);
        churchStaffSearchRepository.save(churchStaff);

        // Search the churchStaff
        restChurchStaffMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + churchStaff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchStaff.getId().intValue())))
            .andExpect(jsonPath("$.[*].staffCode").value(hasItem(DEFAULT_STAFF_CODE)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].staffType").value(hasItem(DEFAULT_STAFF_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].hourlyRateOrMonthlySalary").value(hasItem(sameNumber(DEFAULT_HOURLY_RATE_OR_MONTHLY_SALARY))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    protected long getRepositoryCount() {
        return churchStaffRepository.count();
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

    protected ChurchStaff getPersistedChurchStaff(ChurchStaff churchStaff) {
        return churchStaffRepository.findById(churchStaff.getId()).orElseThrow();
    }

    protected void assertPersistedChurchStaffToMatchAllProperties(ChurchStaff expectedChurchStaff) {
        assertChurchStaffAllPropertiesEquals(expectedChurchStaff, getPersistedChurchStaff(expectedChurchStaff));
    }

    protected void assertPersistedChurchStaffToMatchUpdatableProperties(ChurchStaff expectedChurchStaff) {
        assertChurchStaffAllUpdatablePropertiesEquals(expectedChurchStaff, getPersistedChurchStaff(expectedChurchStaff));
    }
}
