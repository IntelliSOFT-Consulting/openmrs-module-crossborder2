package org.openmrs.module.crossborder2.reporting.queries;

public class CrossborderQueries {
	
	public static String getCrossborderReferralPatients() {
		return "SELECT \n" + "\tr.visit_date ,\n"
		        + "\tepd.given_name + ' ' + epd.middle_name + ' ' + epd.family_name as patient_name,\n" + "\tepd.Gender, \n"
		        + "\tepd.DOB,\n" + "\tepd.national_id_no,\n" + "\tr.nationality ,\n" + "\tr.referring_facility_name ,\n"
		        + "\tr.target_population ,\n" + "\tr.hypersensitivity ,\n" + "\tr.poor_adherence ,\n"
		        + "\tr.reason_for_referral ,\n" + "\tr.general_comments_if_reffered as general_comments,\n"
		        + "\tr.referral_recommendation_continue_ctx as continue_ctx,\n"
		        + "\tr.referral_recommendation_eligible_for_art as eligible_art,\n"
		        + "\tr.referral_recommendation_continue_art  as continue_art\n" + "FROM \n"
		        + "\tkenyaemr_etl.etl_crossborder_referral r\n"
		        + "\tINNER JOIN kenyaemr_etl.etl_patient_demographics epd  on r.patient_id = pn.patient_id \n"
		        + "WHERE r.visit_date BETWEEN :startDate and :endDate;\n";
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
		        + "INNER JOIN kenyaemr_etl.etl_patient_demographics epd  on epd.patient_id = pn.patient_id \n"
		        + "WHERE ecs.visit_date BETWEEN :startDate and :endDate";
	}
	
	public static String getCrossBorderScreeningIndicatorReport() {
		return "select patient_id from kenyaemr_etl.etl_crossborder_screening where visit_date between :startDate AND :endDate";
	}
}
