package org.openmrs.module.crossborder2.api;

import org.hl7.fhir.r4.model.Resource;
import org.openmrs.module.crossborder2.api.exceptions.ResourceGenerationException;
import org.springframework.stereotype.Component;

@Component
public interface FhirResourceBuilder {
	
	public Resource generateFhirResource(Object openmrsEntity) throws ResourceGenerationException;
}
