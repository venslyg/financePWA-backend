package com.gvsolutions.web.rest;

import com.gvsolutions.repository.ExpenseEntryRepository;
import com.gvsolutions.service.ExpenseEntryQueryService;
import com.gvsolutions.service.ExpenseEntryService;
import com.gvsolutions.service.criteria.ExpenseEntryCriteria;
import com.gvsolutions.service.dto.ExpenseEntryDTO;
import com.gvsolutions.web.rest.errors.BadRequestAlertException;
import com.gvsolutions.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gvsolutions.domain.ExpenseEntry}.
 */
@RestController
@RequestMapping("/api/expense-entries")
public class ExpenseEntryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseEntryResource.class);

    private static final String ENTITY_NAME = "expenseEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseEntryService expenseEntryService;

    private final ExpenseEntryRepository expenseEntryRepository;

    private final ExpenseEntryQueryService expenseEntryQueryService;

    public ExpenseEntryResource(
        ExpenseEntryService expenseEntryService,
        ExpenseEntryRepository expenseEntryRepository,
        ExpenseEntryQueryService expenseEntryQueryService
    ) {
        this.expenseEntryService = expenseEntryService;
        this.expenseEntryRepository = expenseEntryRepository;
        this.expenseEntryQueryService = expenseEntryQueryService;
    }

    /**
     * {@code POST  /expense-entries} : Create a new expenseEntry.
     *
     * @param expenseEntryDTO the expenseEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseEntryDTO, or with status {@code 400 (Bad Request)} if the expenseEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExpenseEntryDTO> createExpenseEntry(@RequestBody ExpenseEntryDTO expenseEntryDTO) throws URISyntaxException {
        LOG.debug("REST request to save ExpenseEntry : {}", expenseEntryDTO);
        if (expenseEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new expenseEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        expenseEntryDTO = expenseEntryService.save(expenseEntryDTO);
        return ResponseEntity.created(new URI("/api/expense-entries/" + expenseEntryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, expenseEntryDTO.getId().toString()))
            .body(expenseEntryDTO);
    }

    /**
     * {@code PUT  /expense-entries/:id} : Updates an existing expenseEntry.
     *
     * @param id the id of the expenseEntryDTO to save.
     * @param expenseEntryDTO the expenseEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseEntryDTO,
     * or with status {@code 400 (Bad Request)} if the expenseEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseEntryDTO> updateExpenseEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpenseEntryDTO expenseEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExpenseEntry : {}, {}", id, expenseEntryDTO);
        if (expenseEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        expenseEntryDTO = expenseEntryService.update(expenseEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseEntryDTO.getId().toString()))
            .body(expenseEntryDTO);
    }

    /**
     * {@code PATCH  /expense-entries/:id} : Partial updates given fields of an existing expenseEntry, field will ignore if it is null
     *
     * @param id the id of the expenseEntryDTO to save.
     * @param expenseEntryDTO the expenseEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseEntryDTO,
     * or with status {@code 400 (Bad Request)} if the expenseEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the expenseEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the expenseEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpenseEntryDTO> partialUpdateExpenseEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpenseEntryDTO expenseEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExpenseEntry partially : {}, {}", id, expenseEntryDTO);
        if (expenseEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpenseEntryDTO> result = expenseEntryService.partialUpdate(expenseEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /expense-entries} : get all the expenseEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExpenseEntryDTO>> getAllExpenseEntries(
        ExpenseEntryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExpenseEntries by criteria: {}", criteria);

        Page<ExpenseEntryDTO> page = expenseEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-entries/count} : count all the expenseEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExpenseEntries(ExpenseEntryCriteria criteria) {
        LOG.debug("REST request to count ExpenseEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(expenseEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expense-entries/:id} : get the "id" expenseEntry.
     *
     * @param id the id of the expenseEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseEntryDTO> getExpenseEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExpenseEntry : {}", id);
        Optional<ExpenseEntryDTO> expenseEntryDTO = expenseEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseEntryDTO);
    }

    /**
     * {@code DELETE  /expense-entries/:id} : delete the "id" expenseEntry.
     *
     * @param id the id of the expenseEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExpenseEntry : {}", id);
        expenseEntryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /expense-entries/_search?query=:query} : search for the expenseEntry corresponding
     * to the query.
     *
     * @param query the query of the expenseEntry search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ExpenseEntryDTO>> searchExpenseEntries(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ExpenseEntries for query {}", query);
        try {
            Page<ExpenseEntryDTO> page = expenseEntryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
