package org.openmrs.module.crossborder2.reporting.cohort;

import org.openmrs.module.crossborder2.reporting.queries.CrossborderQueries;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.kenyaemr.reporting.library.ETLReports.MOH731Greencard.*;

import java.util.Date;

@Component
public class CrossBorderCohortDefinition {
	
	@Autowired
	private ETLMoh731GreenCardCohortLibrary etlMoh731GreenCardCohortLibrary;
	
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
	
	/**
	 * Crossborder Patients currently in care and on ART
	 * 
	 * @return
	 */
	public CohortDefinition getCrossBorderPatientsCurrentlyInCareAndOnART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("currentlyInCareAndOnART",
		    ReportUtils.map(etlMoh731GreenCardCohortLibrary.currentlyOnArt(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentlyInCareAndOnART AND crossBorderPatient");
		return cd;
	}
	
	/**
	 * Crossborder Patients Ever tested HIV
	 * 
	 * @return
	 */
	public CohortDefinition getCrossBorderPatientTestedForHIV() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("htsAllNumberTested", ReportUtils.map(etlMoh731GreenCardCohortLibrary.htsAllNumberTested(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("htsAllNumberTested AND crossBorderPatient");
		return cd;
	}
	
	/**
	 * Crossborder Patients Ever tested HIV positive
	 * 
	 * @return
	 */
	public CohortDefinition getCrossBorderPatientTestedHIVPositive() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("everTestedPositiveForHIV", ReportUtils.map(etlMoh731GreenCardCohortLibrary.everTestedHIVPositive(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("everTestedPositiveForHIV AND crossBorderPatient");
		return cd;
	}
	
	/**
	 * Crossborder HIV Positive Mothers who delivered
	 * 
	 * @return
	 */
	public CohortDefinition getCrossBorderHiVPositiveMothersWhoDelivered() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("deliveryFromHIVPositiveMothers", ReportUtils.map(
		    etlMoh731GreenCardCohortLibrary.deliveryFromHIVPositiveMothers(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("deliveryFromHIVPositiveMothers AND crossBorderPatient");
		return cd;
	}
	
	/**
	 * Crossborder Patients linked to care three months ago
	 * 
	 * @return
	 */
	public CohortDefinition getCrossBorderPatientsEnrolledInCareThreeMonthsAgo() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("enrolledInCareThreeMonthsAgo", ReportUtils.map(
		    etlMoh731GreenCardCohortLibrary.referredAndLinkedSinceThreeMonthsAgo(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("enrolledInCareThreeMonthsAgo AND crossBorderPatient");
		return cd;
	}
	
	/**
	 * Crossborder HIV Positive Mothers
	 * 
	 * @return
	 */
	public CohortDefinition getCrossBorderHiVPositiveMothersTotal() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("mothersPositiveTotal", ReportUtils.map(
		    etlMoh731GreenCardCohortLibrary.totalHivPositiveMothersInMchms(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("mothersPositiveTotal AND crossBorderPatient");
		return cd;
	}
	
	public CohortDefinition getCrossBorderMaternalHaarTTotal() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("totalMaternalHAART", ReportUtils.map(etlMoh731GreenCardCohortLibrary.totalMaternalHAART(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("totalMaternalHAART AND crossBorderPatient");
		return cd;
	}
	
	public CohortDefinition getCrossBorderPatientsOnProphylaxis() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("inHivProgramAndOnCtxProphylaxis", ReportUtils.map(
		    etlMoh731GreenCardCohortLibrary.inHivProgramAndOnCtxProphylaxis(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("inHivProgramAndOnCtxProphylaxis AND crossBorderPatient");
		return cd;
	}
	
	public CohortDefinition getCrossBorderPatientsCurrentlyNewEnrollementInCare() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("currentlyInCare",
		    ReportUtils.map(etlMoh731GreenCardCohortLibrary.hivEnrollment(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("currentlyInCare AND crossBorderPatient");
		return cd;
	}
	
	public CohortDefinition getCrossBorderPatientsStartedART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("startedOnART",
		    ReportUtils.map(etlMoh731GreenCardCohortLibrary.startedOnART(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("startedOnART AND crossBorderPatient");
		return cd;
	}
	
	/**
	 * Crossborder TB Total and HIV Positive (HV077+080)
	 * 
	 * @return
	 */
	public CohortDefinition getCrossBorderTBToTalHIVPositive() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("crossBorderPatient",
		    ReportUtils.map(getCrossBorderPatients(), "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("tbNewKnownPositive", ReportUtils.map(etlMoh731GreenCardCohortLibrary.tbNewKnownPositive(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.addSearch("tbNewTestedHIVPositive", ReportUtils.map(etlMoh731GreenCardCohortLibrary.tbNewTestedHIVPositive(),
		    "startDate=${startDate},endDate=${endDate}"));
		cd.setCompositionString("crossBorderPatient AND tbNewKnownPositive AND tbNewTestedHIVPositive");
		return cd;
	}
	
	public CohortDefinition getCrossBorderTransferOutPatients() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("CB Patients Transferred Out");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.crossBorderTransferOuts());
		return sqlCohortDefinition;
	}
	
	public CohortDefinition getCrossBorderTransferInsPatients() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("CB Patients Transferred Ins");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.crossBorderTransferIns());
		return sqlCohortDefinition;
	}
	
}
