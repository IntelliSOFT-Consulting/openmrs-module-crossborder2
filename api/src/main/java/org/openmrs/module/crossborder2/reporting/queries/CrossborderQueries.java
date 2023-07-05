package org.openmrs.module.crossborder2.reporting.queries;

public class CrossborderQueries {
	
	public static String getCrossborderReferralPatients() {
		return "SELECT \n" + "\tr.visit_date ,\n"
		        + "\tepd.given_name + ' ' + epd.middle_name + ' ' + epd.family_name as patient_name,\n" + "\tepd.Gender, \n"
		        + "\tepd.DOB,\n" + "\tepd.national_id_no,\n" + "\tr.nationality ,\n" + "\tr.referring_facility_name ,\n"
		        + "\tr.target_population ,\n" + "\tr.reason_for_referral ,\n"
		        + "\tr.general_comments_if_reffered as general_comments,\n"
		        + "\tr.referral_recommendation_continue_art  as continue_art\n" + "FROM \n"
		        + "\tkenyaemr_etl.etl_crossborder_referral r\n"
		        + "\tINNER JOIN kenyaemr_etl.etl_patient_demographics epd  on r.patient_id = epd.patient_id \n"
		        + "WHERE r.visit_date BETWEEN :startDate and :endDate";
	}
	
	public static String getCrossborderScreeningPatients() {
		return "SELECT \n" + "ecs.visit_date ,\n"
		        + "epd.given_name + ' ' + epd.middle_name + ' ' + epd.family_name as patient_name,\n" + "epd.Gender, \n"
		        + "epd.DOB,\n" + "epd.national_id_no,\n" + "ecs.place_of_residence_country as country_of_residence,\n"
		        + "ecs.place_of_residence_village as village_of_residence,\n"
		        + "ecs.place_of_residence_landmark as landmark,\n" + "ecs.nationality ,\n" + "ecs.target_population ,\n"
		        + "ecs.traveled_last_3_months ,\n" + "ecs.traveled_last_6_months ,\n" + "ecs.traveled_last_12_months ,\n"
		        + "ecs.duration_of_stay ,\n" + "ecs.frequency_of_travel ,\n" + "ecs.type_of_service \n"
		        + "FROM kenyaemr_etl.etl_crossborder_screening ecs \n"
		        + "INNER JOIN kenyaemr_etl.etl_patient_demographics epd  on ecs.patient_id = epd.patient_id \n"
		        + "WHERE ecs.visit_date BETWEEN :startDate and :endDate";
	}
	
	public static String getCrossBorderScreeningIndicatorReport() {
		return "select patient_id from kenyaemr_etl.etl_crossborder_screening where visit_date between :startDate AND :endDate";
	}
	
	public static String getCrossBorderTxCurrPatientsIndicatorReport() {
		return "select patient_id from kenyaemr_etl.etl_crossborder_screening where visit_date between :startDate AND :endDate";
	}
	
	//List crossborder patients
	public static String getCrossBorderPatients() {
		return "select patient_id from kenyaemr_etl.etl_crossborder_screening where\n"
		        + "((place_of_residence_country = 162883 and nationality in (5000025,5000026,5000027,5000028,5000029)) \n"
		        + "or place_of_residence_country = 162884 and nationality in (5000024,5000026,5000027,5000028,5000029))\n"
		        + "and visit_date between  :startDate and :endDate";
	}
	
	//Number crossborder nationalities accessing services
	public static String getOtherNationalitiesAccessingCbServices() {
		return "select patient_id \n" + "FROM kenyaemr_etl.etl_crossborder_screening where \n"
		        + "nationality not in (5000024) and\n" + "visit_date between :startDate and :endDate";
	}
	
	//Number of residents accessing CB services
	public static String getResidentsAccessingCbServices() {
		return "select patient_id \n" + "FROM kenyaemr_etl.etl_crossborder_screening where \n"
		        + "(place_of_residence_country = 162883) and nationality in (5000024)  and\n"
		        + "visit_date between :startDate and :endDate";
	}
	
	//Number patients travelled outside the country within the year.
	public static String getNumberOfPatientsTravelledToAnotherCountryWithinTheYear() {
		return "select patient_id \n"
		        + "FROM kenyaemr_etl.etl_crossborder_screening where \n"
		        + "((traveled_last_3_months = 1065) or (traveled_last_6_months = 1065) or (traveled_last_12_months = 1065 )) and\n"
		        + "visit_date between :startDate and :endDate";
	}
	
}
