package com.kernotec.farm.parametric.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "activity_types")
public class ActivityType extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToMany
    @JoinTable(name = "social_network_actions",
               joinColumns = @JoinColumn(name = "activity_type_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "social_network_id",
                                                referencedColumnName = "id"))
    @Where(clause = "deleted is false")
    private Set<SocialNetwork> socialNetworks;
}
