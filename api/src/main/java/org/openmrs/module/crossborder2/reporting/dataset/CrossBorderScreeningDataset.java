package org.openmrs.module.crossborder2.reporting.dataset;

import org.openmrs.module.crossborder2.reporting.dimension.CommonDimensions;
import org.openmrs.module.crossborder2.reporting.indicator.CrossBorderIndicators;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CrossBorderScreeningDataset {
	
	private final CrossBorderIndicators crossBorderIndicators;
	
	private final CommonDimensions commonDimensions;
	
	@Autowired
	public CrossBorderScreeningDataset(CrossBorderIndicators crossBorderIndicators, CommonDimensions commonDimensions) {
		this.crossBorderIndicators = crossBorderIndicators;
		this.commonDimensions = commonDimensions;
	}
	
	public DataSetDefinition getCrossBorderScreeningDataset() {
		String indParams = "startDate=${startDate},endDate=${endDate}";
		
		CohortIndicatorDataSetDefinition ind = new CohortIndicatorDataSetDefinition();
		ind.setName("random name");
		ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
		ind.addParameter(new Parameter("endDate", "End Date", Date.class));
		ind.addDimension("gender", ReportUtils.map(commonDimensions.getGender(), ""));
		EmrReportingUtils.addRow(ind, "SC", "Screening",
		    ReportUtils.map(crossBorderIndicators.getCrossborderReferralPatients(), indParams), getGender(),
		    Arrays.asList("01", "02", "03"));
		
		return ind;
	}
	
	public DataSetDefinition getCbTxCurrDataset() {
		String indParams = "startDate=${startDate},endDate=${endDate}";
		
		CohortIndicatorDataSetDefinition ind = new CohortIndicatorDataSetDefinition();
		ind.setName("random name");
		ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
		ind.addParameter(new Parameter("endDate", "End Date", Date.class));
		ind.addDimension("gender", ReportUtils.map(commonDimensions.getGender(), ""));
		EmrReportingUtils.addRow(ind, "CB Patients", "CB Patients",
		    ReportUtils.map(crossBorderIndicators.getCrossBorderPatients(), indParams), getGender(),
		    Arrays.asList("01", "02", "03"));
		
		EmrReportingUtils.addRow(ind, "Other Nationalities", "Other Nationalities",
		    ReportUtils.map(crossBorderIndicators.getOtherNationalitiesAccessingCbServices(), indParams), getGender(),
		    Arrays.asList("01", "02", "03"));
		
		EmrReportingUtils.addRow(ind, "Residents", "Residents",
		    ReportUtils.map(crossBorderIndicators.getResidentsAccessingCbServices(), indParams), getGender(),
		    Arrays.asList("01", "02", "03"));
		
		EmrReportingUtils.addRow(ind, "Travelling  Patients", "Travelling  Patients",
		    ReportUtils.map(crossBorderIndicators.getNumberOfPatientsTravelledToAnotherCountryWithinTheYear(), indParams),
		    getGender(), Arrays.asList("01", "02", "03"));
		
		return ind;
	}
	
	private List<ColumnParameters> getGender() {
		ColumnParameters cpMale = new ColumnParameters("Male", "Male", "gender=M");
		ColumnParameters cpFemale = new ColumnParameters("Female", "Female", "gender=F");
		ColumnParameters cpTotal = new ColumnParameters("Total", "Total", "");
		return Arrays.asList(cpMale, cpFemale, cpTotal);
	}
}
