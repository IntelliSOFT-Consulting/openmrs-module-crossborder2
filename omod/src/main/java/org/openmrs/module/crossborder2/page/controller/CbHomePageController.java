package org.openmrs.module.crossborder2.page.controller;

import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CbHomePageController {
	
	public void controller(PageModel model, @SpringBean CbPatientService cbPatientService) {
		Patient patient = createDummyPatient();
		List<Patient> patientList = cbPatientService.searchPatient("Gloria");
		Patient foundPatient = cbPatientService.findPatient("KE-2023-02-7B732");
		Patient createdPatient = cbPatientService.createPatient(patient);
		Patient updatedPatient = cbPatientService.updatePatient(patient, "KE-2023-02-7B732");
		model.addAttribute("message", "This is the CB Home Page");
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
}
