package com.kernotec.farm.parametric.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.dto.entity.PersonDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.rest.command.account.AccountSocialNetworkRegisterCmd;
import com.kernotec.farm.account.rest.command.person.PersonFindOrCreateCmd;
import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.dto.entity.FarmDto;
import com.kernotec.farm.inventory.rest.command.chip.ChipAssignRegisterCmd;
import com.kernotec.farm.inventory.rest.command.device.DeviceFindOrCreateCmd;
import com.kernotec.farm.inventory.rest.command.farm.FarmFindOrCreateCmd;
import com.kernotec.farm.parametric.jpa.enums.SocialNetworkEnum;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Deprecated(since = "v.of.2025-02-27", forRemoval = true)
public class ImportExcelRegisterInDbCmd extends
    AbstractTransactionalRequiredCommand<ImportExcelRegisterInDbCmd.Request, Void>
{

    private final PersonFindOrCreateCmd personFindOrCreateCmd;
    private final FarmFindOrCreateCmd farmFindOrCreateCmd;
    private final DeviceFindOrCreateCmd deviceFindOrCreateCmd;
    private final ChipAssignRegisterCmd chipAssignRegisterCmd;
    private final AccountSocialNetworkRegisterCmd accountSocialNetworkRegisterCmd;

    @Override
    protected Void run(Request request) {
        ImportExcelDataDto excelDataDto = request.getImportExcelDataDto();

        FarmDto farmDto = farmFindOrCreateCmd.withRequest(FarmFindOrCreateCmd.Request.builder()
                .code("FARM_" + excelDataDto.getFarmName())
                .name(excelDataDto.getFarmName())
                .type(excelDataDto.getFarmType())
                .build())
            .execute();

        DeviceDto deviceDto = deviceFindOrCreateCmd.withRequest(
                DeviceFindOrCreateCmd.Request.builder()
                    .deviceNumber(excelDataDto.getDeviceNumber())
                    .name("Dispositivo-" + excelDataDto.getDeviceNumber())
                    .model(excelDataDto.getDeviceModel())
                    .brand(excelDataDto.getDeviceBrand())
                    .farmId(farmDto.getId())
                    .macAddress(excelDataDto.getMacAddress())
                    .ipAddress(excelDataDto.getIpAddress())
                    .ipGateway(excelDataDto.getIpGateway())
                    .imeiOne(excelDataDto.getImeiOne())
                    .imeiTwo(excelDataDto.getImeiTwo())
                    .build())
            .execute();

        PersonDto personRegisterDto = personFindOrCreateCmd.withRequest(
                PersonFindOrCreateCmd.Request.builder()
                    .name(excelDataDto.getPersonRegisterName())
                    .lastName(excelDataDto.getPersonRegisterLastName())
                    .birthDateString(excelDataDto.getBirthDateRegister())
                    .build())
            .execute();

        ChipDto chipDto = chipAssignRegisterCmd.withRequest(ChipAssignRegisterCmd.Request.builder()
                .phoneNumber(excelDataDto.getPhoneNumber())
                .isDeviceInside(excelDataDto.getIsChipInDevice())
                .operatorCode(excelDataDto.getOperator())
                .personId(personRegisterDto.getId())
                .deviceId(deviceDto.getId())
                .build())
            .execute();

        PersonDto personFakeDto = personFindOrCreateCmd.withRequest(
                PersonFindOrCreateCmd.Request.builder()
                    .name(excelDataDto.getPersonFakeName())
                    .lastName(excelDataDto.getPersonFakeLastName())
                    .birthDateString(excelDataDto.getBirthDateFake())
                    .build())
            .execute();

        if (excelDataDto.getFacebookUsername() != null) {
            accountSocialNetworkRegisterCmd.withRequest(
                    AccountSocialNetworkRegisterCmd.Request.builder()
                        .username(excelDataDto.getFacebookUsername())
                        .password(excelDataDto.getFacebookPassword())
                        .chipId(chipDto.getId())
                        .personId(personFakeDto.getId())
                        .socialNetworkCode(SocialNetworkEnum.FACEBOOK)
                        .deviceId(deviceDto.getId())
                        .accountType(AccountTypeEnum.INTERNAL)
                        .observation(excelDataDto.getFacebookObservation())
                        .referenceEmail(excelDataDto.getEmailUsername())
                        .accountLink(excelDataDto.getFacebookAccountLink())
                        .build())
                .execute();
        }

        if (excelDataDto.getTiktokUsername() != null) {
            accountSocialNetworkRegisterCmd.withRequest(
                    AccountSocialNetworkRegisterCmd.Request.builder()
                        .username(excelDataDto.getTiktokUsername())
                        .password(excelDataDto.getTiktokPassword())
                        .chipId(chipDto.getId())
                        .personId(personFakeDto.getId())
                        .socialNetworkCode(SocialNetworkEnum.TIKTOK)
                        .deviceId(deviceDto.getId())
                        .accountType(AccountTypeEnum.INTERNAL)
                        .observation(excelDataDto.getTiktokObservation())
                        .referenceEmail(excelDataDto.getEmailUsername())
                        .build())
                .execute();
        }

        if (excelDataDto.getXUsername() != null) {
            accountSocialNetworkRegisterCmd.withRequest(
                    AccountSocialNetworkRegisterCmd.Request.builder()
                        .username(excelDataDto.getXUsername())
                        .password(excelDataDto.getXPassword())
                        .chipId(chipDto.getId())
                        .personId(personFakeDto.getId())
                        .socialNetworkCode(SocialNetworkEnum.X)
                        .deviceId(deviceDto.getId())
                        .accountType(AccountTypeEnum.INTERNAL)
                        .referenceEmail(excelDataDto.getEmailUsername())
                        .build())
                .execute();
        }

        if (excelDataDto.getIsHaveWhatsapp()) {
            accountSocialNetworkRegisterCmd.withRequest(
                    AccountSocialNetworkRegisterCmd.Request.builder()
                        .username(excelDataDto.getPhoneNumber())
                        .chipId(chipDto.getId())
                        .personId(personFakeDto.getId())
                        .socialNetworkCode(SocialNetworkEnum.WHATSAPP)
                        .deviceId(deviceDto.getId())
                        .accountType(AccountTypeEnum.INTERNAL)
                        .build())
                .execute();
        }

        if (excelDataDto.getEmailUsername() != null) {
            accountSocialNetworkRegisterCmd.withRequest(
                    AccountSocialNetworkRegisterCmd.Request.builder()
                        .username(excelDataDto.getEmailUsername())
                        .password(excelDataDto.getEmailPassword())
                        .chipId(chipDto.getId())
                        .personId(personFakeDto.getId())
                        .socialNetworkCode(SocialNetworkEnum.CORREO)
                        .deviceId(deviceDto.getId())
                        .accountType(AccountTypeEnum.INTERNAL)
                        .build())
                .execute();
        }

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ImportExcelDataDto importExcelDataDto;
    }
}
