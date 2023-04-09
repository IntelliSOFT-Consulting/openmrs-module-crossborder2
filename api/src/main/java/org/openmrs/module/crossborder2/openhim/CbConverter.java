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

import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import org.openmrs.module.fhir2.api.translators.impl.PatientTranslatorImpl;

public class CbConverter {

	private final PatientTranslatorImpl translator = new PatientTranslatorImpl();

	public org.openmrs.Patient convertToOpenMrsPatient(Patient fhirPatient) {
		org.openmrs.Patient openmrsPatient = translator.toOpenmrsType(fhirPatient);
		return openmrsPatient;
	}

	public Patient convertToFhirPatient(org.openmrs.Patient openMrsPatient) {
		//		Patient fhirPatient = translator.toFhirResource(openMrsPatient);
		Patient patient = new Patient();
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		HumanName humanName = new HumanName();
		humanName.addGiven("John");
		humanName.setFamily("Doe");
		humanName.addPrefix("Mr");
		patient.addName(humanName);
		return patient;//fhirPatient;
	}
}
