package com.gvsolutions.web.rest;

import com.gvsolutions.repository.LiabilityLogRepository;
import com.gvsolutions.service.LiabilityLogQueryService;
import com.gvsolutions.service.LiabilityLogService;
import com.gvsolutions.service.criteria.LiabilityLogCriteria;
import com.gvsolutions.service.dto.LiabilityLogDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.LiabilityLog}.
 */
@RestController
@RequestMapping("/api/liability-logs")
public class LiabilityLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(LiabilityLogResource.class);

    private static final String ENTITY_NAME = "liabilityLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LiabilityLogService liabilityLogService;

    private final LiabilityLogRepository liabilityLogRepository;

    private final LiabilityLogQueryService liabilityLogQueryService;

    public LiabilityLogResource(
        LiabilityLogService liabilityLogService,
        LiabilityLogRepository liabilityLogRepository,
        LiabilityLogQueryService liabilityLogQueryService
    ) {
        this.liabilityLogService = liabilityLogService;
        this.liabilityLogRepository = liabilityLogRepository;
        this.liabilityLogQueryService = liabilityLogQueryService;
    }

    /**
     * {@code POST  /liability-logs} : Create a new liabilityLog.
     *
     * @param liabilityLogDTO the liabilityLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new liabilityLogDTO, or with status {@code 400 (Bad Request)} if the liabilityLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LiabilityLogDTO> createLiabilityLog(@RequestBody LiabilityLogDTO liabilityLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save LiabilityLog : {}", liabilityLogDTO);
        if (liabilityLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new liabilityLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        liabilityLogDTO = liabilityLogService.save(liabilityLogDTO);
        return ResponseEntity.created(new URI("/api/liability-logs/" + liabilityLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, liabilityLogDTO.getId().toString()))
            .body(liabilityLogDTO);
    }

    /**
     * {@code PUT  /liability-logs/:id} : Updates an existing liabilityLog.
     *
     * @param id the id of the liabilityLogDTO to save.
     * @param liabilityLogDTO the liabilityLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liabilityLogDTO,
     * or with status {@code 400 (Bad Request)} if the liabilityLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the liabilityLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LiabilityLogDTO> updateLiabilityLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LiabilityLogDTO liabilityLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LiabilityLog : {}, {}", id, liabilityLogDTO);
        if (liabilityLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liabilityLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!liabilityLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        liabilityLogDTO = liabilityLogService.update(liabilityLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, liabilityLogDTO.getId().toString()))
            .body(liabilityLogDTO);
    }

    /**
     * {@code PATCH  /liability-logs/:id} : Partial updates given fields of an existing liabilityLog, field will ignore if it is null
     *
     * @param id the id of the liabilityLogDTO to save.
     * @param liabilityLogDTO the liabilityLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liabilityLogDTO,
     * or with status {@code 400 (Bad Request)} if the liabilityLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the liabilityLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the liabilityLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LiabilityLogDTO> partialUpdateLiabilityLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LiabilityLogDTO liabilityLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LiabilityLog partially : {}, {}", id, liabilityLogDTO);
        if (liabilityLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liabilityLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!liabilityLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LiabilityLogDTO> result = liabilityLogService.partialUpdate(liabilityLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, liabilityLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /liability-logs} : get all the liabilityLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of liabilityLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LiabilityLogDTO>> getAllLiabilityLogs(
        LiabilityLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get LiabilityLogs by criteria: {}", criteria);

        Page<LiabilityLogDTO> page = liabilityLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /liability-logs/count} : count all the liabilityLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLiabilityLogs(LiabilityLogCriteria criteria) {
        LOG.debug("REST request to count LiabilityLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(liabilityLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /liability-logs/:id} : get the "id" liabilityLog.
     *
     * @param id the id of the liabilityLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the liabilityLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LiabilityLogDTO> getLiabilityLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LiabilityLog : {}", id);
        Optional<LiabilityLogDTO> liabilityLogDTO = liabilityLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(liabilityLogDTO);
    }

    /**
     * {@code DELETE  /liability-logs/:id} : delete the "id" liabilityLog.
     *
     * @param id the id of the liabilityLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLiabilityLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LiabilityLog : {}", id);
        liabilityLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /liability-logs/_search?query=:query} : search for the liabilityLog corresponding
     * to the query.
     *
     * @param query the query of the liabilityLog search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<LiabilityLogDTO>> searchLiabilityLogs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of LiabilityLogs for query {}", query);
        try {
            Page<LiabilityLogDTO> page = liabilityLogService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
