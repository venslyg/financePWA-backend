package com.gvsolutions.web.rest;

import com.gvsolutions.repository.PettyCashLedgerRepository;
import com.gvsolutions.service.PettyCashLedgerQueryService;
import com.gvsolutions.service.PettyCashLedgerService;
import com.gvsolutions.service.criteria.PettyCashLedgerCriteria;
import com.gvsolutions.service.dto.PettyCashLedgerDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.PettyCashLedger}.
 */
@RestController
@RequestMapping("/api/petty-cash-ledgers")
public class PettyCashLedgerResource {

    private static final Logger LOG = LoggerFactory.getLogger(PettyCashLedgerResource.class);

    private static final String ENTITY_NAME = "pettyCashLedger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PettyCashLedgerService pettyCashLedgerService;

    private final PettyCashLedgerRepository pettyCashLedgerRepository;

    private final PettyCashLedgerQueryService pettyCashLedgerQueryService;

    public PettyCashLedgerResource(
        PettyCashLedgerService pettyCashLedgerService,
        PettyCashLedgerRepository pettyCashLedgerRepository,
        PettyCashLedgerQueryService pettyCashLedgerQueryService
    ) {
        this.pettyCashLedgerService = pettyCashLedgerService;
        this.pettyCashLedgerRepository = pettyCashLedgerRepository;
        this.pettyCashLedgerQueryService = pettyCashLedgerQueryService;
    }

    /**
     * {@code POST  /petty-cash-ledgers} : Create a new pettyCashLedger.
     *
     * @param pettyCashLedgerDTO the pettyCashLedgerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pettyCashLedgerDTO, or with status {@code 400 (Bad Request)} if the pettyCashLedger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PettyCashLedgerDTO> createPettyCashLedger(@RequestBody PettyCashLedgerDTO pettyCashLedgerDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PettyCashLedger : {}", pettyCashLedgerDTO);
        if (pettyCashLedgerDTO.getId() != null) {
            throw new BadRequestAlertException("A new pettyCashLedger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pettyCashLedgerDTO = pettyCashLedgerService.save(pettyCashLedgerDTO);
        return ResponseEntity.created(new URI("/api/petty-cash-ledgers/" + pettyCashLedgerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, pettyCashLedgerDTO.getId().toString()))
            .body(pettyCashLedgerDTO);
    }

    /**
     * {@code PUT  /petty-cash-ledgers/:id} : Updates an existing pettyCashLedger.
     *
     * @param id the id of the pettyCashLedgerDTO to save.
     * @param pettyCashLedgerDTO the pettyCashLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pettyCashLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the pettyCashLedgerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pettyCashLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PettyCashLedgerDTO> updatePettyCashLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PettyCashLedgerDTO pettyCashLedgerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PettyCashLedger : {}, {}", id, pettyCashLedgerDTO);
        if (pettyCashLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pettyCashLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pettyCashLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pettyCashLedgerDTO = pettyCashLedgerService.update(pettyCashLedgerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pettyCashLedgerDTO.getId().toString()))
            .body(pettyCashLedgerDTO);
    }

    /**
     * {@code PATCH  /petty-cash-ledgers/:id} : Partial updates given fields of an existing pettyCashLedger, field will ignore if it is null
     *
     * @param id the id of the pettyCashLedgerDTO to save.
     * @param pettyCashLedgerDTO the pettyCashLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pettyCashLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the pettyCashLedgerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pettyCashLedgerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pettyCashLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PettyCashLedgerDTO> partialUpdatePettyCashLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PettyCashLedgerDTO pettyCashLedgerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PettyCashLedger partially : {}, {}", id, pettyCashLedgerDTO);
        if (pettyCashLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pettyCashLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pettyCashLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PettyCashLedgerDTO> result = pettyCashLedgerService.partialUpdate(pettyCashLedgerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pettyCashLedgerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /petty-cash-ledgers} : get all the pettyCashLedgers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pettyCashLedgers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PettyCashLedgerDTO>> getAllPettyCashLedgers(
        PettyCashLedgerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PettyCashLedgers by criteria: {}", criteria);

        Page<PettyCashLedgerDTO> page = pettyCashLedgerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /petty-cash-ledgers/count} : count all the pettyCashLedgers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPettyCashLedgers(PettyCashLedgerCriteria criteria) {
        LOG.debug("REST request to count PettyCashLedgers by criteria: {}", criteria);
        return ResponseEntity.ok().body(pettyCashLedgerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /petty-cash-ledgers/:id} : get the "id" pettyCashLedger.
     *
     * @param id the id of the pettyCashLedgerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pettyCashLedgerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PettyCashLedgerDTO> getPettyCashLedger(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PettyCashLedger : {}", id);
        Optional<PettyCashLedgerDTO> pettyCashLedgerDTO = pettyCashLedgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pettyCashLedgerDTO);
    }

    /**
     * {@code DELETE  /petty-cash-ledgers/:id} : delete the "id" pettyCashLedger.
     *
     * @param id the id of the pettyCashLedgerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePettyCashLedger(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PettyCashLedger : {}", id);
        pettyCashLedgerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /petty-cash-ledgers/_search?query=:query} : search for the pettyCashLedger corresponding
     * to the query.
     *
     * @param query the query of the pettyCashLedger search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<PettyCashLedgerDTO>> searchPettyCashLedgers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of PettyCashLedgers for query {}", query);
        try {
            Page<PettyCashLedgerDTO> page = pettyCashLedgerService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
