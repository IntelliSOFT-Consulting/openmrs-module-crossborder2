package org.openmrs.module.crossborder2.reporting.builder;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openmrs.module.crossborder2.reporting.dataset.CrossBorderScreeningDataset;
import org.openmrs.module.crossborder2.reporting.queries.CrossborderQueries;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Builds({ "cross.border.referral" })
public class CrossBorderReferralListReportBuilder extends AbstractHybridReportBuilder {
	
	private final CrossBorderScreeningDataset crossBorderScreeningDataset;
	
	@Autowired
	public CrossBorderReferralListReportBuilder(CrossBorderScreeningDataset crossBorderScreeningDataset) {
		this.crossBorderScreeningDataset = crossBorderScreeningDataset;
	}
	
	@Override
	protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor hybridReportDescriptor,
	        PatientDataSetDefinition patientDataSetDefinition) {
		return null;
	}
	
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		SqlDataSetDefinition dsd = new SqlDataSetDefinition();
		dsd.setName("ReferralsList");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.setSqlQuery(CrossborderQueries.getCrossborderReferralPatients());
		
		return Collections.singletonList(ReportUtils.map((DataSetDefinition) dsd,
		    "startDate=${startDate},endDate=${endDate}"));
	}
}
