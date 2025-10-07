package com.kernotec.farm.report.rest.command.pdf;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PdfExportCmd extends AbstractTransactionalRequiredCommand<PdfExportCmd.Request, Void> {

    @Override
    protected Void run(Request request) {
        return null;
    }

    @Builder
    public record Request() {

    }
}
