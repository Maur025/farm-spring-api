package com.kernotec.farm.jpa.entity;

import com.kernotec.core.jpa.entity.BaseEntity;
import com.kernotec.farm.jpa.enums.OperatorCodeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operators")
public class Operator extends BaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false)
    private OperatorCodeEnum code;
}
