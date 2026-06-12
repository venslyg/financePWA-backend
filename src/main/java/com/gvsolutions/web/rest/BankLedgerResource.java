package com.gvsolutions.web.rest;

import com.gvsolutions.repository.BankLedgerRepository;
import com.gvsolutions.service.BankLedgerQueryService;
import com.gvsolutions.service.BankLedgerService;
import com.gvsolutions.service.criteria.BankLedgerCriteria;
import com.gvsolutions.service.dto.BankLedgerDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.BankLedger}.
 */
@RestController
@RequestMapping("/api/bank-ledgers")
public class BankLedgerResource {

    private static final Logger LOG = LoggerFactory.getLogger(BankLedgerResource.class);

    private static final String ENTITY_NAME = "bankLedger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankLedgerService bankLedgerService;

    private final BankLedgerRepository bankLedgerRepository;

    private final BankLedgerQueryService bankLedgerQueryService;

    public BankLedgerResource(
        BankLedgerService bankLedgerService,
        BankLedgerRepository bankLedgerRepository,
        BankLedgerQueryService bankLedgerQueryService
    ) {
        this.bankLedgerService = bankLedgerService;
        this.bankLedgerRepository = bankLedgerRepository;
        this.bankLedgerQueryService = bankLedgerQueryService;
    }

    /**
     * {@code POST  /bank-ledgers} : Create a new bankLedger.
     *
     * @param bankLedgerDTO the bankLedgerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankLedgerDTO, or with status {@code 400 (Bad Request)} if the bankLedger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BankLedgerDTO> createBankLedger(@RequestBody BankLedgerDTO bankLedgerDTO) throws URISyntaxException {
        LOG.debug("REST request to save BankLedger : {}", bankLedgerDTO);
        if (bankLedgerDTO.getId() != null) {
            throw new BadRequestAlertException("A new bankLedger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bankLedgerDTO = bankLedgerService.save(bankLedgerDTO);
        return ResponseEntity.created(new URI("/api/bank-ledgers/" + bankLedgerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bankLedgerDTO.getId().toString()))
            .body(bankLedgerDTO);
    }

    /**
     * {@code PUT  /bank-ledgers/:id} : Updates an existing bankLedger.
     *
     * @param id the id of the bankLedgerDTO to save.
     * @param bankLedgerDTO the bankLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the bankLedgerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BankLedgerDTO> updateBankLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BankLedgerDTO bankLedgerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BankLedger : {}, {}", id, bankLedgerDTO);
        if (bankLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bankLedgerDTO = bankLedgerService.update(bankLedgerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bankLedgerDTO.getId().toString()))
            .body(bankLedgerDTO);
    }

    /**
     * {@code PATCH  /bank-ledgers/:id} : Partial updates given fields of an existing bankLedger, field will ignore if it is null
     *
     * @param id the id of the bankLedgerDTO to save.
     * @param bankLedgerDTO the bankLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the bankLedgerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bankLedgerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BankLedgerDTO> partialUpdateBankLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BankLedgerDTO bankLedgerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BankLedger partially : {}, {}", id, bankLedgerDTO);
        if (bankLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankLedgerDTO> result = bankLedgerService.partialUpdate(bankLedgerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bankLedgerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bank-ledgers} : get all the bankLedgers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankLedgers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BankLedgerDTO>> getAllBankLedgers(
        BankLedgerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get BankLedgers by criteria: {}", criteria);

        Page<BankLedgerDTO> page = bankLedgerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-ledgers/count} : count all the bankLedgers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBankLedgers(BankLedgerCriteria criteria) {
        LOG.debug("REST request to count BankLedgers by criteria: {}", criteria);
        return ResponseEntity.ok().body(bankLedgerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bank-ledgers/:id} : get the "id" bankLedger.
     *
     * @param id the id of the bankLedgerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankLedgerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankLedgerDTO> getBankLedger(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BankLedger : {}", id);
        Optional<BankLedgerDTO> bankLedgerDTO = bankLedgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankLedgerDTO);
    }

    /**
     * {@code DELETE  /bank-ledgers/:id} : delete the "id" bankLedger.
     *
     * @param id the id of the bankLedgerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankLedger(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BankLedger : {}", id);
        bankLedgerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /bank-ledgers/_search?query=:query} : search for the bankLedger corresponding
     * to the query.
     *
     * @param query the query of the bankLedger search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<BankLedgerDTO>> searchBankLedgers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of BankLedgers for query {}", query);
        try {
            Page<BankLedgerDTO> page = bankLedgerService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
