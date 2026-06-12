package com.gvsolutions.repository;

import com.gvsolutions.domain.BudgetPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BudgetPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long>, JpaSpecificationExecutor<BudgetPlan> {}
