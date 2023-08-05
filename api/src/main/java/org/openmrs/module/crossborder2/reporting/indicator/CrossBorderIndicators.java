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
	
	public CohortIndicator getCrossBorderPatientsCurrentlyInCareAndOnART() {
		return cohortIndicator(
		    "TX-CURR: of adults and children currently receiving ART",
		    map(crossBorderCohortDefinition.getCrossBorderPatientsCurrentlyInCareAndOnART(),
		        "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderPatientTestedForHIV() {
		return cohortIndicator(
		    "HIV Tested total",
		    map(crossBorderCohortDefinition.getCrossBorderPatientTestedForHIV(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderHiVPositiveMothersWhoDelivered() {
		return cohortIndicator(
		    "Delivery from HIV+ Mothers",
		    map(crossBorderCohortDefinition.getCrossBorderHiVPositiveMothersWhoDelivered(),
		        "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderHiVPositiveMothersTotal() {
		return cohortIndicator(
		    "Mothers Positive Total",
		    map(crossBorderCohortDefinition.getCrossBorderHiVPositiveMothersTotal(),
		        "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderMaternalHaarTTotal() {
		return cohortIndicator("Maternal HAART Total",
		    map(crossBorderCohortDefinition.getCrossBorderMaternalHaarTTotal(), "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderPatientsOnProphylaxis() {
		return cohortIndicator(
		    "Total ARV Prophylaxis Total",
		    map(crossBorderCohortDefinition.getCrossBorderPatientsOnProphylaxis(),
		        "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderPatientsCurrentlyInCare() {
		return cohortIndicator(
		    "Total Enrollment in Care",
		    map(crossBorderCohortDefinition.getCrossBorderPatientsCurrentlyInCare(),
		        "startDate=${startDate},endDate=${endDate}"));
	}
	
	public CohortIndicator getCrossBorderPatientsStartedART() {
		return cohortIndicator("Start ART Total",
		    map(crossBorderCohortDefinition.getCrossBorderPatientsStartedART(), "startDate=${startDate},endDate=${endDate}"));
	}
	
}
