package org.openmrs.module.crossborder2.reporting.cohort;

import org.openmrs.module.crossborder2.reporting.queries.CrossborderQueries;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.springframework.stereotype.Component;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.kenyaemr.reporting.library.ETLReports.MOH731Greencard.*;

import java.util.Date;

@Component
public class CrossBorderCohortDefinition {
	
	public CohortDefinition getReferralPatients() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("Referral");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getCrossBorderScreeningIndicatorReport());
		return sqlCohortDefinition;
	}
	
	public CohortDefinition genderCohort(String flag) {
		GenderCohortDefinition gcd = new GenderCohortDefinition();
		gcd.setName("Gender Cohort");
		if (flag.equals("M")) {
			gcd.setFemaleIncluded(false);
			gcd.setMaleIncluded(true);
		} else if (flag.equals("F")) {
			gcd.setFemaleIncluded(true);
			gcd.setMaleIncluded(false);
		}
		return gcd;
	}
	
	public CohortDefinition getCrossBorderPatients() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("Cross Border Patients");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getCrossBorderPatients());
		return sqlCohortDefinition;
	}
	
	public CohortDefinition getOtherNationalitiesAccessingCbServices() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("Number of other nationalities accessing specified services");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getOtherNationalitiesAccessingCbServices());
		return sqlCohortDefinition;
	}
	
	public CohortDefinition getResidentsAccessingCbServices() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition
		        .setName("Number of residents of other districts/counties accessing services at the border facility");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getResidentsAccessingCbServices());
		return sqlCohortDefinition;
	}
	
	public CohortDefinition getNumberOfPatientsTravelledToAnotherCountryWithinTheYear() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("Number reporting travelled to another country last 3/6/12 months");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getNumberOfPatientsTravelledToAnotherCountryWithinTheYear());
		return sqlCohortDefinition;
	}
	
	//Review this class
	public CohortDefinition getCrossBorderPatientsCurrentlyInCareAndOnART() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentlyInCareAndOnART",
		    ReportUtils.map(etlMoh731GreenCardCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentlyInCareAndOnART");
		return cd;
	}
	
	public CohortDefinition getCrossBorderPatientTestedForHIV() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("htsAllNumberTested", ReportUtils.map(etlMoh731GreenCardCohortLibrary.htsAllNumberTested(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("htsAllNumberTested");
		return cd;
	}
	
	public CohortDefinition getCrossBorderHiVPositiveMothersWhoDelivered() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("deliveryFromHIVPositiveMothers", ReportUtils.map(
		    etlMoh731GreenCardCohortLibrary.deliveryFromHIVPositiveMothers(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("deliveryFromHIVPositiveMothers");
		return cd;
	}
	
	public CohortDefinition getCrossBorderHiVPositiveMothersTotal() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("mothersPositiveTotal", ReportUtils.map(
		    etlMoh731GreenCardCohortLibrary.totalHivPositiveMothersInMchms(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("mothersPositiveTotal");
		return cd;
	}
	
	public CohortDefinition getCrossBorderMaternalHaarTTotal() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("totalMaternalHAART", ReportUtils.map(etlMoh731GreenCardCohortLibrary.totalMaternalHAART(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("totalMaternalHAART");
		return cd;
	}
	
	public CohortDefinition getCrossBorderPatientsOnProphylaxis() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("inHivProgramAndOnCtxProphylaxis", ReportUtils.map(
		    etlMoh731GreenCardCohortLibrary.inHivProgramAndOnCtxProphylaxis(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("inHivProgramAndOnCtxProphylaxis");
		return cd;
	}
	
	public CohortDefinition getCrossBorderPatientsCurrentlyInCare() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("currentlyInCare",
		    ReportUtils.map(etlMoh731GreenCardCohortLibrary.currentlyInCare(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentlyInCare");
		return cd;
	}
	
	public CohortDefinition getCrossBorderPatientsStartedART() {
		ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary = new ETLMoh731GreenCardCohortLibrary();
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("startedOnART",
		    ReportUtils.map(etlMoh731GreenCardCohortLibrary.startedOnART(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("startedOnART");
		return cd;
	}
	
}
