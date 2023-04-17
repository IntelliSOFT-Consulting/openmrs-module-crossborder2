package org.openmrs.module.crossborder2.api.impl;

import org.hl7.fhir.r4.model.Resource;
import org.openmrs.Patient;
import org.openmrs.module.crossborder2.api.FhirResourceBuilder;
import org.openmrs.module.crossborder2.api.exceptions.ResourceGenerationException;
import org.openmrs.module.fhir2.api.translators.PatientTranslator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("fhirResourceBuilder")
public class FhirResourceBuilderImpl implements FhirResourceBuilder, ApplicationContextAware {
	
	@Autowired
	private PatientTranslator patientTranslator;
	
	private ApplicationContext applicationContext;
	
	public Resource generateFhirResource(Object openMrsEntity) throws ResourceGenerationException {
		Resource resource = null;
		String localPatientId = "";
		
		if (openMrsEntity instanceof Patient) {
			Patient patient = (Patient) openMrsEntity;
			resource = patientTranslator.toFhirResource(patient);
			
		} else {
			// log.error(String.format("Entity %s not yet implemented", openMrsEntity.getClass().getName()));
			throw new ResourceGenerationException("Entity not implemented");
		}
		
		return resource;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
