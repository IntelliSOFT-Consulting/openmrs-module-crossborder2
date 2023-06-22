package org.openmrs.module.crossborder2.reporting.builder;

import org.openmrs.module.crossborder2.reporting.dataset.CrossBorderScreeningDataset;
import org.openmrs.module.crossborder2.reporting.queries.CrossborderQueries;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Builds({ "cross.border.screening" })
public class CrossBorderScreeningListReportBuilder extends AbstractReportBuilder {
	
	private final CrossBorderScreeningDataset crossBorderScreeningDataset;
	
	@Autowired
	public CrossBorderScreeningListReportBuilder(CrossBorderScreeningDataset crossBorderScreeningDataset) {
		this.crossBorderScreeningDataset = crossBorderScreeningDataset;
	}
	
	@Override
	protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date",
		        Date.class), new Parameter("dateBasedReporting", "", String.class));
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor,
	        ReportDefinition reportDefinition) {
		
		SqlDataSetDefinition dsd = new SqlDataSetDefinition();
		dsd.setName("ScreeningList");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.setSqlQuery(CrossborderQueries.getCrossborderScreeningPatients());
		
		return Collections.singletonList(ReportUtils.map((DataSetDefinition) dsd,
		    "startDate=${startDate},endDate=${endDate}"));
	}
}
