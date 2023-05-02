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

import org.hl7.fhir.r4.model.Encounter;
import org.openmrs.module.fhir2.api.translators.EncounterTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CbEncounterConverter {
	
	@Autowired
	EncounterTranslator<org.openmrs.Encounter> encounterTranslator;
	
	public org.openmrs.Encounter convertToOpenMrsEncounter(Encounter fhirEncounter) {
		org.openmrs.Encounter openmrsEncounter = encounterTranslator.toOpenmrsType(fhirEncounter);
		return openmrsEncounter;
	}
	
	public Encounter convertToFhirEncounter(org.openmrs.Encounter openMrsEncounter) {
		Encounter fhirEncounter = encounterTranslator.toFhirResource(openMrsEncounter);
		return fhirEncounter;
	}
}
