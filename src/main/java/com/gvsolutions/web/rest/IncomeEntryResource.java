package com.gvsolutions.web.rest;

import com.gvsolutions.repository.IncomeEntryRepository;
import com.gvsolutions.service.IncomeEntryQueryService;
import com.gvsolutions.service.IncomeEntryService;
import com.gvsolutions.service.criteria.IncomeEntryCriteria;
import com.gvsolutions.service.dto.IncomeEntryDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.IncomeEntry}.
 */
@RestController
@RequestMapping("/api/income-entries")
public class IncomeEntryResource {

    private static final Logger LOG = LoggerFactory.getLogger(IncomeEntryResource.class);

    private static final String ENTITY_NAME = "incomeEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncomeEntryService incomeEntryService;

    private final IncomeEntryRepository incomeEntryRepository;

    private final IncomeEntryQueryService incomeEntryQueryService;

    public IncomeEntryResource(
        IncomeEntryService incomeEntryService,
        IncomeEntryRepository incomeEntryRepository,
        IncomeEntryQueryService incomeEntryQueryService
    ) {
        this.incomeEntryService = incomeEntryService;
        this.incomeEntryRepository = incomeEntryRepository;
        this.incomeEntryQueryService = incomeEntryQueryService;
    }

    /**
     * {@code POST  /income-entries} : Create a new incomeEntry.
     *
     * @param incomeEntryDTO the incomeEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incomeEntryDTO, or with status {@code 400 (Bad Request)} if the incomeEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IncomeEntryDTO> createIncomeEntry(@RequestBody IncomeEntryDTO incomeEntryDTO) throws URISyntaxException {
        LOG.debug("REST request to save IncomeEntry : {}", incomeEntryDTO);
        if (incomeEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new incomeEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        incomeEntryDTO = incomeEntryService.save(incomeEntryDTO);
        return ResponseEntity.created(new URI("/api/income-entries/" + incomeEntryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, incomeEntryDTO.getId().toString()))
            .body(incomeEntryDTO);
    }

    /**
     * {@code PUT  /income-entries/:id} : Updates an existing incomeEntry.
     *
     * @param id the id of the incomeEntryDTO to save.
     * @param incomeEntryDTO the incomeEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomeEntryDTO,
     * or with status {@code 400 (Bad Request)} if the incomeEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incomeEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncomeEntryDTO> updateIncomeEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IncomeEntryDTO incomeEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IncomeEntry : {}, {}", id, incomeEntryDTO);
        if (incomeEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incomeEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incomeEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        incomeEntryDTO = incomeEntryService.update(incomeEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, incomeEntryDTO.getId().toString()))
            .body(incomeEntryDTO);
    }

    /**
     * {@code PATCH  /income-entries/:id} : Partial updates given fields of an existing incomeEntry, field will ignore if it is null
     *
     * @param id the id of the incomeEntryDTO to save.
     * @param incomeEntryDTO the incomeEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomeEntryDTO,
     * or with status {@code 400 (Bad Request)} if the incomeEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the incomeEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the incomeEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncomeEntryDTO> partialUpdateIncomeEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IncomeEntryDTO incomeEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IncomeEntry partially : {}, {}", id, incomeEntryDTO);
        if (incomeEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incomeEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incomeEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncomeEntryDTO> result = incomeEntryService.partialUpdate(incomeEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, incomeEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /income-entries} : get all the incomeEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incomeEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IncomeEntryDTO>> getAllIncomeEntries(
        IncomeEntryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IncomeEntries by criteria: {}", criteria);

        Page<IncomeEntryDTO> page = incomeEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /income-entries/count} : count all the incomeEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIncomeEntries(IncomeEntryCriteria criteria) {
        LOG.debug("REST request to count IncomeEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(incomeEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /income-entries/:id} : get the "id" incomeEntry.
     *
     * @param id the id of the incomeEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incomeEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncomeEntryDTO> getIncomeEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IncomeEntry : {}", id);
        Optional<IncomeEntryDTO> incomeEntryDTO = incomeEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incomeEntryDTO);
    }

    /**
     * {@code DELETE  /income-entries/:id} : delete the "id" incomeEntry.
     *
     * @param id the id of the incomeEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomeEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IncomeEntry : {}", id);
        incomeEntryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /income-entries/_search?query=:query} : search for the incomeEntry corresponding
     * to the query.
     *
     * @param query the query of the incomeEntry search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<IncomeEntryDTO>> searchIncomeEntries(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of IncomeEntries for query {}", query);
        try {
            Page<IncomeEntryDTO> page = incomeEntryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
