package org.openmrs.module.crossborder2.reporting.indicator;

import org.openmrs.module.crossborder2.reporting.cohort.CrossBorderCohortDefination;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;
import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

@Component
public class CrossBorderIndicators {
	
	private final CrossBorderCohortDefination crossBorderCohortDefinition;
	
	public CrossBorderIndicators(CrossBorderCohortDefination crossBorderCohortDefinition) {
		this.crossBorderCohortDefinition = crossBorderCohortDefinition;
	}
	
	public CohortIndicator getRefeeralPatints() {
		return cohortIndicator("referral",
		    map(crossBorderCohortDefinition.getReferralPatients(), "startDate=${startDate},endDate=${endDate}"));
	}
}
