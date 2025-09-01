package com.kernotec.farm.rest.mapper.device;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kernotec.core.test.unit.test.util.AbstractMapperTest;
import com.kernotec.farm.jpa.entity.Account;
import com.kernotec.farm.jpa.entity.Chip;
import com.kernotec.farm.jpa.entity.Device;
import com.kernotec.farm.jpa.entity.Farm;
import com.kernotec.farm.rest.dto.response.device.DeviceResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

class DeviceResponseMapperTest extends AbstractMapperTest<Device, DeviceResponse> {

    @InjectMocks
    private DeviceResponseMapperImpl deviceResponseMapper;

    private Device device;

    @BeforeEach
    public void openMocks() {
        super.openMocks();

        device = new Device();
        device.setId(UUID.randomUUID());

        device.setName("device 1");
        device.setDeviceNumber("D-0001");
        device.setModel("Model X");
        device.setBrand("Brand Y");
        device.setSerialNumber("SN123456");

        device.setFarmId(UUID.randomUUID());
        device.setFarm(new Farm());

        device.setChips(Set.of(new Chip()));

        device.setAccounts(Set.of(new Account()));

        setInputObject(device);
    }

    @Override
    protected DeviceResponse executeMapper(Device device) {
        return deviceResponseMapper.toResponse(device);
    }

    @Override
    protected List<DeviceResponse> executeMapper(List<Device> deviceList) {
        return deviceResponseMapper.toResponse(deviceList);
    }

    @Override
    protected Set<DeviceResponse> executeMapper(Set<Device> deviceSet) {
        return deviceResponseMapper.toResponse(deviceSet);
    }

    @Override
    protected void assertsForResult(DeviceResponse deviceResponse) {
        assertAll(
            "DeviceResponse assertions", () -> assertNotNull(deviceResponse.getId()),
            () -> assertEquals(device.getId(), deviceResponse.getId()),

            () -> assertEquals(device.getName(), deviceResponse.getName()),
            () -> assertEquals(device.getDeviceNumber(), deviceResponse.getDeviceNumber()),
            () -> assertEquals(device.getModel(), deviceResponse.getModel()),
            () -> assertEquals(device.getBrand(), deviceResponse.getBrand()),
            () -> assertEquals(device.getSerialNumber(), deviceResponse.getSerialNumber()),

            () -> assertEquals(device.getFarmId(), deviceResponse.getFarmId()),
            () -> assertNotNull(deviceResponse.getFarm()),

            () -> assertNotNull(deviceResponse.getChips()), () -> assertEquals(
                device.getChips()
                    .size(), deviceResponse.getChips()
                    .size()
            )
        );
    }
}