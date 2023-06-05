package org.openmrs.module.crossborder2.page.controller.kenyaemr.patient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.openmrs.ConceptReferenceTerm;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.context.SessionContext;
import org.openmrs.module.crossborder2.CbConstants;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class CbPatientRegistrationPageController {
	
	public void controller(@RequestParam(value = "personId", required = false) Patient patient,
	        @RequestParam(value = "crossBorderId", required = false) String crossBorderId,
	        @SpringBean CbPatientService cbPatientService, SessionContext sessionContext, PageModel model) {
		
		if (patient == null) {
			if (crossBorderId != null) {
				List<Patient> patients = Context.getPatientService().getPatients(crossBorderId);
				if (!patients.isEmpty()) {
					patient = patients.get(0);
				} else {
					patient = cbPatientService.findPatient(crossBorderId);
					patient.setUuid(UUID.randomUUID().toString());
					final Set<PatientIdentifier> identifiers = new HashSet<>();
					for (PatientIdentifier patientIdentifier : patient.getIdentifiers()) {
						if (patientIdentifier.getIdentifierType().getUuid().equals(
								CbConstants.CROSSBORDER_ID_IDENTIFIER_TYPE_UUID)) {
							patientIdentifier.setPreferred(true);
						}
						patientIdentifier.setLocation(
								Context.getLocationService().getLocation(sessionContext.getSessionLocationId()));
						identifiers.add(patientIdentifier);
					}
					patient.setIdentifiers(identifiers);
					patient = Context.getPatientService().savePatient(patient);
				}
			}
		}
		model.addAttribute("person", patient);
		
	}
}
