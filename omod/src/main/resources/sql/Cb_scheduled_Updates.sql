
-- Update crossborder screening procedure ----
DROP PROCEDURE IF EXISTS sp_update_etl_crossborder_screening $$
CREATE PROCEDURE sp_update_etl_crossborder_screening(IN last_update_time DATETIME)

BEGIN

INSERT INTO kenyaemr_etl.etl_crossborder_screening
    (patient_id,
    visit_id,
    visit_date,
    location_id,
    encounter_id,
    creator,
    date_created,
    date_last_modified,
    place_of_residence_country,
    nationality,
    target_population,
    traveled_last_3_months,
    traveled_last_6_months,
    traveled_last_12_months,
    duration_of_stay,
    frequency_of_travel,
    type_of_service)
select
    e.patient_id,
    e.visit_id,
    e.encounter_datetime as visit_date,
    e.location_id,
    e.encounter_id,
    e.creator,
    e.date_created,
    if(max(o.date_created) > min(e.date_created),max(o.date_created),NULL) as date_last_modified,
    max(if(o.concept_id=165915,o.value_coded,null)) as place_of_residence_country,
    max(if(o.concept_id=165915,o.value_coded,null)) as nationality,
    max(if(o.concept_id=166433,o.value_coded,null)) as target_population,
    max(if(o.concept_id=162619,o.value_coded,null)) as traveled_last_3_months,
    max(if(o.concept_id=165656,o.value_coded,null)) as traveled_last_6_months,
    max(if(o.concept_id=162619,o.value_coded,null)) as traveled_last_12_months,
    max(if(o.concept_id=162603,o.value_numeric,null)) as duration_of_stay,
    max(if(o.concept_id=1732,o.value_coded,null)) as frequency_of_travel,
    max(if(o.concept_id=168146,o.value_coded,null)) as type_of_service
from encounter e
         inner join
     (
         select encounter_type_id, uuid, name from encounter_type where uuid='6536A8A3-7B77-414D-A0F0-E08A7178FF0F'
     ) et on et.encounter_type_id=e.encounter_type
         inner join person p on p.person_id=e.patient_id and p.voided=0
         left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0
    and o.concept_id in (165915,165915,166433,162619,165656,162619,162603,1732,168146)
where e.voided=0 and e.date_created >= last_update_time
   or e.date_changed >=  last_update_time
   or e.date_voided >=  last_update_time
   or o.date_created >= last_update_time
   or o.date_voided >=  last_update_time
group by e.patient_id, e.encounter_id
    ON DUPLICATE KEY UPDATE
                         visit_date=VALUES(visit_date),
                         date_created=VALUES(date_created),
                         visit_date=VALUES(visit_date),
                         date_created=VALUES(date_created),
                         date_last_modified=VALUES(date_last_modified),
                         place_of_residence_country=VALUES(place_of_residence_country),
                         nationality=VALUES(nationality),
                         target_population=VALUES(target_population),
                         traveled_last_3_months=VALUES(traveled_last_3_months),
                         traveled_last_6_months=VALUES(traveled_last_6_months),
                         traveled_last_12_months=VALUES(traveled_last_12_months),
                         duration_of_stay=VALUES(duration_of_stay),
                         frequency_of_travel=VALUES(frequency_of_travel),
                         type_of_service=VALUES(type_of_service),
                         creator=VALUES(creator);

END $$

-- Update crossBorder referral procedure ----

DROP PROCEDURE IF EXISTS sp_update_etl_crossborder_referral $$
CREATE PROCEDURE sp_update_etl_crossborder_referral(IN last_update_time DATETIME)

BEGIN

