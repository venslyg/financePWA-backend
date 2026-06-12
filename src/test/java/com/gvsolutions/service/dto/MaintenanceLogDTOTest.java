package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintenanceLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintenanceLogDTO.class);
        MaintenanceLogDTO maintenanceLogDTO1 = new MaintenanceLogDTO();
        maintenanceLogDTO1.setId(1L);
        MaintenanceLogDTO maintenanceLogDTO2 = new MaintenanceLogDTO();
        assertThat(maintenanceLogDTO1).isNotEqualTo(maintenanceLogDTO2);
        maintenanceLogDTO2.setId(maintenanceLogDTO1.getId());
        assertThat(maintenanceLogDTO1).isEqualTo(maintenanceLogDTO2);
        maintenanceLogDTO2.setId(2L);
        assertThat(maintenanceLogDTO1).isNotEqualTo(maintenanceLogDTO2);
        maintenanceLogDTO1.setId(null);
        assertThat(maintenanceLogDTO1).isNotEqualTo(maintenanceLogDTO2);
    }
}
