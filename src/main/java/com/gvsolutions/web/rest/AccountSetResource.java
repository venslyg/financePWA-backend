package com.gvsolutions.web.rest;

import com.gvsolutions.repository.AccountSetRepository;
import com.gvsolutions.service.AccountSetQueryService;
import com.gvsolutions.service.AccountSetService;
import com.gvsolutions.service.criteria.AccountSetCriteria;
import com.gvsolutions.service.dto.AccountSetDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.AccountSet}.
 */
@RestController
@RequestMapping("/api/account-sets")
public class AccountSetResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountSetResource.class);

    private static final String ENTITY_NAME = "accountSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountSetService accountSetService;

    private final AccountSetRepository accountSetRepository;

    private final AccountSetQueryService accountSetQueryService;

    public AccountSetResource(
        AccountSetService accountSetService,
        AccountSetRepository accountSetRepository,
        AccountSetQueryService accountSetQueryService
    ) {
        this.accountSetService = accountSetService;
        this.accountSetRepository = accountSetRepository;
        this.accountSetQueryService = accountSetQueryService;
    }

    /**
     * {@code POST  /account-sets} : Create a new accountSet.
     *
     * @param accountSetDTO the accountSetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountSetDTO, or with status {@code 400 (Bad Request)} if the accountSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountSetDTO> createAccountSet(@RequestBody AccountSetDTO accountSetDTO) throws URISyntaxException {
        LOG.debug("REST request to save AccountSet : {}", accountSetDTO);
        if (accountSetDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accountSetDTO = accountSetService.save(accountSetDTO);
        return ResponseEntity.created(new URI("/api/account-sets/" + accountSetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, accountSetDTO.getId().toString()))
            .body(accountSetDTO);
    }

    /**
     * {@code PUT  /account-sets/:id} : Updates an existing accountSet.
     *
     * @param id the id of the accountSetDTO to save.
     * @param accountSetDTO the accountSetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountSetDTO,
     * or with status {@code 400 (Bad Request)} if the accountSetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountSetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountSetDTO> updateAccountSet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountSetDTO accountSetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AccountSet : {}, {}", id, accountSetDTO);
        if (accountSetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountSetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accountSetDTO = accountSetService.update(accountSetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountSetDTO.getId().toString()))
            .body(accountSetDTO);
    }

    /**
     * {@code PATCH  /account-sets/:id} : Partial updates given fields of an existing accountSet, field will ignore if it is null
     *
     * @param id the id of the accountSetDTO to save.
     * @param accountSetDTO the accountSetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountSetDTO,
     * or with status {@code 400 (Bad Request)} if the accountSetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountSetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountSetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountSetDTO> partialUpdateAccountSet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountSetDTO accountSetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AccountSet partially : {}, {}", id, accountSetDTO);
        if (accountSetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountSetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountSetDTO> result = accountSetService.partialUpdate(accountSetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountSetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-sets} : get all the accountSets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountSets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccountSetDTO>> getAllAccountSets(
        AccountSetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AccountSets by criteria: {}", criteria);

        Page<AccountSetDTO> page = accountSetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-sets/count} : count all the accountSets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAccountSets(AccountSetCriteria criteria) {
        LOG.debug("REST request to count AccountSets by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountSetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-sets/:id} : get the "id" accountSet.
     *
     * @param id the id of the accountSetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountSetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountSetDTO> getAccountSet(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AccountSet : {}", id);
        Optional<AccountSetDTO> accountSetDTO = accountSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountSetDTO);
    }

    /**
     * {@code DELETE  /account-sets/:id} : delete the "id" accountSet.
     *
     * @param id the id of the accountSetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountSet(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AccountSet : {}", id);
        accountSetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /account-sets/_search?query=:query} : search for the accountSet corresponding
     * to the query.
     *
     * @param query the query of the accountSet search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccountSetDTO>> searchAccountSets(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AccountSets for query {}", query);
        try {
            Page<AccountSetDTO> page = accountSetService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
