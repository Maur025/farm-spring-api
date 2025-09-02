package com.kernotec.farm.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.jpa.enums.GroupActionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private GroupActionEnum action;

    @Column(name = "request_state_id", nullable = false)
    private UUID requestStateId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_state_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private RequestState requestState;

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

    @Column(name = "publishing_context_id", nullable = false)
    private UUID publishingContextId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publishing_context_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private PublishingContext publishingContext;
}
