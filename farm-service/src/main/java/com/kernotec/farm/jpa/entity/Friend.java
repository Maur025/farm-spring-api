package com.kernotec.farm.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import com.kernotec.farm.jpa.enums.FriendStatusEnum;
import com.kernotec.farm.jpa.enums.FriendTypeEnum;
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
@Table(name = "friends")
public class Friend extends BaseAuditEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private FriendStatusEnum status;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private FriendTypeEnum type;

  @Column(name = "activity_id", nullable = false)
  private UUID activityId;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Activity activity;

  @Column(name = "activity_type_id", nullable = false)
  private UUID activityTypeId;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_type_id", referencedColumnName = "id", insertable = false, updatable = false)
  private ActivityType activityType;
}
