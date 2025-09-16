package com.kernotec.farm.audit.user.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = Id.NAME, property = "auditUserDataType")
@JsonSubTypes({@JsonSubTypes.Type(name = "auditUserData.AuthUserData", value = AuthUserData.class)})
@NoArgsConstructor
public class AuditUserData implements Serializable {

}
