/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.crossborder2.openhim;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.openmrs.PersonName;

import java.util.List;

public class CbConverter {
	
	public org.openmrs.Patient convertToOpenMrsPatient(Patient fhirPatient) {
		org.openmrs.Patient openmrsPatient = new org.openmrs.Patient();
		
		// Convert Patient name
		List<HumanName> names = fhirPatient.getName();
		if (!names.isEmpty()) {
			HumanName fhirName = names.get(0);
			PersonName openmrsName = new PersonName();
			if (!fhirName.getGiven().isEmpty()) {
				openmrsName.setGivenName(fhirName.getGiven().get(0).getValue());
			}
			if (fhirName.getFamily() != null) {
				openmrsName.setFamilyName(fhirName.getFamily());
			}
			openmrsPatient.addName(openmrsName);
		}
		
		// Convert Patient gender
		if (fhirPatient.getGender() != null) {
			switch (fhirPatient.getGender()) {
				case MALE:
					openmrsPatient.setGender("M");
					break;
				case FEMALE:
					openmrsPatient.setGender("F");
					break;
				default:
					openmrsPatient.setGender("U");
					break;
			}
		}
		
		// Add other attributes (e.g., birthdate, address, etc.) here as needed.
		
		return openmrsPatient;
	}
}