Insert INTO kenyaemr_etl.etl_crossborder_referral
(
    patient_id,
    encounter_id,
    visit_id,
    location_id,
    visit_date,
    nationality,
    referring_facility_name,
    referred_facility_name,
    type_of_care,
    date_of_referral,
    reason_for_referral,
    target_population,
    general_comments_if_reffered,
    referral_recommendation_continue_art,
    referring_hc_provider,
    referring_hc_provider_email,
    referring_hc_provider_telephone,
    referring_hc_provider_cadre,
    date_last_modified,
    creator,
    date_created,
    voided
)
select
    e.patient_id,
    e.encounter_id,
    e.visit_id,
    e.location_id,
    e.encounter_datetime as visit_date,
    max(if(o.concept_id=165915,o.value_coded,null)) as nationality,
    max(if(o.concept_id=161550,o.value_text,null)) as referring_facility_name,
    max(if(o.concept_id=162724,o.value_text,null)) as referred_facility_name,
    max(if(o.concept_id=168146,o.value_coded,null)) as type_of_care,
    max(if(o.concept_id=163181,o.value_datetime,null)) as date_of_referral,
    max(if(o.concept_id=1887,o.value_coded,null)) as reason_for_referral,
    max(if(o.concept_id=166433,o.value_coded,null)) as target_population,
    max(if(o.concept_id=161011,o.value_text,null)) as general_comments_if_reffered,
    max(if(o.concept_id=160632,o.value_text,null)) as referral_recommendation_continue_art,
    max(if(o.concept_id=161103,o.value_text,null)) as referring_hc_provider,
    max(if(o.concept_id=168130,o.value_text,null)) as referring_hc_provider_email,
    max(if(o.concept_id=163152,o.value_text,null)) as referring_hc_provider_telephone,
    max(if(o.concept_id=163556,o.value_text,null)) as referring_hc_provider_cadre,
    if(max(o.date_created) > min(e.date_created),max(o.date_created),NULL) as date_last_modified,
    e.creator,
    e.date_created,
    e.voided
from encounter e
         inner join
     (
         select encounter_type_id, uuid, name from encounter_type where uuid='5C6DA02B-51E8-4B3D-BB67-BE8F75C4CCE1'
     ) et on et.encounter_type_id=e.encounter_type
         inner join person p on p.person_id=e.patient_id and p.voided=0
         left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0
    and o.concept_id in (161550,162724,168146,163181,1887,161011,160632,161103,168130,163152,163556,165915,166433)
where e.voided=0 and e.date_created >= last_update_time
   or e.date_changed >=  last_update_time
   or e.date_voided >=  last_update_time
   or o.date_created >=  last_update_time
   or o.date_voided >=  last_update_time
group by e.patient_id, e.encounter_id
    ON DUPLICATE KEY UPDATE
                         visit_date=VALUES(visit_date),
                         nationality=VALUES(nationality),
                         referring_facility_name=VALUES(referring_facility_name),
                         referred_facility_name=VALUES(referred_facility_name),
                         type_of_care=VALUES(type_of_care),
                         date_of_referral=VALUES(date_of_referral),
                         reason_for_referral=VALUES(reason_for_referral),
                         target_population=VALUES(target_population),
                         general_comments_if_reffered=VALUES(general_comments_if_reffered),
                         referral_recommendation_continue_art=VALUES(referral_recommendation_continue_art),
                         referring_hc_provider=VALUES(referring_hc_provider),
                         referring_hc_provider_email=VALUES(referring_hc_provider_email),
                         referring_hc_provider_telephone=VALUES(referring_hc_provider_telephone),
                         referring_hc_provider_cadre=VALUES(referring_hc_provider_cadre),
                         date_last_modified=VALUES(date_last_modified),
                         creator=VALUES(creator),
                         voided=VALUES(voided),
                         creator=VALUES(creator);
END $$

-- ----------------------------  scheduled updates ---------------------

DROP PROCEDURE IF EXISTS cb_sp_scheduled_updates $$
CREATE PROCEDURE cb_sp_scheduled_updates()
BEGIN
    DECLARE update_script_id INT(11);
    DECLARE last_update_time DATETIME;
SELECT max(start_time) into last_update_time from kenyaemr_etl.etl_script_status where stop_time is not null or stop_time !="";

INSERT INTO kenyaemr_etl.etl_script_status(script_name, start_time) VALUES('scheduled_updates', NOW());
SET update_script_id = LAST_INSERT_ID();

CALL sp_update_etl_crossborder_referral(last_update_time);
CALL sp_update_etl_crossborder_screening(last_update_time);


UPDATE kenyaemr_etl.etl_script_status SET stop_time=NOW() where  id= update_script_id;
DELETE FROM kenyaemr_etl.etl_script_status where script_name in ("KenyaEMR_Data_Tool", "scheduled_updates") and start_time < DATE_SUB(NOW(), INTERVAL 12 HOUR);
SELECT update_script_id;

END $$

