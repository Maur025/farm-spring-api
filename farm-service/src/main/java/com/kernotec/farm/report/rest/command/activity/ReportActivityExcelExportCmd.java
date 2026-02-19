package com.kernotec.farm.report.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.activity.jpa.dto.entity.ActivityDto;
import com.kernotec.farm.activity.jpa.dto.entity.CommentDto;
import com.kernotec.farm.activity.jpa.dto.entity.ConnectionDto;
import com.kernotec.farm.activity.jpa.dto.entity.FollowDto;
import com.kernotec.farm.activity.jpa.dto.entity.GroupDto;
import com.kernotec.farm.activity.jpa.dto.entity.GroupMembershipDto;
import com.kernotec.farm.activity.jpa.dto.entity.PublishingDto;
import com.kernotec.farm.activity.jpa.dto.entity.ReactionDto;
import com.kernotec.farm.activity.jpa.dto.mapper.ActivityDtoMapper;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.dto.entity.FarmDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ReactionTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.RegionDto;
import com.kernotec.farm.parametric.jpa.dto.entity.SocialNetworkDto;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.command.excel.ReportHeadCreateCmd;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.util.ExcelUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportActivityExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ReportActivityExcelExportCmd.Request, Void>
{

    private final Map<ReportActivityStyleEnum, CellStyle> cellStyles = new HashMap<>();

    private final ReportActivityService reportActivityService;
    private final ActivityDtoMapper activityDtoMapper;
    private final ExcelUtil excelUtil;
    private final ReportHeadCreateCmd reportHeadCreateCmd;

    @Override
    protected Void run(Request request) {
        Sheet sheet = request.workbook.createSheet("Reporte de Actividades");
        ReportActivityRequest filterRequest = request.filterRequest();

        cellStyles.put(
            ReportActivityStyleEnum.HEADER, excelUtil.getCellStyle(request.workbook, true, true));
        cellStyles.put(
            ReportActivityStyleEnum.CONTENT,
            excelUtil.getCellStyle(request.workbook, false, false)
        );

        excelUtil.setColumnsSize(sheet, List.of(5, 10, 14, 25, 14, 14, 36, 25, 20));

        int headerRowNum = reportHeadCreateCmd.withRequest(ReportHeadCreateCmd.Request.builder()
                .sheet(sheet)
                .reportTitle(request.titleReport())
                .authUsername(request.authUsername())
                .zoneId(filterRequest.getZoneId())
                .searchCriteria(filterRequest.getSearchCriteria())
                .searchDateDetail(reportActivityService.getReportDateDetail(filterRequest))
                .cellStyles(cellStyles)
                .build())
            .execute();

        Row headerRow = sheet.createRow(headerRowNum++);
        excelUtil.fillExcelRow(
            headerRow, List.of(
                "#", "Granja", "Dispositivo", "Cuenta", "Actividad", "Red Social", "Detalle",
                "Complemento", "Fecha"
            ), cellStyles.get(ReportActivityStyleEnum.HEADER)
        );

        int page = 0;
        int size = 500;

        int rowCount = 0;
        Page<Activity> activityPage;

        do {
            Pageable pageable = PageableUtil.of(page, size, request.sortBy(), request.descending());

            activityPage = reportActivityService.findAllWithFilters(
                request.filterRequest(), pageable);

            List<ActivityDto> activityDtoList = activityDtoMapper.toDto(activityPage.getContent());

            for (ActivityDto activityDto : activityDtoList) {
                Row row = sheet.createRow(rowCount + headerRowNum);
                rowCount++;

                String[] detailAndComplement = getDetailAndComplementFromActivity(activityDto);

                String activityDateStr = excelUtil.getDateFormat(
                    activityDto.getActivityDate(), request.filterRequest()
                        .getZoneId(), "dd-MM-yyyy HH:mm"
                );

                List<String> rowValues = List.of(
                    String.valueOf(rowCount), getFarmName(activityDto),
                    getDeviceNumber(activityDto), getAccountUsername(activityDto),
                    getActivityTypeName(activityDto), getSocialNetworkName(activityDto),
                    detailAndComplement[0], detailAndComplement[1], activityDateStr
                );

                excelUtil.fillExcelRow(
                    row, rowValues, cellStyles.get(ReportActivityStyleEnum.CONTENT));
            }

            page++;
        } while (page < activityPage.getTotalPages());

        return null;
    }

    private String getFarmName(ActivityDto activityDto) {
        AccountDto accountDto = activityDto.getAccount();

        DeviceDto deviceDto = accountDto.getDevices()
            .stream()
            .findFirst()
            .orElse(new DeviceDto());

        FarmDto farmDto = deviceDto.getFarm() == null ? new FarmDto() : deviceDto.getFarm();

        return farmDto.getName() != null ? farmDto.getName() : "N/A";
    }

    private String getDeviceNumber(ActivityDto activityDto) {
        AccountDto accountDto = activityDto.getAccount();

        DeviceDto deviceDto = accountDto.getDevices()
            .stream()
            .findFirst()
            .orElse(new DeviceDto());

        return deviceDto.getDeviceNumber() != null ? deviceDto.getDeviceNumber() : "N/A";
    }

    private String getAccountUsername(ActivityDto activityDto) {
        AccountDto accountDto = activityDto.getAccount();
        return accountDto.getUsername() != null ? accountDto.getUsername() : "N/A";
    }

    private String getActivityTypeName(ActivityDto activityDto) {
        ActivityTypeDto activityTypeDto =
            activityDto.getActivityType() == null ? new ActivityTypeDto()
                : activityDto.getActivityType();

        return activityTypeDto.getName() != null ? activityTypeDto.getName() : "N/A";
    }

    private String getSocialNetworkName(ActivityDto activityDto) {
        AccountDto accountDto = activityDto.getAccount();
        SocialNetworkDto socialNetworkDto =
            accountDto.getSocialNetwork() == null ? new SocialNetworkDto()
                : accountDto.getSocialNetwork();

        return socialNetworkDto.getName() != null ? socialNetworkDto.getName() : "N/A";
    }

    private String[] getDetailAndComplementFromActivity(ActivityDto activityDto) {
        ActivityTypeDto activityType = activityDto.getActivityType();
        String link = activityDto.getLink() == null ? "N/A" : activityDto.getLink();

        return switch (ActivityTypeCodeEnum.fromValue(activityType.getCode())) {
            case COMENTARIO -> getDetailAndComplementOfComment(activityDto, link);
            case AMISTAD -> getDetailAndComplementOfConnection(activityDto);
            case FOLLOW, FOLLOW_TIKTOK -> getDetailAndComplementOfFollow(activityDto, link);
            case GRUPO -> getDetailAndComplementOfGroup(activityDto);
            case REACCION -> getDetailAndComplementOfReaction(activityDto, link);
            case PUBLICACION -> getDetailAndComplementOfPublishing(activityDto, link);
        };
    }

    private String[] getDetailAndComplementOfComment(ActivityDto activityDto, String link) {
        if (activityDto.getComments()
            .isEmpty())
        {
            return new String[]{"N/A", "N/A"};
        }

        CommentDto commentDto = activityDto.getComments()
            .stream()
            .findFirst()
            .orElse(new CommentDto());

        return new String[]{commentDto.getComment(), link};
    }

    private String[] getDetailAndComplementOfConnection(ActivityDto activityDto) {
        if (activityDto.getConnections()
            .isEmpty())
        {
            return new String[]{"N/A", "N/A"};
        }

        ConnectionDto connectionDto = activityDto.getConnections()
            .stream()
            .findFirst()
            .orElse(new ConnectionDto());

        AccountDto accountDto = connectionDto.getPotentialFriendAccount() == null ? new AccountDto()
            : connectionDto.getPotentialFriendAccount();

        return new String[]{getConnectionAction(connectionDto.getAction()),
            accountDto.getUsername()};
    }

    private String getConnectionAction(ConnectionActionEnum action) {
        return switch (action) {
            case OUTGOING_FRIEND_REQUEST -> "Enviado";
            case BREAK_CONNECTION -> "Eliminado";
            case REJECT_CONNECTION -> "Rechazado";
            case APPROVE_CONNECTION -> "Confirmado";
            case INCOMING_FRIEND_REQUEST_AND_CONFIRMED -> "Recibido y confirmado";
            case INCOMING_FRIEND_REQUEST -> "Recibido";
        };
    }

    private String[] getDetailAndComplementOfFollow(ActivityDto activityDto, String link) {
        if (activityDto.getFollows()
            .isEmpty())
        {
            return new String[]{"N/A", "N/A"};
        }

        FollowDto followDto = activityDto.getFollows()
            .stream()
            .findFirst()
            .orElse(new FollowDto());

        return new String[]{followDto.getName(), link};
    }

    private String[] getDetailAndComplementOfGroup(ActivityDto activityDto) {
        if (activityDto.getGroupMemberships()
            .isEmpty())
        {
            return new String[]{"N/A", "N/A"};
        }

        GroupMembershipDto groupMembershipDto = activityDto.getGroupMemberships()
            .stream()
            .findFirst()
            .orElse(new GroupMembershipDto());

        GroupDto groupDto =
            groupMembershipDto.getGroup() == null ? new GroupDto() : groupMembershipDto.getGroup();

        RegionDto regionDto = groupDto.getRegion() == null ? new RegionDto() : groupDto.getRegion();

        return new String[]{groupDto.getName(), regionDto.getName()};
    }

    private String[] getDetailAndComplementOfReaction(ActivityDto activityDto, String link) {
        if (activityDto.getReactions()
            .isEmpty())
        {
            return new String[]{"N/A", "N/A"};
        }

        ReactionDto reactionDto = activityDto.getReactions()
            .stream()
            .findFirst()
            .orElse(new ReactionDto());

        ReactionTypeDto reactionTypeDto =
            reactionDto.getReactionType() == null ? new ReactionTypeDto()
                : reactionDto.getReactionType();

        return new String[]{reactionTypeDto.getName(), link};
    }

    private String[] getDetailAndComplementOfPublishing(ActivityDto activityDto, String link) {
        if (activityDto.getPublishings()
            .isEmpty())
        {
            return new String[]{"N/A", "N/A"};
        }

        PublishingDto publishingDto = activityDto.getPublishings()
            .stream()
            .findFirst()
            .orElse(new PublishingDto());

        return new String[]{publishingDto.getDescription(), link};
    }

    @Builder
    public record Request(SXSSFWorkbook workbook, ReportActivityRequest filterRequest,
                          String sortBy, boolean descending, String titleReport,
                          String authUsername)
    {

    }
}
