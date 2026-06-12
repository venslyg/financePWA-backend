package com.gvsolutions.web.rest;

import com.gvsolutions.repository.MaintenanceLogRepository;
import com.gvsolutions.service.MaintenanceLogQueryService;
import com.gvsolutions.service.MaintenanceLogService;
import com.gvsolutions.service.criteria.MaintenanceLogCriteria;
import com.gvsolutions.service.dto.MaintenanceLogDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.MaintenanceLog}.
 */
@RestController
@RequestMapping("/api/maintenance-logs")
public class MaintenanceLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceLogResource.class);

    private static final String ENTITY_NAME = "maintenanceLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaintenanceLogService maintenanceLogService;

    private final MaintenanceLogRepository maintenanceLogRepository;

    private final MaintenanceLogQueryService maintenanceLogQueryService;

    public MaintenanceLogResource(
        MaintenanceLogService maintenanceLogService,
        MaintenanceLogRepository maintenanceLogRepository,
        MaintenanceLogQueryService maintenanceLogQueryService
    ) {
        this.maintenanceLogService = maintenanceLogService;
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.maintenanceLogQueryService = maintenanceLogQueryService;
    }

    /**
     * {@code POST  /maintenance-logs} : Create a new maintenanceLog.
     *
     * @param maintenanceLogDTO the maintenanceLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maintenanceLogDTO, or with status {@code 400 (Bad Request)} if the maintenanceLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaintenanceLogDTO> createMaintenanceLog(@RequestBody MaintenanceLogDTO maintenanceLogDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MaintenanceLog : {}", maintenanceLogDTO);
        if (maintenanceLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new maintenanceLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        maintenanceLogDTO = maintenanceLogService.save(maintenanceLogDTO);
        return ResponseEntity.created(new URI("/api/maintenance-logs/" + maintenanceLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, maintenanceLogDTO.getId().toString()))
            .body(maintenanceLogDTO);
    }

    /**
     * {@code PUT  /maintenance-logs/:id} : Updates an existing maintenanceLog.
     *
     * @param id the id of the maintenanceLogDTO to save.
     * @param maintenanceLogDTO the maintenanceLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintenanceLogDTO,
     * or with status {@code 400 (Bad Request)} if the maintenanceLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maintenanceLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceLogDTO> updateMaintenanceLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaintenanceLogDTO maintenanceLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MaintenanceLog : {}, {}", id, maintenanceLogDTO);
        if (maintenanceLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintenanceLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintenanceLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        maintenanceLogDTO = maintenanceLogService.update(maintenanceLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, maintenanceLogDTO.getId().toString()))
            .body(maintenanceLogDTO);
    }

    /**
     * {@code PATCH  /maintenance-logs/:id} : Partial updates given fields of an existing maintenanceLog, field will ignore if it is null
     *
     * @param id the id of the maintenanceLogDTO to save.
     * @param maintenanceLogDTO the maintenanceLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintenanceLogDTO,
     * or with status {@code 400 (Bad Request)} if the maintenanceLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maintenanceLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maintenanceLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaintenanceLogDTO> partialUpdateMaintenanceLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaintenanceLogDTO maintenanceLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MaintenanceLog partially : {}, {}", id, maintenanceLogDTO);
        if (maintenanceLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintenanceLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintenanceLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaintenanceLogDTO> result = maintenanceLogService.partialUpdate(maintenanceLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, maintenanceLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maintenance-logs} : get all the maintenanceLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maintenanceLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaintenanceLogDTO>> getAllMaintenanceLogs(
        MaintenanceLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MaintenanceLogs by criteria: {}", criteria);

        Page<MaintenanceLogDTO> page = maintenanceLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maintenance-logs/count} : count all the maintenanceLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMaintenanceLogs(MaintenanceLogCriteria criteria) {
        LOG.debug("REST request to count MaintenanceLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(maintenanceLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /maintenance-logs/:id} : get the "id" maintenanceLog.
     *
     * @param id the id of the maintenanceLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maintenanceLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLogDTO> getMaintenanceLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MaintenanceLog : {}", id);
        Optional<MaintenanceLogDTO> maintenanceLogDTO = maintenanceLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maintenanceLogDTO);
    }

    /**
     * {@code DELETE  /maintenance-logs/:id} : delete the "id" maintenanceLog.
     *
     * @param id the id of the maintenanceLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MaintenanceLog : {}", id);
        maintenanceLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /maintenance-logs/_search?query=:query} : search for the maintenanceLog corresponding
     * to the query.
     *
     * @param query the query of the maintenanceLog search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MaintenanceLogDTO>> searchMaintenanceLogs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MaintenanceLogs for query {}", query);
        try {
            Page<MaintenanceLogDTO> page = maintenanceLogService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
