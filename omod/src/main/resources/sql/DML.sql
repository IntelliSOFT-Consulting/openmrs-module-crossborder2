-- Populating etl_crossborder_screening table
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
    max(if(o.concept_id=5000010,o.value_coded,null)) as place_of_residence_country,
    max(if(o.concept_id=5000023,o.value_coded,null)) as nationality,
    max(if(o.concept_id=5000022,o.value_coded,null)) as target_population,
    max(if(o.concept_id=5000013,o.value_coded,null)) as traveled_last_3_months,
    max(if(o.concept_id=5000014,o.value_coded,null)) as traveled_last_6_months,
    max(if(o.concept_id=5000015,o.value_coded,null)) as traveled_last_12_months,
    max(if(o.concept_id=5000016,o.value_numeric,null)) as duration_of_stay,
    max(if(o.concept_id=5000018,o.value_coded,null)) as frequency_of_travel,
    max(if(o.concept_id=5000030,o.value_coded,null)) as type_of_service
from encounter e
         inner join
     (
         select encounter_type_id, uuid, name from encounter_type where uuid='6536A8A3-7B77-414D-A0F0-E08A7178FF0F'
     ) et on et.encounter_type_id=e.encounter_type
         inner join person p on p.person_id=e.patient_id and p.voided=0
         left outer join obs o on o.encounter_id=e.encounter_id and o.voided=0
    and o.concept_id in (5000010,5000023,5000022,5000013,5000014,5000015,5000016,5000018,5000030)
where e.voided=0
group by e.patient_id, e.encounter_id;

-- Populating etl_crossborder_referral table

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
    max(if(o.concept_id=5000023,o.value_coded,null)) as nationality,
    max(if(o.concept_id=161550,o.value_text,null)) as referring_facility_name,
    max(if(o.concept_id=162724,o.value_text,null)) as referred_facility_name,
    max(if(o.concept_id=5000030,o.value_coded,null)) as type_of_care,
    max(if(o.concept_id=163181,o.value_datetime,null)) as date_of_referral,
    max(if(o.concept_id=1887,o.value_coded,null)) as reason_for_referral,
    max(if(o.concept_id=5000022,o.value_coded,null)) as target_population,
    max(if(o.concept_id=161011,o.value_text,null)) as general_comments_if_reffered,
    max(if(o.concept_id=5000043,o.value_text,null)) as referral_recommendation_continue_art,
    max(if(o.concept_id=5000044,o.value_text,null)) as referring_hc_provider,
    max(if(o.concept_id=5000045,o.value_text,null)) as referring_hc_provider_email,
    max(if(o.concept_id=5000046,o.value_text,null)) as referring_hc_provider_telephone,
    max(if(o.concept_id=5000047,o.value_text,null)) as referring_hc_provider_cadre,
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
    and o.concept_id in (161550,162724,5000030,163181,1887,161011,5000043,5000044,5000045,5000046,5000047,5000023,5000022)
where e.voided=0
group by e.patient_id, e.encounter_id;


