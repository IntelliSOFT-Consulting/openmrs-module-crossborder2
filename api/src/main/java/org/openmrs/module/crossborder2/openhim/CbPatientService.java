/**
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under the terms
 * of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.crossborder2.openhim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.openmrs.Patient;
import org.openmrs.module.crossborder2.api.exceptions.ResourceGenerationException;
import org.openmrs.module.crossborder2.openhim.converter.CbPatientConverter;
import org.openmrs.module.crossborder2.utils.FhirUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CbPatientService {
	
	public static final String CROSS_BORDER_ID_SYSTEM_URN = "urn:oid:2.16.840.1.113883.3.26.1.3";
	
	public static final String CLINIC_NUMBER_SYSTEM_URN = "urn:oid:2.16.840.1.113883.3.26.1.2";
	
	public static final String NATIONAL_ID_SYSTEM_URN = "urn:oid:2.16.840.1.113883.3.26.1.1";
	
	@Autowired
	private CbPatientConverter cbPatientConverter;
	
	private FhirUtil fhirUtil = FhirUtil.getInstance();
	
	/**
	 * Searches the MPI for patients that match a given search term.
	 * 
	 * @param searchTerm the search term by which to search for patients.
	 * @return the list of patients that match the search term.
	 */
	public List<Patient> searchPatient(String searchTerm) {
		String jsonResponse = new Http().get("search", "name=" + searchTerm);
		List<Patient> openMrsPatients = deserializePatients(jsonResponse);
		return openMrsPatients;
	}
	
	/**
	 * Retrieves a patient from the MPI using the patient's cross-border ID.
	 * 
	 * @param crossBorderId the patient's cross-border ID.
	 * @return the patient with the given cross-border ID.
	 */
	public Patient findPatient(String crossBorderId) {
		Patient openMrsPatient = null;
		try {
			String jsonResponse = new Http().get("Patient", "id=" + crossBorderId);
			openMrsPatient = deserializePatient(jsonResponse);
		}
		catch (Exception ex) {
			
		}
		
		return openMrsPatient;
	}
	
	/**
	 * Creates a patient in the MPI.
	 * 
	 * @param openMrsPatient the patient to create.
	 * @return the newly created patient patient.
	 */
	public Patient createPatient(Patient openMrsPatient) {
		org.hl7.fhir.r4.model.Patient fhirPatient = null;
		try {
			fhirPatient = cbPatientConverter.convertToFhirPatient(openMrsPatient);
		}
		catch (ResourceGenerationException e) {
			throw new RuntimeException(e);
		}
		String payload = serializePatient(fhirPatient);
		//		payload = "{\"resourceType\":\"Patient\",\"identifier\":[{\"id\":\"POINT_OF_CARE_ID\",\"value\":\"BUSIA-RN-00\"},{\"id\":\"NATIONAL_ID\",\"system\":\"Kenya\",\"value\":\"098900\"}],\"name\":[{\"family\":\"Gloria\",\"given\":[\"West\"]}],\"telecom\":[{\"value\":\"0719999090\"}],\"gender\":\"female\",\"birthDate\":\"1997-05-05\",\"address\":[{\"city\":\"Langata\",\"state\":\"Nairobi\",\"country\":\"Kenya\"}],\"maritalStatus\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-MaritalStatus\",\"code\":\"MARRIED POLYGAMOUS\",\"display\":\"Married Polygamous\"}],\"text\":\"Married Polygamous\"},\"contact\":[{\"relationship\":[{\"text\":\"Spouse\"}],\"name\":{\"family\":\"Edith Her\"},\"telecom\":[{\"value\":\"0712345678\"}]}]}";
		String jsonResponse = new Http().post("Patient", payload);
		Patient patient = deserializePatient(jsonResponse);
		return patient;
	}
	
	/**
	 * Updates a patient in the MPI.
	 * 
	 * @param openMrsPatient the patient to update.
	 * @param crossBorderId the cross-border ID of the patient to update.
	 * @return the updated patient.
	 */
	public Patient updatePatient(Patient openMrsPatient, String crossBorderId) {
		org.hl7.fhir.r4.model.Patient fhirPatient = null;
		try {
			fhirPatient = cbPatientConverter.convertToFhirPatient(openMrsPatient);
		}
		catch (ResourceGenerationException e) {
			throw new RuntimeException(e);
		}
		String payload = serializePatient(fhirPatient);
		//		payload = "{\"resourceType\":\"Patient\",\"id\":\"302\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2023-02-02T08:43:09.977+00:00\",\"source\":\"#5ZDxH1E3TJbr1zsk\"},\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><div class=\\\"hapiHeaderText\\\">West <b>GLORIA </b></div><table class=\\\"hapiPropertyTable\\\"><tbody><tr><td>Identifier</td><td>BUSIA-RN-00</td></tr><tr><td>Address</td><td><span>Langata </span><span>Nairobi </span><span>Kenya </span></td></tr><tr><td>Date of birth</td><td><span>05 May 1997</span></td></tr></tbody></table></div>\"},\"identifier\":[{\"id\":\"POINT_OF_CARE_ID\",\"value\":\"BUSIA-RN-00\"},{\"id\":\"CROSS_BORDER_ID\",\"value\":\"KE-2023-02-7B732\"},{\"id\":\"NATIONAL_ID\",\"system\":\"Kenya\",\"value\":\"098900\"}],\"name\":[{\"family\":\"Gloria\",\"given\":[\"Anna\"]}],\"telecom\":[{\"value\":\"0719999090\"}],\"gender\":\"female\",\"birthDate\":\"1997-05-05\",\"address\":[{\"city\":\"Langata\",\"state\":\"Nairobi\",\"country\":\"Kenya\"}],\"maritalStatus\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-MaritalStatus\",\"code\":\"MARRIED POLYGAMOUS\",\"display\":\"Married Polygamous\"}],\"text\":\"Married Polygamous\"},\"contact\":[{\"relationship\":[{\"text\":\"Edith Her\"}],\"name\":{\"family\":\"Spouse\"}}]}\n";
		String jsonResponse = new Http().put("Patient", payload, "crossBorderId=" + crossBorderId);
		openMrsPatient = deserializePatient(jsonResponse);
		return openMrsPatient;
	}
	
	private List<Patient> deserializePatients(String jsonResponse) {
		List<Patient> patients = new ArrayList<Patient>();
		FhirContext fhirContext = FhirContext.forR4();
		Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, jsonResponse);
		for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
			Resource resource = entry.getResource();
			Patient p = cbPatientConverter.convertToOpenMrsPatient((org.hl7.fhir.r4.model.Patient) resource);
			patients.add(p);
		}
		return patients;
	}
	
	public Patient deserializePatient(String jsonResponse) {
		IParser parser = FhirContext.forR4().newJsonParser();
		org.hl7.fhir.r4.model.Patient patient = parser.parseResource(org.hl7.fhir.r4.model.Patient.class, jsonResponse);
		return cbPatientConverter.convertToOpenMrsPatient(patient);
	}
	
	private String serializePatient(org.hl7.fhir.r4.model.Patient patient) {
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		return parser.encodeResourceToString(patient);
	}
	
	public List<Patient> searchPatient(HashMap<String, String> searchParams) {
		// TODO: Breakdown the search params
		String query = "";
		if (searchParams.containsKey("cbId")) {
			query += "identifier=" + CROSS_BORDER_ID_SYSTEM_URN + "|" + searchParams.get("cbId");
		}
		query += query.equals("") ? "" : "&";
		if (searchParams.containsKey("clinicNo")) {
			query += "identifier=" + CLINIC_NUMBER_SYSTEM_URN + "|" + searchParams.get("clinicNo");
		}
		query += query.equals("") ? "" : "&";
		if (searchParams.containsKey("nationalId")) {
			query += "identifier=" + NATIONAL_ID_SYSTEM_URN + "|" + searchParams.get("clinicNo");
		}
		query += query.equals("") ? "" : "&";
		if (searchParams.containsKey("name")) {
			query += "phonetic" + searchParams.get("name");
		}
		query += query.equals("") ? "" : "&";
		if (searchParams.containsKey("gender")) {
			query += "gender" + searchParams.get("gender");
		}
		
/*
		IGenericClient client = FhirUtil.getInstance().getClient();
		Bundle bundle = (Bundle) client.search().forResource(org.hl7.fhir.r4.model.Patient.class)
				.where(new TokenClientParam("identifier").exactly().systemAndCode("oid:2.16.840.1.113883.3.26.1.3", "cbid"))
				.where(new TokenClientParam("identifier").exactly().systemAndCode("urn:oid:2.16.840.1.113883.3.26.1.2", "clinincno"))
				.where(new TokenClientParam("identifier").exactly().systemAndCode("urn:oid:2.16.840.1.113883.3.26.1.1", "natid"))
				.where(new TokenClientParam("gender").exactly().code("male"))
				.execute();
*/
		//TODO: Add logic for clinic number etc
		String jsonResponse = new Http().get("search", query);
		List<Patient> openMrsPatients = deserializePatients(jsonResponse);
		return openMrsPatients;
	}
}
