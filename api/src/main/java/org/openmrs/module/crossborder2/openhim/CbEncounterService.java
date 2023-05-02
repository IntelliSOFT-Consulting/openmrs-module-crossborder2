/**
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under the terms
 * of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.crossborder2.openhim;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.openmrs.Encounter;
import org.openmrs.module.crossborder2.openhim.converter.CbEncounterConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CbEncounterService {
	
	@Autowired
	private CbEncounterConverter cbEncounterConverter;
	
	/**
	 * Creates an encounter in the MPI.
	 * 
	 * @param openMrsEncounter the encounter to create.
	 * @param crossBorderId the cross-border ID of the patient whose encounter to create.
	 * @return the newly created encounter.
	 */
	public Encounter createEncounter(Encounter openMrsEncounter, String crossBorderId) {
		org.hl7.fhir.r4.model.Encounter fhirEncounter = cbEncounterConverter.convertToFhirEncounter(openMrsEncounter);
		
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.BATCH);
		bundle.addEntry().setResource(fhirEncounter).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Encounter");
		FhirContext ctx = FhirContext.forR4();
		String bundleJson = ctx.newJsonParser().encodeResourceToString(bundle);
		
		String payload = bundleJson;//serializeEncounter(fhirEncounter);
		String jsonResponse = new Http().post("shr", payload, "crossBorderId=" + crossBorderId);
		Encounter encounter = deserializeEncounter(jsonResponse);
		return encounter;
	}
	
	/**
	 * Updates an encounter in the MPI.
	 * 
	 * @param openMrsEncounter the encounter to update.
	 * @param crossBorderId the cross-border ID of the patient whose encounter to update.
	 * @return the updated encounter.
	 */
	public Encounter updateEncounter(Encounter openMrsEncounter, String crossBorderId) {
		org.hl7.fhir.r4.model.Encounter fhirEncounter = cbEncounterConverter.convertToFhirEncounter(openMrsEncounter);
		String payload = serializeEncounter(fhirEncounter);
		String jsonResponse = new Http().put("shr", payload, "crossBorderId=" + crossBorderId);
		Encounter encounter = deserializeEncounter(jsonResponse);
		return encounter;
	}
	
	public Encounter deserializeEncounter(String jsonResponse) {
		IParser parser = FhirContext.forR4().newJsonParser();
		org.hl7.fhir.r4.model.Encounter encounter = parser
		        .parseResource(org.hl7.fhir.r4.model.Encounter.class, jsonResponse);
		return cbEncounterConverter.convertToOpenMrsEncounter(encounter);
	}
	
	private String serializeEncounter(org.hl7.fhir.r4.model.Encounter patient) {
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		return parser.encodeResourceToString(patient);
	}
}
