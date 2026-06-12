package com.gvsolutions.web.rest;

import com.gvsolutions.repository.BudgetPlanRepository;
import com.gvsolutions.service.BudgetPlanQueryService;
import com.gvsolutions.service.BudgetPlanService;
import com.gvsolutions.service.criteria.BudgetPlanCriteria;
import com.gvsolutions.service.dto.BudgetPlanDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.BudgetPlan}.
 */
@RestController
@RequestMapping("/api/budget-plans")
public class BudgetPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetPlanResource.class);

    private static final String ENTITY_NAME = "budgetPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BudgetPlanService budgetPlanService;

    private final BudgetPlanRepository budgetPlanRepository;

    private final BudgetPlanQueryService budgetPlanQueryService;

    public BudgetPlanResource(
        BudgetPlanService budgetPlanService,
        BudgetPlanRepository budgetPlanRepository,
        BudgetPlanQueryService budgetPlanQueryService
    ) {
        this.budgetPlanService = budgetPlanService;
        this.budgetPlanRepository = budgetPlanRepository;
        this.budgetPlanQueryService = budgetPlanQueryService;
    }

    /**
     * {@code POST  /budget-plans} : Create a new budgetPlan.
     *
     * @param budgetPlanDTO the budgetPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new budgetPlanDTO, or with status {@code 400 (Bad Request)} if the budgetPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BudgetPlanDTO> createBudgetPlan(@RequestBody BudgetPlanDTO budgetPlanDTO) throws URISyntaxException {
        LOG.debug("REST request to save BudgetPlan : {}", budgetPlanDTO);
        if (budgetPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new budgetPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        budgetPlanDTO = budgetPlanService.save(budgetPlanDTO);
        return ResponseEntity.created(new URI("/api/budget-plans/" + budgetPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, budgetPlanDTO.getId().toString()))
            .body(budgetPlanDTO);
    }

    /**
     * {@code PUT  /budget-plans/:id} : Updates an existing budgetPlan.
     *
     * @param id the id of the budgetPlanDTO to save.
     * @param budgetPlanDTO the budgetPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetPlanDTO,
     * or with status {@code 400 (Bad Request)} if the budgetPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the budgetPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BudgetPlanDTO> updateBudgetPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BudgetPlanDTO budgetPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BudgetPlan : {}, {}", id, budgetPlanDTO);
        if (budgetPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        budgetPlanDTO = budgetPlanService.update(budgetPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, budgetPlanDTO.getId().toString()))
            .body(budgetPlanDTO);
    }

    /**
     * {@code PATCH  /budget-plans/:id} : Partial updates given fields of an existing budgetPlan, field will ignore if it is null
     *
     * @param id the id of the budgetPlanDTO to save.
     * @param budgetPlanDTO the budgetPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetPlanDTO,
     * or with status {@code 400 (Bad Request)} if the budgetPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the budgetPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the budgetPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BudgetPlanDTO> partialUpdateBudgetPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BudgetPlanDTO budgetPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BudgetPlan partially : {}, {}", id, budgetPlanDTO);
        if (budgetPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BudgetPlanDTO> result = budgetPlanService.partialUpdate(budgetPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, budgetPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /budget-plans} : get all the budgetPlans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of budgetPlans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BudgetPlanDTO>> getAllBudgetPlans(
        BudgetPlanCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get BudgetPlans by criteria: {}", criteria);

        Page<BudgetPlanDTO> page = budgetPlanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /budget-plans/count} : count all the budgetPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBudgetPlans(BudgetPlanCriteria criteria) {
        LOG.debug("REST request to count BudgetPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(budgetPlanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /budget-plans/:id} : get the "id" budgetPlan.
     *
     * @param id the id of the budgetPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the budgetPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BudgetPlanDTO> getBudgetPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BudgetPlan : {}", id);
        Optional<BudgetPlanDTO> budgetPlanDTO = budgetPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetPlanDTO);
    }

    /**
     * {@code DELETE  /budget-plans/:id} : delete the "id" budgetPlan.
     *
     * @param id the id of the budgetPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BudgetPlan : {}", id);
        budgetPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /budget-plans/_search?query=:query} : search for the budgetPlan corresponding
     * to the query.
     *
     * @param query the query of the budgetPlan search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<BudgetPlanDTO>> searchBudgetPlans(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of BudgetPlans for query {}", query);
        try {
            Page<BudgetPlanDTO> page = budgetPlanService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
