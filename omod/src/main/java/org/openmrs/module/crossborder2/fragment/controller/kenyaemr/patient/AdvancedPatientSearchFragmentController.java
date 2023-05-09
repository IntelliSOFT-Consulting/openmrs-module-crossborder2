package org.openmrs.module.crossborder2.fragment.controller.kenyaemr.patient;

import org.openmrs.Patient;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class AdvancedPatientSearchFragmentController {
	
	public void controller(FragmentModel model) {
	}
	
	public List<SimpleObject> searchPatients(@RequestParam(value = "searchTerm") String searchTerm,
	        @RequestParam(value = "properties", required = false) String[] properties,
	        @SpringBean CbPatientService cbPatientService, UiUtils ui) {
		List<SimpleObject> results = new ArrayList<SimpleObject>();
		if (searchTerm != null && !searchTerm.isEmpty()) {
			List<Patient> patients = cbPatientService.searchPatient(searchTerm);
			results = SimpleObject.fromCollection(patients, ui, properties);
		}
		return results;
	}
	
	public SimpleObject findPatient(@RequestParam(value = "crossBorderId") String crossBorderId,
	        @RequestParam(value = "properties", required = false) String[] properties,
	        @SpringBean CbPatientService cbPatientService, UiUtils ui) {
		SimpleObject result = new SimpleObject();
		if (crossBorderId != null && !crossBorderId.isEmpty()) {
			Patient patient = cbPatientService.findPatient(crossBorderId);
			result = SimpleObject.fromObject(patient, ui, properties);
		}
		return result;
	}
}
