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
		return "SELECT\n" +
				"    ecs.visit_date,\n" +
				"    CONCAT(COALESCE(epd.given_name, ''), ' ', COALESCE(epd.middle_name, ''), ' ', COALESCE(epd.family_name, '')) AS 'Patient Names',\n" +
				"    epd.Gender,\n" +
				"    epd.DOB,\n" +
				"    cn_country.name AS 'Country of Residence',\n" +
				"    cn_nationality.name As 'Nationality',\n" +
				"    cn_target_pop.name AS 'Target Population',\n" +
				"    cn_threemonths.name As 'Traveled last 3 months',\n" +
				"    cn_sixmonths.name As 'Traveled last 6 months',\n" +
				"    cn_twelvemonths.name As 'Traveled last 12 months',\n" +
				"    ecs.duration_of_stay,\n" +
				"    cn_travelFrequency.name As 'Frequency of travel',\n" +
				"    cn_serviceType.name As 'Type of Service'\n" +
				"FROM\n" +
				"    kenyaemr_etl.etl_crossborder_screening ecs\n" +
				"INNER JOIN\n" +
				"    kenyaemr_etl.etl_patient_demographics epd ON ecs.patient_id = epd.patient_id\n" +
				"INNER JOIN\n" +
				"    concept_name cn_country ON cn_country.concept_id = ecs.place_of_residence_country and cn_country.concept_name_type = 'FULLY_SPECIFIED'\n" +
				"INNER JOIN\n" +
				"    concept_name cn_target_pop ON cn_target_pop.concept_id = ecs.target_population and cn_target_pop.concept_name_type = 'FULLY_SPECIFIED'  \n" +
				"INNER JOIN\n" +
				"    concept_name cn_nationality ON cn_nationality.concept_id = ecs.nationality and cn_nationality.concept_name_type = 'FULLY_SPECIFIED' \n" +
				"INNER JOIN\n" +
				"    concept_name cn_threemonths ON cn_threemonths.concept_id = ecs.traveled_last_3_months and cn_threemonths.concept_name_type = 'FULLY_SPECIFIED' and cn_threemonths.locale = 'en' \n" +
				"INNER JOIN\n" +
				"    concept_name cn_sixmonths ON cn_sixmonths.concept_id = ecs.traveled_last_6_months and cn_sixmonths.concept_name_type = 'FULLY_SPECIFIED' and cn_sixmonths.locale = 'en'     \n" +
				"INNER JOIN\n" +
				"    concept_name cn_twelvemonths ON cn_twelvemonths.concept_id = ecs.traveled_last_12_months and cn_twelvemonths.concept_name_type = 'FULLY_SPECIFIED' and cn_twelvemonths.locale = 'en'     \n" +
				"INNER JOIN\n" +
				"    concept_name cn_travelFrequency ON cn_travelFrequency.concept_id = ecs.frequency_of_travel and cn_travelFrequency.concept_name_type = 'FULLY_SPECIFIED' and cn_travelFrequency.locale = 'en'     \n" +
				"INNER JOIN\n" +
				"    concept_name cn_serviceType ON cn_serviceType.concept_id = ecs.type_of_service and cn_serviceType.concept_name_type = 'FULLY_SPECIFIED' and cn_serviceType.locale = 'en'     \n" +
				" where visit_date between :startDate AND :endDate";
	}
	
	public static String getCrossBorderScreeningIndicatorReport() {
		return "select patient_id from kenyaemr_etl.etl_crossborder_screening where visit_date between :startDate AND :endDate";
	}
}
