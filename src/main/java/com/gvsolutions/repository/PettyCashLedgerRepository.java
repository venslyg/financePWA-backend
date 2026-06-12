package com.gvsolutions.repository;

import com.gvsolutions.domain.PettyCashLedger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PettyCashLedger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PettyCashLedgerRepository extends JpaRepository<PettyCashLedger, Long>, JpaSpecificationExecutor<PettyCashLedger> {}
