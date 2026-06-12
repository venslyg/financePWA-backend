package com.gvsolutions.web.rest;

import com.gvsolutions.repository.BinCardLineRepository;
import com.gvsolutions.service.BinCardLineQueryService;
import com.gvsolutions.service.BinCardLineService;
import com.gvsolutions.service.criteria.BinCardLineCriteria;
import com.gvsolutions.service.dto.BinCardLineDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.BinCardLine}.
 */
@RestController
@RequestMapping("/api/bin-card-lines")
public class BinCardLineResource {

    private static final Logger LOG = LoggerFactory.getLogger(BinCardLineResource.class);

    private static final String ENTITY_NAME = "binCardLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BinCardLineService binCardLineService;

    private final BinCardLineRepository binCardLineRepository;

    private final BinCardLineQueryService binCardLineQueryService;

    public BinCardLineResource(
        BinCardLineService binCardLineService,
        BinCardLineRepository binCardLineRepository,
        BinCardLineQueryService binCardLineQueryService
    ) {
        this.binCardLineService = binCardLineService;
        this.binCardLineRepository = binCardLineRepository;
        this.binCardLineQueryService = binCardLineQueryService;
    }

    /**
     * {@code POST  /bin-card-lines} : Create a new binCardLine.
     *
     * @param binCardLineDTO the binCardLineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new binCardLineDTO, or with status {@code 400 (Bad Request)} if the binCardLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BinCardLineDTO> createBinCardLine(@RequestBody BinCardLineDTO binCardLineDTO) throws URISyntaxException {
        LOG.debug("REST request to save BinCardLine : {}", binCardLineDTO);
        if (binCardLineDTO.getId() != null) {
            throw new BadRequestAlertException("A new binCardLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        binCardLineDTO = binCardLineService.save(binCardLineDTO);
        return ResponseEntity.created(new URI("/api/bin-card-lines/" + binCardLineDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, binCardLineDTO.getId().toString()))
            .body(binCardLineDTO);
    }

    /**
     * {@code PUT  /bin-card-lines/:id} : Updates an existing binCardLine.
     *
     * @param id the id of the binCardLineDTO to save.
     * @param binCardLineDTO the binCardLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated binCardLineDTO,
     * or with status {@code 400 (Bad Request)} if the binCardLineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the binCardLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BinCardLineDTO> updateBinCardLine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BinCardLineDTO binCardLineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BinCardLine : {}, {}", id, binCardLineDTO);
        if (binCardLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, binCardLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!binCardLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        binCardLineDTO = binCardLineService.update(binCardLineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, binCardLineDTO.getId().toString()))
            .body(binCardLineDTO);
    }

    /**
     * {@code PATCH  /bin-card-lines/:id} : Partial updates given fields of an existing binCardLine, field will ignore if it is null
     *
     * @param id the id of the binCardLineDTO to save.
     * @param binCardLineDTO the binCardLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated binCardLineDTO,
     * or with status {@code 400 (Bad Request)} if the binCardLineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the binCardLineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the binCardLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BinCardLineDTO> partialUpdateBinCardLine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BinCardLineDTO binCardLineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BinCardLine partially : {}, {}", id, binCardLineDTO);
        if (binCardLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, binCardLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!binCardLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BinCardLineDTO> result = binCardLineService.partialUpdate(binCardLineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, binCardLineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bin-card-lines} : get all the binCardLines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of binCardLines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BinCardLineDTO>> getAllBinCardLines(
        BinCardLineCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get BinCardLines by criteria: {}", criteria);

        Page<BinCardLineDTO> page = binCardLineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bin-card-lines/count} : count all the binCardLines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBinCardLines(BinCardLineCriteria criteria) {
        LOG.debug("REST request to count BinCardLines by criteria: {}", criteria);
        return ResponseEntity.ok().body(binCardLineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bin-card-lines/:id} : get the "id" binCardLine.
     *
     * @param id the id of the binCardLineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the binCardLineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BinCardLineDTO> getBinCardLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BinCardLine : {}", id);
        Optional<BinCardLineDTO> binCardLineDTO = binCardLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(binCardLineDTO);
    }

    /**
     * {@code DELETE  /bin-card-lines/:id} : delete the "id" binCardLine.
     *
     * @param id the id of the binCardLineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBinCardLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BinCardLine : {}", id);
        binCardLineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /bin-card-lines/_search?query=:query} : search for the binCardLine corresponding
     * to the query.
     *
     * @param query the query of the binCardLine search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<BinCardLineDTO>> searchBinCardLines(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of BinCardLines for query {}", query);
        try {
            Page<BinCardLineDTO> page = binCardLineService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
