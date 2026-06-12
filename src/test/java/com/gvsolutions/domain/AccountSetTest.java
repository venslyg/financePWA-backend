package com.gvsolutions.domain;

import static com.gvsolutions.domain.AccountSetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountSet.class);
        AccountSet accountSet1 = getAccountSetSample1();
        AccountSet accountSet2 = new AccountSet();
        assertThat(accountSet1).isNotEqualTo(accountSet2);

        accountSet2.setId(accountSet1.getId());
        assertThat(accountSet1).isEqualTo(accountSet2);

        accountSet2 = getAccountSetSample2();
        assertThat(accountSet1).isNotEqualTo(accountSet2);
    }
}
