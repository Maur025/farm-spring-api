package com.kernotec.farm.account.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "accounts")
public class Account extends BaseAuditEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AccountTypeEnum type;

    @Column(name = "person_id", nullable = false)
    private UUID personId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Person person;

    @Column(name = "social_network_id", nullable = false)
    private UUID socialNetworkId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "social_network_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private SocialNetwork socialNetwork;

    @ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
    private Set<Device> devices;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "assigned_chips",
               joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "chip_id", referencedColumnName = "id"))
    private Set<Chip> chips;

    @ManyToMany
    @JoinTable(name = "account_observations",
               joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "observation_id",
                                                referencedColumnName = "id"))
    private Set<Observation> observations;
}
