package com.gvsolutions.repository;

import com.gvsolutions.domain.IncomeEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncomeEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomeEntryRepository extends JpaRepository<IncomeEntry, Long>, JpaSpecificationExecutor<IncomeEntry> {}
