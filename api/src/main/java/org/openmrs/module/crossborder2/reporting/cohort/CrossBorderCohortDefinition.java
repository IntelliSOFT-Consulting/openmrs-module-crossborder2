package org.openmrs.module.crossborder2.reporting.cohort;

import org.openmrs.module.crossborder2.reporting.queries.CrossborderQueries;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.springframework.stereotype.Component;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

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
	
	public CohortDefinition getCbTxCurrPatients() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("Referral");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getCrossBorderTxCurrPatientsIndicatorReport());
		return sqlCohortDefinition;
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
		sqlCohortDefinition.setName("Other Nationalities Accessing CB Services");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getOtherNationalitiesAccessingCbServices());
		return sqlCohortDefinition;
	}
	
	public CohortDefinition getResidentsAccessingCbServices() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("Residents Accessing CB Services");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getResidentsAccessingCbServices());
		return sqlCohortDefinition;
	}
	
	public CohortDefinition getNumberOfPatientsTravelledToAnotherCountryWithinTheYear() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("Patients Travelled Outside The Country Within 1 year");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getNumberOfPatientsTravelledToAnotherCountryWithinTheYear());
		return sqlCohortDefinition;
	}
}
