package com.kernotec.farm.activity.jpa.entity;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.audit.user.DataBaseAuditEntity;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "activities")
public class Activity extends DataBaseAuditEntity {

    @Column(name = "link", nullable = false, length = 2000)
    private String link;

    @Column(name = "activity_date", nullable = false)
    private ZonedDateTime activityDate;

    @Column(name = "is_system_activity", columnDefinition = "boolean default false")
    private boolean isSystemActivity = false;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private Account account;

    @Column(name = "activity_type_id", nullable = false)
    private UUID activityTypeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id", referencedColumnName = "id", insertable = false,
                updatable = false)
    private ActivityType activityType;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<Publishing> publishings;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<Reaction> reactions;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<GroupMembership> groupMemberships;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<Connection> connections;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<Follow> follows;
}
