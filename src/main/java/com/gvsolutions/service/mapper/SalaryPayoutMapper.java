package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.SalaryPayout;
import com.gvsolutions.service.dto.SalaryPayoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SalaryPayout} and its DTO {@link SalaryPayoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalaryPayoutMapper extends EntityMapper<SalaryPayoutDTO, SalaryPayout> {}
