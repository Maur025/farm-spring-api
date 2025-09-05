package com.kernotec.farm.parametric.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.account.jpa.entity.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "social_networks")
public class SocialNetwork extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "icon", length = 150)
    private String icon;

    @Column(name = "color", length = 50)
    private String color;

    @OneToMany(mappedBy = "socialNetwork", fetch = FetchType.LAZY)
    private Set<Account> accounts;

    @Where(clause = "deleted is false")
    @ManyToMany(mappedBy = "socialNetworks", fetch = FetchType.LAZY)
    private Set<ActivityType> activityTypes;
}
