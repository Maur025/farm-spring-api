package com.kernotec.farm.jpa.entity;

import com.kernotec.core.jpa.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "activities")
public class Activity extends BaseAuditEntity {

  @Column(name = "link", nullable = false, length = 2000)
  private String link;

  @Column(name = "activity_date", nullable = false)
  private LocalDateTime activityDate;

  @Column(name = "activity_type_id", nullable = false)
  private UUID activityTypeId;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_type_id", referencedColumnName = "id", insertable = false, updatable = false)
  private ActivityType activityType;
}
