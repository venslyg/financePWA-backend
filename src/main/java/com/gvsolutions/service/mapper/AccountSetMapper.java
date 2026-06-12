package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.AccountSet;
import com.gvsolutions.service.dto.AccountSetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountSet} and its DTO {@link AccountSetDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccountSetMapper extends EntityMapper<AccountSetDTO, AccountSet> {}
