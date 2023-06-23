package org.openmrs.module.crossborder2.reporting.indicator;

import org.openmrs.module.crossborder2.reporting.cohort.CrossBorderCohortDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;
import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

@Component
public class CrossBorderIndicators {
	
	private final CrossBorderCohortDefinition crossBorderCohortDefinition;
	
	@Autowired
	public CrossBorderIndicators(CrossBorderCohortDefinition crossBorderCohortDefinition) {
		this.crossBorderCohortDefinition = crossBorderCohortDefinition;
	}
	
	public CohortIndicator getCrossborderReferralPatients() {
		return cohortIndicator("referral",
		    map(crossBorderCohortDefinition.getReferralPatients(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	//	public CohortIndicator getCrossborderScreeningPatients() {
	//		return cohortIndicator("screening",
	//		    map(crossBorderCohortDefinition.getCrossborderScreeningPatients(), "startDate=${startDate},endDate=${endDate}"));
	//	}
}
