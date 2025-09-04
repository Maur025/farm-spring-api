package com.kernotec.farm.activity.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.parametric.jpa.entity.Region;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "groups")
public class Group extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "region_id", nullable = false)
    private UUID regionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Region region;

    @Where(clause = "deleted is false")
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<GroupMembership> groupMemberships;
}
