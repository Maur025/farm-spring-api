package com.kernotec.farm.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportExcelDataDto {

    private String sequentialNumber;
    private String deviceNumber;
    private String farmName;
    private String personRegisterName;
    private String personRegisterLastName;
    private String phoneNumber;
    private Boolean isChipInDevice;
    private Boolean isHaveWhatsapp;
    private String emailUsername;
    private String emailPassword;
    private String facebookUsername;
    private String facebookPassword;
    private String tiktokUsername;
    private String tiktokPassword;
    private String xUsername;
    private String xPassword;
    private String macAddress;
    private String ipAddress;
    private String ipGateway;
    private String personFakeName;
    private String personFakeLastName;
    private String facebookObservation;
    private String tiktokObservation;
    private String operator;
    private String imeiOne;
    private String imeiTwo;
    private String deviceBrand;
    private String deviceModel;
    private String farmType;
    private String birthDateRegister;
    private String birthDateFake;
}
