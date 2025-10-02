package com.kernotec.farm.report.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
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
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.dto.entity.FarmDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ReactionTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.RegionDto;
import com.kernotec.farm.parametric.jpa.dto.entity.SocialNetworkDto;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportActivityExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ReportActivityExcelExportCmd.Request, Void>
{

    private final ReportActivityService reportActivityService;
    private final ActivityDtoMapper activityDtoMapper;
    private final ExcelUtil excelUtil;

    @Override
    protected Void run(Request request) {
        try (var workbook = new SXSSFWorkbook(100); OutputStream out = request.response()
            .getOutputStream())
        {
            Sheet sheet = workbook.createSheet("Report-Activity");

            sheet.setColumnWidth(0, 5 * 256);
            sheet.setColumnWidth(1, 10 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 35 * 256);
            sheet.setColumnWidth(4, 15 * 256);
            sheet.setColumnWidth(5, 20 * 256);
            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 15 * 256);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Activity Report");

            List<Activity> activityList = reportActivityService.findAllWithFiltersNoPaginated(
                request.filterRequest());

            List<ActivityDto> activityDtoList = activityDtoMapper.toDto(activityList);

            Row headerRow = sheet.createRow(1);
            excelUtil.fillExcelRow(
                headerRow, true, List.of(
                    "#", "Granja", "Dispositivo", "Cuenta", "Actividad", "Red Social", "Detalle",
                    "Complemento", "Fecha"
                )
            );

            int headerRowNum = 2;
            int rowCount = 0;

            for (ActivityDto activityDto : activityDtoList) {
                Row row = sheet.createRow(rowCount + headerRowNum);
                rowCount++;

                String[] detailAndComplement = getDetailAndComplementFromActivity(activityDto);

                excelUtil.fillExcelRow(
                    row, false, List.of(
                        String.valueOf(rowCount), getFarmName(activityDto),
                        getDeviceNumber(activityDto), getAccountUsername(activityDto),
                        getActivityTypeName(activityDto), getSocialNetworkName(activityDto),
                        detailAndComplement[0], detailAndComplement[1],
                        getActivityDateFormat(activityDto)
                    )
                );
            }

            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    private String getActivityDateFormat(ActivityDto activityDto) {
        var formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        return activityDto.getActivityDate()
            .format(formater);
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

        return new String[]{connectionDto.getAction().toString(), accountDto.getUsername()};
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
    public record Request(HttpServletResponse response, ReportActivityRequest filterRequest) {

    }
}
