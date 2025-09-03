package com.kernotec.farm.inventory.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.parametric.jpa.entity.Operator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chips")
public class Chip extends BaseAuditEntity {

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "is_device_inside", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeviceInside = false;

    @Column(name = "operator_id", nullable = false)
    private UUID operatorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operator_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Operator operator;

    @Column(name = "registration_person_id", nullable = false)
    private UUID registrationPersonId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_person_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private RegistrationPerson registrationPerson;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Device device;

    @ManyToMany(mappedBy = "chips", fetch = FetchType.LAZY)
    private Set<Account> accounts;
}
