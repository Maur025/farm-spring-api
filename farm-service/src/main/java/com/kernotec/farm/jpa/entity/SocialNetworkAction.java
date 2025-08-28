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
@Table(name = "social_network_actions")
public class SocialNetworkAction extends BaseAuditEntity {

    @Column(name = "social_network_id", nullable = false)
    private UUID socialNetworkId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "social_network_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private SocialNetwork socialNetwork;

    @Column(name = "activity_type_id", nullable = false)
    private UUID activityTypeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ActivityType activityType;
}
