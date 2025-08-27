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
@Table(name = "groups")
public class Group extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_join", nullable = false, columnDefinition = "boolean default false")
    private boolean isJoin;

    @Column(name = "region_id", nullable = false)
    private UUID regionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Region region;

    @Column(name = "activity_id", nullable = false)
    private UUID activityId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Activity activity;

    @Column(name = "activity_type_id", nullable = false)
    private UUID activityTypeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ActivityType activityType;
}
