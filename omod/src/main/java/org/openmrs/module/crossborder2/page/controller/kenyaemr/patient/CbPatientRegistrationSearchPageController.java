package org.openmrs.module.crossborder2.page.controller.kenyaemr.patient;

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class CbPatientRegistrationSearchPageController {
	
	public void controller(PageModel model, @SpringBean CbPatientService cbPatientService) {
		AppDescriptor currentApp = Context.getService(AppFrameworkService.class).getApp("crossborder2.cb");
		model.addAttribute("message", "This is the CB Home Page");
		model.addAttribute("currentApp", currentApp);
		
	}
}
