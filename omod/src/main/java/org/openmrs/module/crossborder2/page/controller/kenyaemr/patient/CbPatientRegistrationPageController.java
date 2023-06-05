package org.openmrs.module.crossborder2.page.controller.kenyaemr.patient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.context.SessionContext;
import org.openmrs.module.crossborder2.CbConstants;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class CbPatientRegistrationPageController {
	
	public void controller(@RequestParam(value = "personId", required = false) Patient patient,
	        @RequestParam(value = "crossBorderId", required = false) String crossBorderId,
	        @SpringBean CbPatientService cbPatientService, SessionContext sessionContext, PageModel model) {
		
		if (patient == null) {
			if (StringUtils.isNotBlank(crossBorderId)) {
				List<Patient> patients = Context.getPatientService().getPatients(crossBorderId);
				if (!patients.isEmpty()) {
					patient = patients.get(0);
				} else {
					patient = cbPatientService.findPatient(crossBorderId);
					patient.setUuid(UUID.randomUUID().toString());
					final Set<PatientIdentifier> identifiers = new HashSet<>();
					boolean hasOpenmrsId = false;
					Location defaultLocation = Context.getService(KenyaEmrService.class).getDefaultLocation();
					for (PatientIdentifier patientIdentifier : patient.getIdentifiers()) {
						if (patientIdentifier.getIdentifierType().getUuid().equals(
								CbConstants.CROSSBORDER_ID_IDENTIFIER_TYPE_UUID)) {
							patientIdentifier.setPreferred(true);
						}
						if (patientIdentifier.getIdentifierType().getUuid().equals(
								CbConstants.OPENMRS_ID_IDENTIFIER_TYPE_UUID)) {
							hasOpenmrsId = true;
						}
						patientIdentifier.setLocation(defaultLocation);
						identifiers.add(patientIdentifier);
					}
					
					if (!hasOpenmrsId) {
						PatientIdentifier openmrsIdentifier = new PatientIdentifier();
						PatientIdentifierType openmrsIdType = Context.getPatientService()
								.getPatientIdentifierTypeByUuid(CbConstants.OPENMRS_ID_IDENTIFIER_TYPE_UUID);
						String generated = Context.getService(IdentifierSourceService.class).generateIdentifier(openmrsIdType,
								"Registration");
						openmrsIdentifier.setIdentifierType(openmrsIdType);
						openmrsIdentifier.setIdentifier(generated);
						openmrsIdentifier.setLocation(defaultLocation);
						
						identifiers.add(openmrsIdentifier);
					}
					patient.setIdentifiers(identifiers);
					// patient = Context.getPatientService().savePatient(patient);
				}
			}
		}
		model.addAttribute("person", patient);
		
	}
}
