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
@Table(name = "devices")
public class Device extends BaseAuditEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "model", length = 150)
    private String model;

    @Column(name = "brand", length = 150)
    private String brand;

    @Column(name = "serial_number", length = 150)
    private String serialNumber;

    @Column(name = "farm_id", nullable = false)
    private UUID farmId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Farm farm;
}
