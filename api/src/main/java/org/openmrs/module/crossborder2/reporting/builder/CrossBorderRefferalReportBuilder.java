package org.openmrs.module.crossborder2.reporting.builder;

import org.openmrs.module.crossborder2.reporting.dataset.CrossBorderReferralDataset;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
@Builds({ "cross.border.referral" })
public class CrossBorderRefferalReportBuilder extends AbstractReportBuilder {
    private final CrossBorderReferralDataset crossRefeeralDataset;

    @Autowired
    public CrossBorderRefferalReportBuilder(CrossBorderReferralDataset crossRefeeralDataset) {
        this.crossRefeeralDataset = crossRefeeralDataset;
    }

    @Override
    protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
        return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
                Date.class), new Parameter("dateBasedReporting", "", String.class));
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor, ReportDefinition reportDefinition) {

        return Arrays.asList(ReportUtils.map(crossRefeeralDataset.getCrossBorderDataset(), "startDate=${startDate},endDate=${endDate}"));
    }
}
