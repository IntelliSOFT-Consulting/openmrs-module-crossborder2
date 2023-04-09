package org.openmrs.module.crossborder2.page.controller;

import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Date;
import java.util.List;

public class CbHomePageController {

	public void controller(PageModel model) {
		CbPatientService cbPatientService = new CbPatientService();
		List<Patient> patientList = cbPatientService.searchPatient("Gloria");
		Patient foundPatient = cbPatientService.findPatient("KE-2023-02-7B732");
		String crossBorderId = cbPatientService.createPatient(createDummyPatient());
		Patient updatedPatient = cbPatientService.updatePatient(createDummyPatient(), "KE-2023-02-7B732");
		model.addAttribute("message", "This is the CB Home Page");
	}

	private Patient createDummyPatient() {
		Patient patient = new Patient();
		{
			PersonName personName = new PersonName();
			personName.setGivenName("Gloria");
			personName.setFamilyName("Mwende");
			personName.setPreferred(true);
			patient.addName(new PersonName());
		}
		{
			PatientIdentifier patientIdentifier = new PatientIdentifier();
			patientIdentifier.setPatient(patient);
			patientIdentifier.setIdentifier("12345-00001");
			PatientIdentifierType idType = MetadataUtils.existing(PatientIdentifierType.class,
				HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
			patientIdentifier.setIdentifierType(idType);
			patient.addIdentifier(patientIdentifier);
		}
		patient.setGender("F");
		patient.setBirthdate(new Date());
		patient.setBirthdateEstimated(false);
		patient.setVoided(false);
		patient.setUuid("77b4b43a-d6c4-11ed-8cd3-4753b3dc5409");
		patient.setPatientId(100000);
		patient.setAddresses(null);
		return patient;
	}
}
