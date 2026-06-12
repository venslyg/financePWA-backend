package com.gvsolutions.web.rest;

import com.gvsolutions.repository.AssetRegisterRepository;
import com.gvsolutions.service.AssetRegisterQueryService;
import com.gvsolutions.service.AssetRegisterService;
import com.gvsolutions.service.criteria.AssetRegisterCriteria;
import com.gvsolutions.service.dto.AssetRegisterDTO;
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
 * REST controller for managing {@link com.gvsolutions.domain.AssetRegister}.
 */
@RestController
@RequestMapping("/api/asset-registers")
public class AssetRegisterResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssetRegisterResource.class);

    private static final String ENTITY_NAME = "assetRegister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetRegisterService assetRegisterService;

    private final AssetRegisterRepository assetRegisterRepository;

    private final AssetRegisterQueryService assetRegisterQueryService;

    public AssetRegisterResource(
        AssetRegisterService assetRegisterService,
        AssetRegisterRepository assetRegisterRepository,
        AssetRegisterQueryService assetRegisterQueryService
    ) {
        this.assetRegisterService = assetRegisterService;
        this.assetRegisterRepository = assetRegisterRepository;
        this.assetRegisterQueryService = assetRegisterQueryService;
    }

    /**
     * {@code POST  /asset-registers} : Create a new assetRegister.
     *
     * @param assetRegisterDTO the assetRegisterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetRegisterDTO, or with status {@code 400 (Bad Request)} if the assetRegister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssetRegisterDTO> createAssetRegister(@RequestBody AssetRegisterDTO assetRegisterDTO) throws URISyntaxException {
        LOG.debug("REST request to save AssetRegister : {}", assetRegisterDTO);
        if (assetRegisterDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetRegister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assetRegisterDTO = assetRegisterService.save(assetRegisterDTO);
        return ResponseEntity.created(new URI("/api/asset-registers/" + assetRegisterDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, assetRegisterDTO.getId().toString()))
            .body(assetRegisterDTO);
    }

    /**
     * {@code PUT  /asset-registers/:id} : Updates an existing assetRegister.
     *
     * @param id the id of the assetRegisterDTO to save.
     * @param assetRegisterDTO the assetRegisterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetRegisterDTO,
     * or with status {@code 400 (Bad Request)} if the assetRegisterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetRegisterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetRegisterDTO> updateAssetRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetRegisterDTO assetRegisterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AssetRegister : {}, {}", id, assetRegisterDTO);
        if (assetRegisterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetRegisterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        assetRegisterDTO = assetRegisterService.update(assetRegisterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetRegisterDTO.getId().toString()))
            .body(assetRegisterDTO);
    }

    /**
     * {@code PATCH  /asset-registers/:id} : Partial updates given fields of an existing assetRegister, field will ignore if it is null
     *
     * @param id the id of the assetRegisterDTO to save.
     * @param assetRegisterDTO the assetRegisterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetRegisterDTO,
     * or with status {@code 400 (Bad Request)} if the assetRegisterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetRegisterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetRegisterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetRegisterDTO> partialUpdateAssetRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssetRegisterDTO assetRegisterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AssetRegister partially : {}, {}", id, assetRegisterDTO);
        if (assetRegisterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetRegisterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetRegisterDTO> result = assetRegisterService.partialUpdate(assetRegisterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetRegisterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-registers} : get all the assetRegisters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetRegisters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssetRegisterDTO>> getAllAssetRegisters(
        AssetRegisterCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AssetRegisters by criteria: {}", criteria);

        Page<AssetRegisterDTO> page = assetRegisterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-registers/count} : count all the assetRegisters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAssetRegisters(AssetRegisterCriteria criteria) {
        LOG.debug("REST request to count AssetRegisters by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetRegisterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-registers/:id} : get the "id" assetRegister.
     *
     * @param id the id of the assetRegisterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetRegisterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetRegisterDTO> getAssetRegister(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AssetRegister : {}", id);
        Optional<AssetRegisterDTO> assetRegisterDTO = assetRegisterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetRegisterDTO);
    }

    /**
     * {@code DELETE  /asset-registers/:id} : delete the "id" assetRegister.
     *
     * @param id the id of the assetRegisterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssetRegister(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AssetRegister : {}", id);
        assetRegisterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /asset-registers/_search?query=:query} : search for the assetRegister corresponding
     * to the query.
     *
     * @param query the query of the assetRegister search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AssetRegisterDTO>> searchAssetRegisters(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of AssetRegisters for query {}", query);
        try {
            Page<AssetRegisterDTO> page = assetRegisterService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
