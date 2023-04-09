package org.openmrs.module.crossborder2.page.controller;

import org.openmrs.Patient;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.ui.framework.page.PageModel;

import java.util.List;

public class CbHomePageController {
	
	public void controller(PageModel model) {
		CbPatientService cbPatientService = new CbPatientService();
		List<Patient> patientList = cbPatientService.searchPatient("Gloria");
		Patient patient = cbPatientService.findPatient("KE-2023-02-7B732");
		model.addAttribute("message", "This is the CB Home Page");
	}
}
