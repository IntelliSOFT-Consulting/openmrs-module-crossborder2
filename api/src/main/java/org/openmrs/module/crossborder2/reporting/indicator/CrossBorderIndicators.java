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
	
	public CohortIndicator getCbTxCurrPatients() {
		return cohortIndicator("referral",
		    map(crossBorderCohortDefinition.getCbTxCurrPatients(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderPatients() {
		return cohortIndicator("Cross Border Patients",
		    map(crossBorderCohortDefinition.getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getOtherNationalitiesAccessingCbServices() {
		return cohortIndicator(
		    "Other Nationalities Accessing CB Services",
		    map(crossBorderCohortDefinition.getOtherNationalitiesAccessingCbServices(),
		        "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getResidentsAccessingCbServices() {
		return cohortIndicator("Residents Accessing CB Services",
		    map(crossBorderCohortDefinition.getResidentsAccessingCbServices(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getNumberOfPatientsTravelledToAnotherCountryWithinTheYear() {
		return cohortIndicator(
		    "Patients Travelled Outside The Country Within 1 year",
		    map(crossBorderCohortDefinition.getNumberOfPatientsTravelledToAnotherCountryWithinTheYear(),
		        "startDate=${startDate},endDate=${endDate}"));
	}
	
	//	public CohortIndicator getCrossborderScreeningPatients() {
	//		return cohortIndicator("screening",
	//		    map(crossBorderCohortDefinition.getCrossborderScreeningPatients(), "startDate=${startDate},endDate=${endDate}"));
	//	}
}
