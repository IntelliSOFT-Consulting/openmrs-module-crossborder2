package org.openmrs.module.crossborder2.reporting.dataset;

import org.openmrs.module.crossborder2.reporting.indicator.CrossBorderIndicators;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CrossBorderScreeningDataset {
	
	private final CrossBorderIndicators crossBorderIndicators;
	
	@Autowired
	public CrossBorderScreeningDataset(CrossBorderIndicators crossBorderIndicators) {
		this.crossBorderIndicators = crossBorderIndicators;
	}
	
	public DataSetDefinition getCrossBorderScreeningDataset() {
		CohortIndicatorDataSetDefinition ind = new CohortIndicatorDataSetDefinition();
		ind.setName("random name");
		ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
		ind.addParameter(new Parameter("endDate", "End Date", Date.class));
		ind.addColumn("referral", "Number referrals", ReportUtils.map(
		    crossBorderIndicators.getCrossborderReferralPatients(), "startDate=${startDate},endDate=${endDate}"), "");
		
		return ind;
	}
}
