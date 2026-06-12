package com.gvsolutions.domain;

import static com.gvsolutions.domain.AssetRegisterTestSamples.*;
import static com.gvsolutions.domain.MaintenanceLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintenanceLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintenanceLog.class);
        MaintenanceLog maintenanceLog1 = getMaintenanceLogSample1();
        MaintenanceLog maintenanceLog2 = new MaintenanceLog();
        assertThat(maintenanceLog1).isNotEqualTo(maintenanceLog2);

        maintenanceLog2.setId(maintenanceLog1.getId());
        assertThat(maintenanceLog1).isEqualTo(maintenanceLog2);

        maintenanceLog2 = getMaintenanceLogSample2();
        assertThat(maintenanceLog1).isNotEqualTo(maintenanceLog2);
    }

    @Test
    void assetTest() {
        MaintenanceLog maintenanceLog = getMaintenanceLogRandomSampleGenerator();
        AssetRegister assetRegisterBack = getAssetRegisterRandomSampleGenerator();

        maintenanceLog.setAsset(assetRegisterBack);
        assertThat(maintenanceLog.getAsset()).isEqualTo(assetRegisterBack);

        maintenanceLog.asset(null);
        assertThat(maintenanceLog.getAsset()).isNull();
    }
}
