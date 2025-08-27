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
@Table(name = "chips")
public class Chip extends BaseAuditEntity {

  @Column(name = "number_phone", nullable = false)
  private String numberPhone;

  @Column(name = "operator_id", nullable = false)
  private UUID operatorId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "operator_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Operator operator;

  @Column(name = "registration_person_id", nullable = false)
  private UUID registrationPersonId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "registration_person_id", referencedColumnName = "id", insertable = false, updatable = false)
  private RegistrationPerson registrationPerson;

  @Column(name = "device_id", nullable = false)
  private UUID deviceId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "device_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Device device;
}
