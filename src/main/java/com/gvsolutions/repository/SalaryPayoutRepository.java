package com.gvsolutions.repository;

import com.gvsolutions.domain.SalaryPayout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SalaryPayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryPayoutRepository extends JpaRepository<SalaryPayout, Long>, JpaSpecificationExecutor<SalaryPayout> {}
