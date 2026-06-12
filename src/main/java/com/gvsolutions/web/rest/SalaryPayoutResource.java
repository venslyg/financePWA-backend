package com.gvsolutions.web.rest;

import com.gvsolutions.repository.SalaryPayoutRepository;
import com.gvsolutions.service.SalaryPayoutQueryService;
import com.gvsolutions.service.SalaryPayoutService;
import com.gvsolutions.service.criteria.SalaryPayoutCriteria;
import com.gvsolutions.service.dto.SalaryPayoutDTO;
import com.gvsolutions.web.rest.errors.BadRequestAlertException;
import com.gvsolutions.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.gvsolutions.domain.SalaryPayout}.
 */
@RestController
@RequestMapping("/api/salary-payouts")
public class SalaryPayoutResource {

    private static final Logger LOG = LoggerFactory.getLogger(SalaryPayoutResource.class);

    private static final String ENTITY_NAME = "salaryPayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryPayoutService salaryPayoutService;

    private final SalaryPayoutRepository salaryPayoutRepository;

    private final SalaryPayoutQueryService salaryPayoutQueryService;

    public SalaryPayoutResource(
        SalaryPayoutService salaryPayoutService,
        SalaryPayoutRepository salaryPayoutRepository,
        SalaryPayoutQueryService salaryPayoutQueryService
    ) {
        this.salaryPayoutService = salaryPayoutService;
        this.salaryPayoutRepository = salaryPayoutRepository;
        this.salaryPayoutQueryService = salaryPayoutQueryService;
    }

    /**
     * {@code POST  /salary-payouts} : Create a new salaryPayout.
     *
     * @param salaryPayoutDTO the salaryPayoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaryPayoutDTO, or with status {@code 400 (Bad Request)} if the salaryPayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SalaryPayoutDTO> createSalaryPayout(@Valid @RequestBody SalaryPayoutDTO salaryPayoutDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SalaryPayout : {}", salaryPayoutDTO);
        if (salaryPayoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaryPayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        salaryPayoutDTO = salaryPayoutService.save(salaryPayoutDTO);
        return ResponseEntity.created(new URI("/api/salary-payouts/" + salaryPayoutDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, salaryPayoutDTO.getId().toString()))
            .body(salaryPayoutDTO);
    }

    /**
     * {@code PUT  /salary-payouts/:id} : Updates an existing salaryPayout.
     *
     * @param id the id of the salaryPayoutDTO to save.
     * @param salaryPayoutDTO the salaryPayoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryPayoutDTO,
     * or with status {@code 400 (Bad Request)} if the salaryPayoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaryPayoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalaryPayoutDTO> updateSalaryPayout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SalaryPayoutDTO salaryPayoutDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SalaryPayout : {}, {}", id, salaryPayoutDTO);
        if (salaryPayoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryPayoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaryPayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        salaryPayoutDTO = salaryPayoutService.update(salaryPayoutDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salaryPayoutDTO.getId().toString()))
            .body(salaryPayoutDTO);
    }

    /**
     * {@code PATCH  /salary-payouts/:id} : Partial updates given fields of an existing salaryPayout, field will ignore if it is null
     *
     * @param id the id of the salaryPayoutDTO to save.
     * @param salaryPayoutDTO the salaryPayoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryPayoutDTO,
     * or with status {@code 400 (Bad Request)} if the salaryPayoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salaryPayoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salaryPayoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalaryPayoutDTO> partialUpdateSalaryPayout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SalaryPayoutDTO salaryPayoutDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SalaryPayout partially : {}, {}", id, salaryPayoutDTO);
        if (salaryPayoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryPayoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaryPayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalaryPayoutDTO> result = salaryPayoutService.partialUpdate(salaryPayoutDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salaryPayoutDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /salary-payouts} : get all the salaryPayouts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaryPayouts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SalaryPayoutDTO>> getAllSalaryPayouts(
        SalaryPayoutCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SalaryPayouts by criteria: {}", criteria);

        Page<SalaryPayoutDTO> page = salaryPayoutQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salary-payouts/count} : count all the salaryPayouts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSalaryPayouts(SalaryPayoutCriteria criteria) {
        LOG.debug("REST request to count SalaryPayouts by criteria: {}", criteria);
        return ResponseEntity.ok().body(salaryPayoutQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /salary-payouts/:id} : get the "id" salaryPayout.
     *
     * @param id the id of the salaryPayoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaryPayoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalaryPayoutDTO> getSalaryPayout(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SalaryPayout : {}", id);
        Optional<SalaryPayoutDTO> salaryPayoutDTO = salaryPayoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salaryPayoutDTO);
    }

    /**
     * {@code DELETE  /salary-payouts/:id} : delete the "id" salaryPayout.
     *
     * @param id the id of the salaryPayoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalaryPayout(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SalaryPayout : {}", id);
        salaryPayoutService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /salary-payouts/_search?query=:query} : search for the salaryPayout corresponding
     * to the query.
     *
     * @param query the query of the salaryPayout search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SalaryPayoutDTO>> searchSalaryPayouts(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of SalaryPayouts for query {}", query);
        try {
            Page<SalaryPayoutDTO> page = salaryPayoutService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
