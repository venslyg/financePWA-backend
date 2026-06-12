package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.BudgetPlan;
import com.gvsolutions.service.dto.BudgetPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BudgetPlan} and its DTO {@link BudgetPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetPlanMapper extends EntityMapper<BudgetPlanDTO, BudgetPlan> {}
