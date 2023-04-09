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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CbPatientService {

    public List<org.openmrs.Patient> searchPatient(String searchTerm) {
        String jsonResponse = new Http().get("search", "name=" + searchTerm);
        List<Patient> fhirPatients = processSearchPatientResponse(jsonResponse);
        List<org.openmrs.Patient> omrsPatients = new ArrayList<org.openmrs.Patient>();
        for (Patient patient : fhirPatients) {
            org.openmrs.Patient p = new CbConverter().convertToOpenMrsPatient(patient);
            omrsPatients.add(p);
        }
        return omrsPatients;
    }

    public org.openmrs.Patient findPatient(String crossBorderId) {
        String jsonResponse = new Http().get("patients", "id=" + crossBorderId);
        Patient fhirPatient = processFindPatientResponse(jsonResponse);
        org.openmrs.Patient omrsPatient = new CbConverter().convertToOpenMrsPatient(fhirPatient);
        return omrsPatient;
    }

    private List<Patient> processSearchPatientResponse(String jsonResponse) {
        List<Patient> patients = new ArrayList<Patient>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = extractRootNode(mapper, jsonResponse);
        JsonNode resultsNode = root.get("results");
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

    public Patient processFindPatientResponse(String jsonResponse) {
        Patient patient = new Patient();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = extractRootNode(mapper, jsonResponse);
        JsonNode patientNode = root.get("patient");
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

    private JsonNode extractRootNode(ObjectMapper mapper, String json) {
        JsonNode root;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return root;
    }

    private Patient deserializePatient(String patientJson) {
        FhirContext fhirContext = FhirContext.forR4();
        Patient patient = fhirContext.newJsonParser().parseResource(Patient.class, patientJson);
        return patient;
    }
}
