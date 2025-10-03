package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import com.kernotec.farm.report.rest.dto.response.farm.FarmReportTotalResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportDashboardService {

    private final FarmService farmService;

    public List<FarmReportTotalResponse> findReportTotalFarms() {
        List<Farm> farmList = farmService.findAll();
        return  List.of();
    }
}
