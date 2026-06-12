package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.ChurchStaff;
import com.gvsolutions.service.dto.ChurchStaffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChurchStaff} and its DTO {@link ChurchStaffDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChurchStaffMapper extends EntityMapper<ChurchStaffDTO, ChurchStaff> {}
