package org.openmrs.module.crossborder2.reporting.cohort;

import org.openmrs.module.crossborder2.reporting.queries.CrossborderQueries;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.springframework.stereotype.Component;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.Date;

@Component
public class CrossBorderCohortDefination {
	
	public CohortDefinition getReferralPatients() {
		SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition();
		sqlCohortDefinition.setName("FFF");
		sqlCohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		sqlCohortDefinition.addParameter(new Parameter("endDate", "End  Date", Date.class));
		sqlCohortDefinition.setQuery(CrossborderQueries.getReferralPatients());
		return sqlCohortDefinition;
	}
}
