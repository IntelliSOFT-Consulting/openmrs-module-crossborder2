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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CbPatientService {

	/**
	 * Searches the MPI for patients that match a given search term.
	 *
	 * @param searchTerm the search term by which to search for patients.
	 * @return the list of patients that match the search term.
	 */
	public List<org.openmrs.Patient> searchPatient(String searchTerm) {
		String jsonResponse = new Http().get("search", "name=" + searchTerm);
		List<Patient> fhirPatients = processPatientsResponse(jsonResponse);
		List<org.openmrs.Patient> openMrsPatients = new ArrayList<org.openmrs.Patient>();
		for (Patient fhirPatient : fhirPatients) {
			org.openmrs.Patient openMrsPatient = new CbConverter().convertToOpenMrsPatient(fhirPatient);
			openMrsPatients.add(openMrsPatient);
		}
		return openMrsPatients;
	}

	/**
	 * Retrieves a patient from the MPI using the patient's cross-border ID.
	 *
	 * @param crossBorderId the patient's cross-border ID.
	 * @return the patient with the given cross-border ID.
	 */
	public org.openmrs.Patient findPatient(String crossBorderId) {
		String jsonResponse = new Http().get("patients", "id=" + crossBorderId);
		Patient fhirPatient = processPatientResponse(jsonResponse);
		org.openmrs.Patient openMrsPatient = new CbConverter().convertToOpenMrsPatient(fhirPatient);
		return openMrsPatient;
	}

	/**
	 * Creates a patient in the MPI.
	 *
	 * @param openMrsPatient the patient to create.
	 * @return the cross-border ID of the created patient.
	 */
	public String createPatient(org.openmrs.Patient openMrsPatient) {
		Patient fhirPatient = new CbConverter().convertToFhirPatient(openMrsPatient);
		String payload = serializePatient(fhirPatient);
		payload = "{\"resourceType\":\"Patient\",\"identifier\":[{\"id\":\"POINT_OF_CARE_ID\",\"value\":\"BUSIA-RN-00\"},{\"id\":\"NATIONAL_ID\",\"system\":\"Kenya\",\"value\":\"098900\"}],\"name\":[{\"family\":\"Gloria\",\"given\":[\"West\"]}],\"telecom\":[{\"value\":\"0719999090\"}],\"gender\":\"female\",\"birthDate\":\"1997-05-05\",\"address\":[{\"city\":\"Langata\",\"state\":\"Nairobi\",\"country\":\"Kenya\"}],\"maritalStatus\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-MaritalStatus\",\"code\":\"MARRIED POLYGAMOUS\",\"display\":\"Married Polygamous\"}],\"text\":\"Married Polygamous\"},\"contact\":[{\"relationship\":[{\"text\":\"Spouse\"}],\"name\":{\"family\":\"Edith Her\"},\"telecom\":[{\"value\":\"0712345678\"}]}]}";
		String jsonResponse = new Http().post("patients", payload);
		String crossBorderId = processCrossBorderIdResponse(jsonResponse);
		return crossBorderId;
	}

	/**
	 * Updates a patient in the MPI.
	 *
	 * @param openMrsPatient the patient to update.
	 * @param crossBorderId  the cross-border ID of the patient to update.
	 * @return the updated patient.
	 */
	public org.openmrs.Patient updatePatient(org.openmrs.Patient openMrsPatient, String crossBorderId) {
		Patient fhirPatient = new CbConverter().convertToFhirPatient(openMrsPatient);
		String payload = serializePatient(fhirPatient);
		payload = "{\"resourceType\":\"Patient\",\"id\":\"302\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2023-02-02T08:43:09.977+00:00\",\"source\":\"#5ZDxH1E3TJbr1zsk\"},\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><div class=\\\"hapiHeaderText\\\">West <b>GLORIA </b></div><table class=\\\"hapiPropertyTable\\\"><tbody><tr><td>Identifier</td><td>BUSIA-RN-00</td></tr><tr><td>Address</td><td><span>Langata </span><span>Nairobi </span><span>Kenya </span></td></tr><tr><td>Date of birth</td><td><span>05 May 1997</span></td></tr></tbody></table></div>\"},\"identifier\":[{\"id\":\"POINT_OF_CARE_ID\",\"value\":\"BUSIA-RN-00\"},{\"id\":\"CROSS_BORDER_ID\",\"value\":\"KE-2023-02-7B732\"},{\"id\":\"NATIONAL_ID\",\"system\":\"Kenya\",\"value\":\"098900\"}],\"name\":[{\"family\":\"Gloria\",\"given\":[\"Anna\"]}],\"telecom\":[{\"value\":\"0719999090\"}],\"gender\":\"female\",\"birthDate\":\"1997-05-05\",\"address\":[{\"city\":\"Langata\",\"state\":\"Nairobi\",\"country\":\"Kenya\"}],\"maritalStatus\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-MaritalStatus\",\"code\":\"MARRIED POLYGAMOUS\",\"display\":\"Married Polygamous\"}],\"text\":\"Married Polygamous\"},\"contact\":[{\"relationship\":[{\"text\":\"Edith Her\"}],\"name\":{\"family\":\"Spouse\"}}]}\n";
		String jsonResponse = new Http().put("patients", payload, "crossBorderId=" + crossBorderId);
		fhirPatient = processPatientResponse(jsonResponse);
		openMrsPatient = new CbConverter().convertToOpenMrsPatient(fhirPatient);
		return openMrsPatient;
	}

	private List<Patient> processPatientsResponse(String jsonResponse) {
		List<Patient> patients = new ArrayList<Patient>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode resultsNode = extractNodeOfInterest(mapper, jsonResponse, "results");
		if (resultsNode.isArray()) {
			for (JsonNode resultNode : resultsNode) {
				String resultJsonString;
				try {
					resultJsonString = mapper.writeValueAsString(resultNode);
					patients.add(deserializePatient(resultJsonString));
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return patients;
	}

	public Patient processPatientResponse(String jsonResponse) {
		Patient patient = new Patient();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode patientNode = extractNodeOfInterest(mapper, jsonResponse, "patient");
		;
		if (patientNode.isObject()) {
			String patientJson;
			try {
				patientJson = mapper.writeValueAsString(patientNode);
				patient = deserializePatient(patientJson);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
		return patient;
	}

	public String processCrossBorderIdResponse(String jsonResponse) {
		ObjectMapper mapper = new ObjectMapper();
		String crossBorderId = extractNodeOfInterest(mapper, jsonResponse, "crossBorderId").asText();
		return crossBorderId;
	}

	private JsonNode extractNodeOfInterest(ObjectMapper mapper, String jsonResponse, String nodeName) {
		JsonNode root;
		try {
			root = mapper.readTree(jsonResponse);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return root.get(nodeName);
	}

	private Patient deserializePatient(String patientJson) {
		FhirContext fhirContext = FhirContext.forR4();
		Patient patient = fhirContext.newJsonParser().parseResource(Patient.class, patientJson);
		return patient;
	}

	private String serializePatient(Patient patient) {
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		String serialized = parser.encodeResourceToString(patient);
		return serialized;
	}
}
