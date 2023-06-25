package org.openmrs.module.crossborder2.reporting.dimension;

import org.openmrs.module.crossborder2.reporting.cohort.CrossBorderCohortDefinition;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonDimensions {
	
	private final CrossBorderCohortDefinition crossBorderCohortDefinition;
	
	@Autowired
	public CommonDimensions(CrossBorderCohortDefinition crossBorderCohortDefinition) {
		this.crossBorderCohortDefinition = crossBorderCohortDefinition;
	}
	
	public CohortDefinitionDimension getGender() {
		CohortDefinitionDimension dm = new CohortDefinitionDimension();
		dm.setName("gender");
		dm.addCohortDefinition("F", ReportUtils.map(crossBorderCohortDefinition.genderCohort("F"), ""));
		dm.addCohortDefinition("M", ReportUtils.map(crossBorderCohortDefinition.genderCohort("M"), ""));
		return dm;
	}
}
