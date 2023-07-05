package org.openmrs.module.crossborder2.reporting.queries;

public class CrossborderQueries {
	
	public static String getCrossborderReferralPatients() {
		return "SELECT \n" +
				"visit_date as 'Visit Date',\n" +
				" CONCAT(COALESCE(epd.given_name, ''), ' ', COALESCE(epd.middle_name, ''), ' ', COALESCE(epd.family_name, '')) AS 'Patient Names',\n" +
				" cn_target_pop.name As 'Target Population',\n" +
				" cn_nationality.name As 'Nationality',\n" +
				" referring_facility_name as 'Referring Facility Name',\n" +
				" referred_facility_name as 'Referred Facility Name',\n" +
				" cn_careType.name as 'Type OF Care',\n" +
				" date_of_referral as 'Referral Date',\n" +
				" cn_referralReason.name as 'Referral Reason',\n" +
				" general_comments_if_reffered as 'General Comments',\n" +
				" referral_recommendation_continue_art as 'Referral Recommendation',\n" +
				" referring_hc_provider as 'Referring HC Provider',\n" +
				" referring_hc_provider_email as 'Referring HC Provider Email',\n" +
				" referring_hc_provider_telephone as 'Referring HC Provider Telephone',\n" +
				" referring_hc_provider_cadre as 'Referring HC Provider Telephone'\n" +
				"FROM \n" +
				"kenyaemr_etl.etl_crossborder_referral ref\n" +
				"INNER JOIN\n" +
				"    kenyaemr_etl.etl_patient_demographics epd ON ref.patient_id = epd.patient_id \n" +
				"INNER JOIN\n" +
				"    concept_name cn_target_pop ON cn_target_pop.concept_id = ref.target_population and cn_target_pop.concept_name_type = 'FULLY_SPECIFIED'  \n" +
				"INNER JOIN\n" +
				"    concept_name cn_nationality ON cn_nationality.concept_id = ref.nationality and cn_nationality.concept_name_type = 'FULLY_SPECIFIED' \n" +
				"INNER JOIN\n" +
				"    concept_name cn_careType ON cn_careType.concept_id = ref.type_of_care and cn_careType.concept_name_type = 'FULLY_SPECIFIED'   \n" +
				"INNER JOIN\n" +
				"    concept_name cn_referralReason ON cn_referralReason.concept_id = ref.reason_for_referral and cn_referralReason.concept_name_type = 'FULLY_SPECIFIED' and cn_referralReason.locale = \"en\"   \n" +
				"and visit_date between :startDate and :endDate";
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
}
