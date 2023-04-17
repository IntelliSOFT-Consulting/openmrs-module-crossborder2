/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.crossborder2.openhim;

import org.hl7.fhir.r4.model.Patient;
import org.openmrs.module.crossborder2.api.exceptions.ResourceGenerationException;
import org.openmrs.module.crossborder2.api.impl.FhirResourceBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CbConverter {
	
	@Autowired
	FhirResourceBuilderImpl fhirResourceBuilder;
	
	public org.openmrs.Patient convertToOpenMrsPatient(Patient fhirPatient) {
		// org.openmrs.Patient openmrsPatient = fhirResourceBuilder.convertToOpenMRS(fhirPatient);
		//return openmrsPatient;
		return null;
	}
	
	public Patient convertToFhirPatient(org.openmrs.Patient openMrsPatient) throws ResourceGenerationException {
		Patient fhirPatient = (Patient) new FhirResourceBuilderImpl().generateFhirResource(openMrsPatient);
		return fhirPatient;
	}
}
