package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountSetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountSetDTO.class);
        AccountSetDTO accountSetDTO1 = new AccountSetDTO();
        accountSetDTO1.setId(1L);
        AccountSetDTO accountSetDTO2 = new AccountSetDTO();
        assertThat(accountSetDTO1).isNotEqualTo(accountSetDTO2);
        accountSetDTO2.setId(accountSetDTO1.getId());
        assertThat(accountSetDTO1).isEqualTo(accountSetDTO2);
        accountSetDTO2.setId(2L);
        assertThat(accountSetDTO1).isNotEqualTo(accountSetDTO2);
        accountSetDTO1.setId(null);
        assertThat(accountSetDTO1).isNotEqualTo(accountSetDTO2);
    }
}
