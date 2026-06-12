package com.gvsolutions.repository;

import com.gvsolutions.domain.BankLedger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BankLedger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankLedgerRepository extends JpaRepository<BankLedger, Long>, JpaSpecificationExecutor<BankLedger> {}
