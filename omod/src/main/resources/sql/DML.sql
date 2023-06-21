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
