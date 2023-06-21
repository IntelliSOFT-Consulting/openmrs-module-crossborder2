DELIMITER $$
DROP PROCEDURE IF EXISTS create_crossborder_etl_tables $$
CREATE PROCEDURE create_crossborder_etl_tables()
BEGIN
DECLARE script_id INT(11);

DROP TABLE IF EXISTS kenyaemr_etl.etl_script_status;
CREATE TABLE kenyaemr_etl.etl_script_status(
  id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  script_name VARCHAR(50) DEFAULT null,
  start_time DATETIME DEFAULT NULL,
  stop_time DATETIME DEFAULT NULL,
  error VARCHAR(255) DEFAULT NULL
);

-- Log start time
INSERT INTO kenyaemr_etl.etl_script_status(script_name, start_time) VALUES('initial_creation_of_tables', NOW());
SET script_id = LAST_INSERT_ID();

DROP TABLE IF EXISTS kenyaemr_etl.etl_crossborder_referral;
DROP TABLE IF EXISTS kenyaemr_etl.etl_crossbborder_screening;


-- Create table etl_clinical_referral --
create table kenyaemr_etl.etl_crossborder_referral(
  patient_id INT,
  encounter_id INT,
  visit_id INT,
  visit_date DATE,
  nationality INT(15),
  nationality_other VARCHAR(255),
  referring_facility_name VARCHAR(255) not null,
  referred_facility_name VARCHAR(255) not null,
  type_of_care INT(15),
  type_of_care_other VARCHAR(255),
  date_of_referral DATETIME,
  lhss_site VARCHAR(255),
  target_population INT(15),
  target_population_other VARCHAR(255),
  hypersensitivity VARCHAR(255),
  poor_adherence VARCHAR(255),
  reason_for_referral VARCHAR(255),
  reason_for_referral_other VARCHAR(255),
  general_comments_if_reffered VARCHAR(255),
  referral_recommendation_continue_ctx VARCHAR(255),
  referral_recommendation_eligible_for_art VARCHAR(255),
  referral_recommendation_continue_art VARCHAR(255),
  date_created DATETIME,
  date_last_modified DATETIME,
  INDEX(patient_id),
  INDEX(visit_id),
  INDEX(visit_date),
  INDEX(encounter_id)
);
SELECT "Successfully created etl_crossborder_referral table";

-- Create table etl_crossbborder_screening--
create table kenyaemr_etl.etl_crossbborder_screening(
  patient_id INT,
  encounter_id INT,
  visit_id INT,
  visit_date DATE,
  country VARCHAR(255),
  lhss_site VARCHAR(255),
  place_of_residence_country VARCHAR(255),
  place_of_residence_county_district VARCHAR(255),
  place_of_residence_village VARCHAR(255),
  place_of_residence_landmark VARCHAR(255),
  nationality INT,
  nationality_other VARCHAR(255),
  target_population INT,
  target_population_other VARCHAR(255),
  traveled_last_3_months INT,
  traveled_last_6_months INT,
  traveled_last_12_months INT,
  duration_of_stay INT(15),
  frequency_of_travel INT,
  frequency_of_travel_other VARCHAR(255),
  type_of_service INT,
  service_other VARCHAR(255),
  INDEX(patient_id),
  INDEX(visit_id),
  INDEX(visit_date)
);
SELECT "Successfully created etl_crossbborder_screening table";

-- UPDATE kenyaemr_etl.etl_script_status SET stop_time=NOW() where id= script_id;

END $$
DELIMETER;
