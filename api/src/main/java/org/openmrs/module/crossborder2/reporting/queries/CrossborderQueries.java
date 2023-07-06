package org.openmrs.module.crossborder2.reporting.queries;

public class CrossborderQueries {
	
	public static String getCrossborderReferralPatients() {
		return "SELECT \n"
		        + "visit_date as 'Visit Date',\n"
		        + " CONCAT(COALESCE(epd.given_name, ''), ' ', COALESCE(epd.middle_name, ''), ' ', COALESCE(epd.family_name, '')) AS 'Patient Names',\n"
		        + " cn_target_pop.name As 'Target Population',\n"
		        + " cn_nationality.name As 'Nationality',\n"
		        + " referring_facility_name as 'Referring Facility Name',\n"
		        + " referred_facility_name as 'Referred Facility Name',\n"
		        + " cn_careType.name as 'Type OF Care',\n"
		        + " date_of_referral as 'Referral Date',\n"
		        + " cn_referralReason.name as 'Referral Reason',\n"
		        + " general_comments_if_reffered as 'General Comments',\n"
		        + " referral_recommendation_continue_art as 'Referral Recommendation',\n"
		        + " referring_hc_provider as 'Referring HC Provider',\n"
		        + " referring_hc_provider_email as 'Referring HC Provider Email',\n"
		        + " referring_hc_provider_telephone as 'Referring HC Provider Telephone',\n"
		        + " referring_hc_provider_cadre as 'Referring HC Provider Cadre'\n"
		        + "FROM \n"
		        + "kenyaemr_etl.etl_crossborder_referral ref\n"
		        + "INNER JOIN\n"
		        + "    kenyaemr_etl.etl_patient_demographics epd ON ref.patient_id = epd.patient_id \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_target_pop ON cn_target_pop.concept_id = ref.target_population and cn_target_pop.concept_name_type = 'FULLY_SPECIFIED' and cn_target_pop.locale = \"en\"   \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_nationality ON cn_nationality.concept_id = ref.nationality and cn_nationality.concept_name_type = 'FULLY_SPECIFIED' and cn_nationality.locale = \"en\"   \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_careType ON cn_careType.concept_id = ref.type_of_care and cn_careType.concept_name_type = 'FULLY_SPECIFIED' and cn_careType.locale = \"en\"    \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_referralReason ON cn_referralReason.concept_id = ref.reason_for_referral and cn_referralReason.concept_name_type = 'FULLY_SPECIFIED' and cn_referralReason.locale = \"en\"   \n"
		        + "and visit_date between :startDate AND :endDate ";
	}
	
	public static String getCrossborderScreeningPatients() {
		return "SELECT\n"
		        + "    ecs.visit_date,\n"
		        + "    CONCAT(COALESCE(epd.given_name, ''), ' ', COALESCE(epd.middle_name, ''), ' ', COALESCE(epd.family_name, '')) AS 'Patient Names',\n"
		        + "    epd.Gender,\n"
		        + "    epd.DOB,\n"
		        + "    cn_country.name AS 'Country of Residence',\n"
		        + "    cn_nationality.name As 'Nationality',\n"
		        + "    cn_target_pop.name AS 'Target Population',\n"
		        + "    cn_threemonths.name As 'traveled_last_3_months',\n"
		        + "    cn_sixmonths.name As 'traveled_last_6_months',\n"
		        + "    cn_twelvemonths.name As 'traveled_last_12_months',\n"
		        + "    ecs.duration_of_stay,\n"
		        + "    cn_travelFrequency.name As 'frequency_of_travel',\n"
		        + "    cn_serviceType.name As 'type_of_service'\n"
		        + "FROM\n"
		        + "    kenyaemr_etl.etl_crossborder_screening ecs\n"
		        + "INNER JOIN\n"
		        + "    kenyaemr_etl.etl_patient_demographics epd ON ecs.patient_id = epd.patient_id\n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_country ON cn_country.concept_id = ecs.place_of_residence_country and cn_country.concept_name_type = 'FULLY_SPECIFIED' and cn_country.locale = 'en'\n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_target_pop ON cn_target_pop.concept_id = ecs.target_population and cn_target_pop.concept_name_type = 'FULLY_SPECIFIED'and cn_target_pop.locale = 'en'  \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_nationality ON cn_nationality.concept_id = ecs.nationality and cn_nationality.concept_name_type = 'FULLY_SPECIFIED' and cn_nationality.locale = 'en'\n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_threemonths ON cn_threemonths.concept_id = ecs.traveled_last_3_months and cn_threemonths.concept_name_type = 'FULLY_SPECIFIED' and cn_threemonths.locale = 'en' \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_sixmonths ON cn_sixmonths.concept_id = ecs.traveled_last_6_months and cn_sixmonths.concept_name_type = 'FULLY_SPECIFIED' and cn_sixmonths.locale = 'en'     \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_twelvemonths ON cn_twelvemonths.concept_id = ecs.traveled_last_12_months and cn_twelvemonths.concept_name_type = 'FULLY_SPECIFIED' and cn_twelvemonths.locale = 'en'     \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_travelFrequency ON cn_travelFrequency.concept_id = ecs.frequency_of_travel and cn_travelFrequency.concept_name_type = 'FULLY_SPECIFIED' and cn_travelFrequency.locale = 'en'     \n"
		        + "INNER JOIN\n"
		        + "    concept_name cn_serviceType ON cn_serviceType.concept_id = ecs.type_of_service and cn_serviceType.concept_name_type = 'FULLY_SPECIFIED' and cn_serviceType.locale = 'en'     \n"
		        + " where visit_date between :startDate AND :endDate";
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
