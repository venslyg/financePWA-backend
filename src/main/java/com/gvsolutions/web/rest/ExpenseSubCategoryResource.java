package com.gvsolutions.web.rest;

import com.gvsolutions.repository.ExpenseSubCategoryRepository;
import com.gvsolutions.service.ExpenseSubCategoryQueryService;
import com.gvsolutions.service.ExpenseSubCategoryService;
import com.gvsolutions.service.criteria.ExpenseSubCategoryCriteria;
import com.gvsolutions.service.dto.ExpenseSubCategoryDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.ExpenseSubCategory}.
 */
@RestController
@RequestMapping("/api/expense-sub-categories")
public class ExpenseSubCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseSubCategoryResource.class);

    private static final String ENTITY_NAME = "expenseSubCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseSubCategoryService expenseSubCategoryService;

    private final ExpenseSubCategoryRepository expenseSubCategoryRepository;

    private final ExpenseSubCategoryQueryService expenseSubCategoryQueryService;

    public ExpenseSubCategoryResource(
        ExpenseSubCategoryService expenseSubCategoryService,
        ExpenseSubCategoryRepository expenseSubCategoryRepository,
        ExpenseSubCategoryQueryService expenseSubCategoryQueryService
    ) {
        this.expenseSubCategoryService = expenseSubCategoryService;
        this.expenseSubCategoryRepository = expenseSubCategoryRepository;
        this.expenseSubCategoryQueryService = expenseSubCategoryQueryService;
    }

    /**
     * {@code POST  /expense-sub-categories} : Create a new expenseSubCategory.
     *
     * @param expenseSubCategoryDTO the expenseSubCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseSubCategoryDTO, or with status {@code 400 (Bad Request)} if the expenseSubCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExpenseSubCategoryDTO> createExpenseSubCategory(@RequestBody ExpenseSubCategoryDTO expenseSubCategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExpenseSubCategory : {}", expenseSubCategoryDTO);
        if (expenseSubCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new expenseSubCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        expenseSubCategoryDTO = expenseSubCategoryService.save(expenseSubCategoryDTO);
        return ResponseEntity.created(new URI("/api/expense-sub-categories/" + expenseSubCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, expenseSubCategoryDTO.getId().toString()))
            .body(expenseSubCategoryDTO);
    }

    /**
     * {@code PUT  /expense-sub-categories/:id} : Updates an existing expenseSubCategory.
     *
     * @param id the id of the expenseSubCategoryDTO to save.
     * @param expenseSubCategoryDTO the expenseSubCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseSubCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the expenseSubCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseSubCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseSubCategoryDTO> updateExpenseSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpenseSubCategoryDTO expenseSubCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExpenseSubCategory : {}, {}", id, expenseSubCategoryDTO);
        if (expenseSubCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseSubCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseSubCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        expenseSubCategoryDTO = expenseSubCategoryService.update(expenseSubCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseSubCategoryDTO.getId().toString()))
            .body(expenseSubCategoryDTO);
    }

    /**
     * {@code PATCH  /expense-sub-categories/:id} : Partial updates given fields of an existing expenseSubCategory, field will ignore if it is null
     *
     * @param id the id of the expenseSubCategoryDTO to save.
     * @param expenseSubCategoryDTO the expenseSubCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseSubCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the expenseSubCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the expenseSubCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the expenseSubCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpenseSubCategoryDTO> partialUpdateExpenseSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpenseSubCategoryDTO expenseSubCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExpenseSubCategory partially : {}, {}", id, expenseSubCategoryDTO);
        if (expenseSubCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseSubCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseSubCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpenseSubCategoryDTO> result = expenseSubCategoryService.partialUpdate(expenseSubCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseSubCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /expense-sub-categories} : get all the expenseSubCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseSubCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExpenseSubCategoryDTO>> getAllExpenseSubCategories(
        ExpenseSubCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExpenseSubCategories by criteria: {}", criteria);

        Page<ExpenseSubCategoryDTO> page = expenseSubCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-sub-categories/count} : count all the expenseSubCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExpenseSubCategories(ExpenseSubCategoryCriteria criteria) {
        LOG.debug("REST request to count ExpenseSubCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(expenseSubCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expense-sub-categories/:id} : get the "id" expenseSubCategory.
     *
     * @param id the id of the expenseSubCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseSubCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseSubCategoryDTO> getExpenseSubCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExpenseSubCategory : {}", id);
        Optional<ExpenseSubCategoryDTO> expenseSubCategoryDTO = expenseSubCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseSubCategoryDTO);
    }

    /**
     * {@code DELETE  /expense-sub-categories/:id} : delete the "id" expenseSubCategory.
     *
     * @param id the id of the expenseSubCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseSubCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExpenseSubCategory : {}", id);
        expenseSubCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /expense-sub-categories/_search?query=:query} : search for the expenseSubCategory corresponding
     * to the query.
     *
     * @param query the query of the expenseSubCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ExpenseSubCategoryDTO>> searchExpenseSubCategories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ExpenseSubCategories for query {}", query);
        try {
            Page<ExpenseSubCategoryDTO> page = expenseSubCategoryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
