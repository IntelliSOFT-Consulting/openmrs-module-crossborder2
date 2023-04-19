/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.crossborder2.openhim.converter;

import org.hl7.fhir.r4.model.Patient;
import org.openmrs.module.crossborder2.api.exceptions.ResourceGenerationException;
import org.openmrs.module.fhir2.api.translators.PatientTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CbPatientConverter {
	
	@Autowired
	PatientTranslator patientTranslator;
	
	public org.openmrs.Patient convertToOpenMrsPatient(Patient fhirPatient) {
		org.openmrs.Patient openmrsPatient = patientTranslator.toOpenmrsType(fhirPatient);
		return openmrsPatient;
	}
	
	public Patient convertToFhirPatient(org.openmrs.Patient openMrsPatient) throws ResourceGenerationException {
		Patient fhirPatient = patientTranslator.toFhirResource(openMrsPatient);
		return fhirPatient;
	}
}
