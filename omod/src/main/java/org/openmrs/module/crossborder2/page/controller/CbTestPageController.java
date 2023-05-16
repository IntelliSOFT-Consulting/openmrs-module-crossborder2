package org.openmrs.module.crossborder2.page.controller;

import java.util.Date;
import java.util.UUID;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.api.EncounterService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.crossborder2.openhim.CbEncounterService;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class CbTestPageController {
	
	public void controller(PageModel model, @SpringBean CbPatientService cbPatientService,
	        @SpringBean CbEncounterService cbEncounterService) {
		Patient patient = createDummyPatient();
		//		List<Patient> patientList = cbPatientService.searchPatient("Gloria");
		//		Patient foundPatient = cbPatientService.findPatient("KE-2023-02-7B732");
		//		Patient createdPatient = cbPatientService.createPatient(patient);
		//		Patient updatedPatient = cbPatientService.updatePatient(patient, "KE-2023-02-7B732");
		
		Encounter encounter = createDummyEncounter(patient);
		//Encounter createdEncounter = cbEncounterService.createEncounter(encounter, "KE-2023-02-7B732");
		Encounter updatedEncounter = cbEncounterService.updateEncounter(encounter, "KE-2023-02-7B732");
		model.addAttribute("message", "This is the CB Test Page");
	}
	
	private Patient createDummyPatient() {
		Patient patient = new Patient();
		patient.setUuid(UUID.randomUUID().toString());
		patient.setGender("F");
		patient.setBirthdate(new Date());
		patient.setBirthdateEstimated(false);
		patient.setDead(false);
		patient.setVoided(false);
		patient.setDeathDate(null);
		PersonName personName = new PersonName();
		personName.setGivenName("Jane");
		personName.setMiddleName("K");
		personName.setFamilyName("Wanjiku");
		personName.setUuid(UUID.randomUUID().toString());
		patient.addName(personName);
		PersonAddress personAddress = new PersonAddress();
		personAddress.setAddress1("1234 Main St");
		personAddress.setCityVillage("Nairobi");
		personAddress.setStateProvince("NBI");
		personAddress.setCountry("Kenya");
		personAddress.setPostalCode("12345");
		personAddress.setUuid(UUID.randomUUID().toString());
		patient.addAddress(personAddress);
		{
			PatientIdentifier patientIdentifier = new PatientIdentifier();
			patientIdentifier.setPatient(patient);
			patientIdentifier.setIdentifier("12345-00001");
			PatientIdentifierType idType = MetadataUtils.existing(PatientIdentifierType.class,
			    HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
			patientIdentifier.setIdentifierType(idType);
			patient.addIdentifier(patientIdentifier);
		}
		return patient;
	}
	
	private Encounter createDummyEncounter(Patient patient) {
		EncounterService encounterService = Context.getEncounterService();
		LocationService locationService = Context.getLocationService();
		Encounter encounter = new Encounter();
		encounter.setUuid(UUID.randomUUID().toString());
		encounter.setPatient(patient);
		EncounterType encounterType = encounterService
		        .getEncounterTypeByUuid(CommonMetadata._EncounterType.DRUG_REGIMEN_EDITOR);
		encounter.setEncounterType(encounterType);
		Location location = locationService.getLocation(1);
		encounter.setLocation(location);
		Obs obs = new Obs();
		obs.setConcept(Context.getConceptService().getConcept(5000023));
		obs.setValueNumeric(42.0);
		encounter.addObs(obs);
		return encounter;
	}
}
