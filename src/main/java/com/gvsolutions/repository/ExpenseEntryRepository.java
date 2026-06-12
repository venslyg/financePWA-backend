package com.gvsolutions.repository;

import com.gvsolutions.domain.ExpenseEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExpenseEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseEntryRepository extends JpaRepository<ExpenseEntry, Long>, JpaSpecificationExecutor<ExpenseEntry> {}
