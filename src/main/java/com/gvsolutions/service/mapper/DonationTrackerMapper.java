package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.DonationTracker;
import com.gvsolutions.service.dto.DonationTrackerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DonationTracker} and its DTO {@link DonationTrackerDTO}.
 */
@Mapper(componentModel = "spring")
public interface DonationTrackerMapper extends EntityMapper<DonationTrackerDTO, DonationTracker> {}
