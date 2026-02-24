package com.kernotec.farm.inventory.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.account.jpa.entity.Account;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "devices")
public class Device extends BaseAuditEntity {

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "device_number", nullable = false, length = 150)
    private String deviceNumber;

    @Column(name = "device_number_long", nullable = false)
    private Long deviceNumberLong;

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

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
    @Where(clause = "deleted is false")
    @OrderBy("phoneNumber ASC")
    private Set<Chip> chips;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "device_accounts",
               joinColumns = @JoinColumn(name = "device_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"))
    @Where(clause = "deleted is false AND is_enabled is true")
    private Set<Account> accounts;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Where(clause = "deleted is false")
    private Set<DeviceImei> deviceImeis;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Where(clause = "deleted is false")
    private Set<DeviceConnection> deviceConnections;
}
