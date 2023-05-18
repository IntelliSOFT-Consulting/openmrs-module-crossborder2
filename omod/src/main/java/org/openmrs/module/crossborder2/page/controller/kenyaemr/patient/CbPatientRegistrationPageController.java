package org.openmrs.module.crossborder2.page.controller.kenyaemr.patient;

import org.openmrs.Person;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class CbPatientRegistrationPageController {
	
	public void controller(@RequestParam(value = "personId", required = false) Person person, PageModel model) {
		
		model.addAttribute("person", person);
	}
}
