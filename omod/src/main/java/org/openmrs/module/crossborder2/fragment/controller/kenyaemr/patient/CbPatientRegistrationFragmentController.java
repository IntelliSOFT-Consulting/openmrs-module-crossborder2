package org.openmrs.module.crossborder2.fragment.controller.kenyaemr.patient;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.springframework.web.bind.annotation.RequestParam;

public class CbPatientRegistrationFragmentController {
	
	public Patient createPatient(@RequestParam(value = "patient") Patient patient,
	        @RequestParam(value = "properties", required = false) String[] properties,
	        @SpringBean CbPatientService cbPatientService, UiUtils ui) {
		
		Patient savedPatientResponse = cbPatientService.createPatient(patient);
		Context.getPatientService().savePatient(savedPatientResponse);
		return savedPatientResponse;
	}
}
