package org.openmrs.module.crossborder2.page.controller.kenyaemr.patient;

import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class CbPatientRegistrationSearchPageController {
	
	public void controller(PageModel model, @SpringBean CbPatientService cbPatientService) {
		model.addAttribute("message", "This is the CB Home Page");
	}
}
