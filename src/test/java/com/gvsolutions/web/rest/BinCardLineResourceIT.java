package com.gvsolutions.web.rest;

import static com.gvsolutions.domain.BinCardLineAsserts.*;
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
import com.gvsolutions.domain.BinCardLine;
import com.gvsolutions.repository.BinCardLineRepository;
import com.gvsolutions.repository.search.BinCardLineSearchRepository;
import com.gvsolutions.service.dto.BinCardLineDTO;
import com.gvsolutions.service.mapper.BinCardLineMapper;
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
 * Integration tests for the {@link BinCardLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BinCardLineResourceIT {

    private static final String DEFAULT_INVENTORY_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY_ITEM_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REFERENCE_NO = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY_IN = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_IN = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY_IN = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_QUANTITY_OUT = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY_OUT = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY_OUT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_RUNNING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RUNNING_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RUNNING_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/bin-card-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/bin-card-lines/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BinCardLineRepository binCardLineRepository;

    @Autowired
    private BinCardLineMapper binCardLineMapper;

    @Autowired
    private BinCardLineSearchRepository binCardLineSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBinCardLineMockMvc;

    private BinCardLine binCardLine;

    private BinCardLine insertedBinCardLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BinCardLine createEntity() {
        return new BinCardLine()
            .inventoryItemCode(DEFAULT_INVENTORY_ITEM_CODE)
            .date(DEFAULT_DATE)
            .referenceNo(DEFAULT_REFERENCE_NO)
            .description(DEFAULT_DESCRIPTION)
            .quantityIn(DEFAULT_QUANTITY_IN)
            .quantityOut(DEFAULT_QUANTITY_OUT)
            .runningBalance(DEFAULT_RUNNING_BALANCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BinCardLine createUpdatedEntity() {
        return new BinCardLine()
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .quantityIn(UPDATED_QUANTITY_IN)
            .quantityOut(UPDATED_QUANTITY_OUT)
            .runningBalance(UPDATED_RUNNING_BALANCE);
    }

    @BeforeEach
    void initTest() {
        binCardLine = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBinCardLine != null) {
            binCardLineRepository.delete(insertedBinCardLine);
            binCardLineSearchRepository.delete(insertedBinCardLine);
            insertedBinCardLine = null;
        }
    }

    @Test
    @Transactional
    void createBinCardLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        // Create the BinCardLine
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);
        var returnedBinCardLineDTO = om.readValue(
            restBinCardLineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(binCardLineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BinCardLineDTO.class
        );

        // Validate the BinCardLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBinCardLine = binCardLineMapper.toEntity(returnedBinCardLineDTO);
        assertBinCardLineUpdatableFieldsEquals(returnedBinCardLine, getPersistedBinCardLine(returnedBinCardLine));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedBinCardLine = returnedBinCardLine;
    }

    @Test
    @Transactional
    void createBinCardLineWithExistingId() throws Exception {
        // Create the BinCardLine with an existing ID
        binCardLine.setId(1L);
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restBinCardLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(binCardLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllBinCardLines() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList
        restBinCardLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(binCardLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].inventoryItemCode").value(hasItem(DEFAULT_INVENTORY_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantityIn").value(hasItem(sameNumber(DEFAULT_QUANTITY_IN))))
            .andExpect(jsonPath("$.[*].quantityOut").value(hasItem(sameNumber(DEFAULT_QUANTITY_OUT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))));
    }

    @Test
    @Transactional
    void getBinCardLine() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get the binCardLine
        restBinCardLineMockMvc
            .perform(get(ENTITY_API_URL_ID, binCardLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(binCardLine.getId().intValue()))
            .andExpect(jsonPath("$.inventoryItemCode").value(DEFAULT_INVENTORY_ITEM_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.referenceNo").value(DEFAULT_REFERENCE_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantityIn").value(sameNumber(DEFAULT_QUANTITY_IN)))
            .andExpect(jsonPath("$.quantityOut").value(sameNumber(DEFAULT_QUANTITY_OUT)))
            .andExpect(jsonPath("$.runningBalance").value(sameNumber(DEFAULT_RUNNING_BALANCE)));
    }

    @Test
    @Transactional
    void getBinCardLinesByIdFiltering() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        Long id = binCardLine.getId();

        defaultBinCardLineFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBinCardLineFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBinCardLineFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByInventoryItemCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where inventoryItemCode equals to
        defaultBinCardLineFiltering(
            "inventoryItemCode.equals=" + DEFAULT_INVENTORY_ITEM_CODE,
            "inventoryItemCode.equals=" + UPDATED_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByInventoryItemCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where inventoryItemCode in
        defaultBinCardLineFiltering(
            "inventoryItemCode.in=" + DEFAULT_INVENTORY_ITEM_CODE + "," + UPDATED_INVENTORY_ITEM_CODE,
            "inventoryItemCode.in=" + UPDATED_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByInventoryItemCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where inventoryItemCode is not null
        defaultBinCardLineFiltering("inventoryItemCode.specified=true", "inventoryItemCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBinCardLinesByInventoryItemCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where inventoryItemCode contains
        defaultBinCardLineFiltering(
            "inventoryItemCode.contains=" + DEFAULT_INVENTORY_ITEM_CODE,
            "inventoryItemCode.contains=" + UPDATED_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByInventoryItemCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where inventoryItemCode does not contain
        defaultBinCardLineFiltering(
            "inventoryItemCode.doesNotContain=" + UPDATED_INVENTORY_ITEM_CODE,
            "inventoryItemCode.doesNotContain=" + DEFAULT_INVENTORY_ITEM_CODE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where date equals to
        defaultBinCardLineFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where date in
        defaultBinCardLineFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where date is not null
        defaultBinCardLineFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where date is greater than or equal to
        defaultBinCardLineFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where date is less than or equal to
        defaultBinCardLineFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where date is less than
        defaultBinCardLineFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where date is greater than
        defaultBinCardLineFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByReferenceNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where referenceNo equals to
        defaultBinCardLineFiltering("referenceNo.equals=" + DEFAULT_REFERENCE_NO, "referenceNo.equals=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByReferenceNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where referenceNo in
        defaultBinCardLineFiltering(
            "referenceNo.in=" + DEFAULT_REFERENCE_NO + "," + UPDATED_REFERENCE_NO,
            "referenceNo.in=" + UPDATED_REFERENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByReferenceNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where referenceNo is not null
        defaultBinCardLineFiltering("referenceNo.specified=true", "referenceNo.specified=false");
    }

    @Test
    @Transactional
    void getAllBinCardLinesByReferenceNoContainsSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where referenceNo contains
        defaultBinCardLineFiltering("referenceNo.contains=" + DEFAULT_REFERENCE_NO, "referenceNo.contains=" + UPDATED_REFERENCE_NO);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByReferenceNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where referenceNo does not contain
        defaultBinCardLineFiltering(
            "referenceNo.doesNotContain=" + UPDATED_REFERENCE_NO,
            "referenceNo.doesNotContain=" + DEFAULT_REFERENCE_NO
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where description equals to
        defaultBinCardLineFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where description in
        defaultBinCardLineFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where description is not null
        defaultBinCardLineFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where description contains
        defaultBinCardLineFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where description does not contain
        defaultBinCardLineFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityInIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityIn equals to
        defaultBinCardLineFiltering("quantityIn.equals=" + DEFAULT_QUANTITY_IN, "quantityIn.equals=" + UPDATED_QUANTITY_IN);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityInIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityIn in
        defaultBinCardLineFiltering(
            "quantityIn.in=" + DEFAULT_QUANTITY_IN + "," + UPDATED_QUANTITY_IN,
            "quantityIn.in=" + UPDATED_QUANTITY_IN
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityInIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityIn is not null
        defaultBinCardLineFiltering("quantityIn.specified=true", "quantityIn.specified=false");
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityIn is greater than or equal to
        defaultBinCardLineFiltering(
            "quantityIn.greaterThanOrEqual=" + DEFAULT_QUANTITY_IN,
            "quantityIn.greaterThanOrEqual=" + UPDATED_QUANTITY_IN
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityInIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityIn is less than or equal to
        defaultBinCardLineFiltering(
            "quantityIn.lessThanOrEqual=" + DEFAULT_QUANTITY_IN,
            "quantityIn.lessThanOrEqual=" + SMALLER_QUANTITY_IN
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityInIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityIn is less than
        defaultBinCardLineFiltering("quantityIn.lessThan=" + UPDATED_QUANTITY_IN, "quantityIn.lessThan=" + DEFAULT_QUANTITY_IN);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityInIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityIn is greater than
        defaultBinCardLineFiltering("quantityIn.greaterThan=" + SMALLER_QUANTITY_IN, "quantityIn.greaterThan=" + DEFAULT_QUANTITY_IN);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityOutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityOut equals to
        defaultBinCardLineFiltering("quantityOut.equals=" + DEFAULT_QUANTITY_OUT, "quantityOut.equals=" + UPDATED_QUANTITY_OUT);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityOutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityOut in
        defaultBinCardLineFiltering(
            "quantityOut.in=" + DEFAULT_QUANTITY_OUT + "," + UPDATED_QUANTITY_OUT,
            "quantityOut.in=" + UPDATED_QUANTITY_OUT
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityOutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityOut is not null
        defaultBinCardLineFiltering("quantityOut.specified=true", "quantityOut.specified=false");
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityOutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityOut is greater than or equal to
        defaultBinCardLineFiltering(
            "quantityOut.greaterThanOrEqual=" + DEFAULT_QUANTITY_OUT,
            "quantityOut.greaterThanOrEqual=" + UPDATED_QUANTITY_OUT
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityOutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityOut is less than or equal to
        defaultBinCardLineFiltering(
            "quantityOut.lessThanOrEqual=" + DEFAULT_QUANTITY_OUT,
            "quantityOut.lessThanOrEqual=" + SMALLER_QUANTITY_OUT
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityOutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityOut is less than
        defaultBinCardLineFiltering("quantityOut.lessThan=" + UPDATED_QUANTITY_OUT, "quantityOut.lessThan=" + DEFAULT_QUANTITY_OUT);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByQuantityOutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where quantityOut is greater than
        defaultBinCardLineFiltering("quantityOut.greaterThan=" + SMALLER_QUANTITY_OUT, "quantityOut.greaterThan=" + DEFAULT_QUANTITY_OUT);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByRunningBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where runningBalance equals to
        defaultBinCardLineFiltering("runningBalance.equals=" + DEFAULT_RUNNING_BALANCE, "runningBalance.equals=" + UPDATED_RUNNING_BALANCE);
    }

    @Test
    @Transactional
    void getAllBinCardLinesByRunningBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where runningBalance in
        defaultBinCardLineFiltering(
            "runningBalance.in=" + DEFAULT_RUNNING_BALANCE + "," + UPDATED_RUNNING_BALANCE,
            "runningBalance.in=" + UPDATED_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByRunningBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where runningBalance is not null
        defaultBinCardLineFiltering("runningBalance.specified=true", "runningBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllBinCardLinesByRunningBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where runningBalance is greater than or equal to
        defaultBinCardLineFiltering(
            "runningBalance.greaterThanOrEqual=" + DEFAULT_RUNNING_BALANCE,
            "runningBalance.greaterThanOrEqual=" + UPDATED_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByRunningBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where runningBalance is less than or equal to
        defaultBinCardLineFiltering(
            "runningBalance.lessThanOrEqual=" + DEFAULT_RUNNING_BALANCE,
            "runningBalance.lessThanOrEqual=" + SMALLER_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByRunningBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where runningBalance is less than
        defaultBinCardLineFiltering(
            "runningBalance.lessThan=" + UPDATED_RUNNING_BALANCE,
            "runningBalance.lessThan=" + DEFAULT_RUNNING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBinCardLinesByRunningBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        // Get all the binCardLineList where runningBalance is greater than
        defaultBinCardLineFiltering(
            "runningBalance.greaterThan=" + SMALLER_RUNNING_BALANCE,
            "runningBalance.greaterThan=" + DEFAULT_RUNNING_BALANCE
        );
    }

    private void defaultBinCardLineFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBinCardLineShouldBeFound(shouldBeFound);
        defaultBinCardLineShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBinCardLineShouldBeFound(String filter) throws Exception {
        restBinCardLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(binCardLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].inventoryItemCode").value(hasItem(DEFAULT_INVENTORY_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantityIn").value(hasItem(sameNumber(DEFAULT_QUANTITY_IN))))
            .andExpect(jsonPath("$.[*].quantityOut").value(hasItem(sameNumber(DEFAULT_QUANTITY_OUT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))));

        // Check, that the count call also returns 1
        restBinCardLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBinCardLineShouldNotBeFound(String filter) throws Exception {
        restBinCardLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBinCardLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBinCardLine() throws Exception {
        // Get the binCardLine
        restBinCardLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBinCardLine() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        binCardLineSearchRepository.save(binCardLine);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());

        // Update the binCardLine
        BinCardLine updatedBinCardLine = binCardLineRepository.findById(binCardLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBinCardLine are not directly saved in db
        em.detach(updatedBinCardLine);
        updatedBinCardLine
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .quantityIn(UPDATED_QUANTITY_IN)
            .quantityOut(UPDATED_QUANTITY_OUT)
            .runningBalance(UPDATED_RUNNING_BALANCE);
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(updatedBinCardLine);

        restBinCardLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, binCardLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(binCardLineDTO))
            )
            .andExpect(status().isOk());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBinCardLineToMatchAllProperties(updatedBinCardLine);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<BinCardLine> binCardLineSearchList = Streamable.of(binCardLineSearchRepository.findAll()).toList();
                BinCardLine testBinCardLineSearch = binCardLineSearchList.get(searchDatabaseSizeAfter - 1);

                assertBinCardLineAllPropertiesEquals(testBinCardLineSearch, updatedBinCardLine);
            });
    }

    @Test
    @Transactional
    void putNonExistingBinCardLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        binCardLine.setId(longCount.incrementAndGet());

        // Create the BinCardLine
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBinCardLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, binCardLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(binCardLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchBinCardLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        binCardLine.setId(longCount.incrementAndGet());

        // Create the BinCardLine
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBinCardLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(binCardLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBinCardLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        binCardLine.setId(longCount.incrementAndGet());

        // Create the BinCardLine
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBinCardLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(binCardLineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateBinCardLineWithPatch() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the binCardLine using partial update
        BinCardLine partialUpdatedBinCardLine = new BinCardLine();
        partialUpdatedBinCardLine.setId(binCardLine.getId());

        partialUpdatedBinCardLine
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .quantityIn(UPDATED_QUANTITY_IN)
            .quantityOut(UPDATED_QUANTITY_OUT);

        restBinCardLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBinCardLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBinCardLine))
            )
            .andExpect(status().isOk());

        // Validate the BinCardLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBinCardLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBinCardLine, binCardLine),
            getPersistedBinCardLine(binCardLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateBinCardLineWithPatch() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the binCardLine using partial update
        BinCardLine partialUpdatedBinCardLine = new BinCardLine();
        partialUpdatedBinCardLine.setId(binCardLine.getId());

        partialUpdatedBinCardLine
            .inventoryItemCode(UPDATED_INVENTORY_ITEM_CODE)
            .date(UPDATED_DATE)
            .referenceNo(UPDATED_REFERENCE_NO)
            .description(UPDATED_DESCRIPTION)
            .quantityIn(UPDATED_QUANTITY_IN)
            .quantityOut(UPDATED_QUANTITY_OUT)
            .runningBalance(UPDATED_RUNNING_BALANCE);

        restBinCardLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBinCardLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBinCardLine))
            )
            .andExpect(status().isOk());

        // Validate the BinCardLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBinCardLineUpdatableFieldsEquals(partialUpdatedBinCardLine, getPersistedBinCardLine(partialUpdatedBinCardLine));
    }

    @Test
    @Transactional
    void patchNonExistingBinCardLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        binCardLine.setId(longCount.incrementAndGet());

        // Create the BinCardLine
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBinCardLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, binCardLineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(binCardLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBinCardLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        binCardLine.setId(longCount.incrementAndGet());

        // Create the BinCardLine
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBinCardLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(binCardLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBinCardLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        binCardLine.setId(longCount.incrementAndGet());

        // Create the BinCardLine
        BinCardLineDTO binCardLineDTO = binCardLineMapper.toDto(binCardLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBinCardLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(binCardLineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BinCardLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteBinCardLine() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);
        binCardLineRepository.save(binCardLine);
        binCardLineSearchRepository.save(binCardLine);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the binCardLine
        restBinCardLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, binCardLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(binCardLineSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchBinCardLine() throws Exception {
        // Initialize the database
        insertedBinCardLine = binCardLineRepository.saveAndFlush(binCardLine);
        binCardLineSearchRepository.save(binCardLine);

        // Search the binCardLine
        restBinCardLineMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + binCardLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(binCardLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].inventoryItemCode").value(hasItem(DEFAULT_INVENTORY_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].referenceNo").value(hasItem(DEFAULT_REFERENCE_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantityIn").value(hasItem(sameNumber(DEFAULT_QUANTITY_IN))))
            .andExpect(jsonPath("$.[*].quantityOut").value(hasItem(sameNumber(DEFAULT_QUANTITY_OUT))))
            .andExpect(jsonPath("$.[*].runningBalance").value(hasItem(sameNumber(DEFAULT_RUNNING_BALANCE))));
    }

    protected long getRepositoryCount() {
        return binCardLineRepository.count();
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

    protected BinCardLine getPersistedBinCardLine(BinCardLine binCardLine) {
        return binCardLineRepository.findById(binCardLine.getId()).orElseThrow();
    }

    protected void assertPersistedBinCardLineToMatchAllProperties(BinCardLine expectedBinCardLine) {
        assertBinCardLineAllPropertiesEquals(expectedBinCardLine, getPersistedBinCardLine(expectedBinCardLine));
    }

    protected void assertPersistedBinCardLineToMatchUpdatableProperties(BinCardLine expectedBinCardLine) {
        assertBinCardLineAllUpdatablePropertiesEquals(expectedBinCardLine, getPersistedBinCardLine(expectedBinCardLine));
    }
}
