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
@Table(name = "publishings")
public class Publishing extends BaseAuditEntity {

    @Column(name = "publishing_type_id", nullable = false)
    private UUID publishingTypeId;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "publishing_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private PublishingType publishingType;

    @Column(name = "publishing_context_id", nullable = false)
    private UUID publishingContextId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "publishing_context_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private PublishingContext publishingContext;

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
