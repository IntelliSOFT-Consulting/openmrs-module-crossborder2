package org.openmrs.module.crossborder2;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CbAppUtils {
	
	public static Set<Integer> male(Collection<Integer> cohort, PatientCalculationContext context) {
		return CalculationUtils.patientsThatPass(Calculations.genders(cohort, context), "M");
	}
	
	public static Double calculateBmi(Double w, Double h) {
		Double bmi = 0.0;
		
		if (w != null && h != null) {
			double convertedHeihgt = h / 100;
			double productHeight = convertedHeihgt * convertedHeihgt;
			bmi = w / productHeight;
		}
		
		return bmi;
	}
	
	public static String formatDate(Date date) {
		
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		return formatter.format(date);
	}
	
	public static CohortDefinition allDmHtnForScreeningPatientCohort(int enc1) {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("Active Patients in the DM and HTN screening encounter type");
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		cd.setQuery("SELECT p.patient_id FROM patient p INNER JOIN encounter e ON p.patient_id=e.patient_id WHERE e.encounter_datetime BETWEEN :startDate AND :endDate AND  e.encounter_type IN("
		        + enc1 + ")");
		return cd;
	}
	
	public static CohortDefinition allDmHtnProgramPatientCohort(int program) {
		SqlCohortDefinition cd = new SqlCohortDefinition();
		cd.setName("Active Patients in the DM and HTN Program");
		cd.setQuery("SELECT p.patient_id FROM patient p INNER JOIN patient_program pg ON p.patient_id=pg.patient_id INNER JOIN program prg ON prg.program_id=pg.program_id WHERE prg.program_id="
		        + program);
		return cd;
	}
	
	public static List<Encounter> getBasePatientsToWorkWith(Location location) {
		EncounterType screeningForm = Context.getEncounterService().getEncounterTypeByUuid(
		    "af5dbd36-18f9-11eb-ae6b-7f4c0920f004");
		EncounterType initialForm = Context.getEncounterService().getEncounterTypeByUuid(
		    "cb5f27f0-18f8-11eb-88d7-fb1a7178f8ea");
		EncounterType followupForm = Context.getEncounterService().getEncounterTypeByUuid(
		    "f1573d1c-18f8-11eb-a453-63d51e56f5cb");
		List<Encounter> allEncounters = Context.getEncounterService().getEncounters(null, location, null, null, null,
		    Arrays.asList(screeningForm, initialForm, followupForm), null, null, null, false);
		return allEncounters;
	}
}
