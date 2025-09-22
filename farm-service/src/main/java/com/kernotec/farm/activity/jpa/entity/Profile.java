package com.kernotec.farm.activity.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.parametric.jpa.entity.PublishingContext;
import com.kernotec.farm.parametric.jpa.entity.Region;
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
@Table(name = "profiles")
public class Profile extends BaseAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "region_id")
    private UUID regionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Region region;

    @Column(name = "publishing_context_id", nullable = false)
    private UUID publishingContextId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publishing_context_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private PublishingContext publishingContext;
}
