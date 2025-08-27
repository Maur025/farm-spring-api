package com.kernotec.farm.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "device_connections")
public class DeviceConnection extends BaseAuditEntity {

    @Column(name = "mac_address", unique = true, nullable = false, length = 150)
    private String macAddress;

    @Column(name = "ip_address", nullable = false, length = 100)
    private String ipAddress;

    @Column(name = "ip_gateway", nullable = false, length = 100)
    private String ipGateway;

    @Column(name = "network_name", nullable = false)
    private String networkName;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Device device;
}
