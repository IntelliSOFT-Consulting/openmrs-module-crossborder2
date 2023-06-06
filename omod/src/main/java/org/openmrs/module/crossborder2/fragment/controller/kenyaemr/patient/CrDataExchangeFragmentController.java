package org.openmrs.module.crossborder2.fragment.controller.kenyaemr.patient;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.crossborder2.CbConstants;
import org.openmrs.module.crossborder2.openhim.CbPatientService;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrDataExchangeFragmentController {
	
	public void controller(FragmentModel model, @FragmentParam("patient") Patient patient) {
	}
	
	public SimpleObject postUpiClientRegistrationInfoToCR(@RequestParam("postParams") String params,
	        @RequestParam(value = "patient", required = false) Patient patient, @SpringBean CbPatientService cbPatientService)
	        throws IOException, NoSuchAlgorithmException, KeyManagementException {
		SimpleObject responseObj = null;
		//Check whether this client already has NUPI number hence this is an error verification
		Patient patient1 = getPatientUsingNationalIdentifiers(params);
		final PatientIdentifierType crossborderPatientIdType = Context.getPatientService().getPatientIdentifierTypeByUuid(
		    CbConstants._PatientIdentifierType.CROSS_BORDER_IDENTIFIER_UUID);
		try {
			responseObj = new SimpleObject();
			Patient patientToSave = null;
			if (patient1 == null) {
				//TODO add object mapper to map params to patient
				// System.out.println("These are the params" + params);
				Patient formattedPatient = createDummyPatient(params);
				if (formattedPatient != null) {
					patientToSave = formattedPatient;
				}
			} else {
				patientToSave = patient1;
			}
			
			//if patient to save has a cross border id, update the upstream record, otherwise create a new patient record
			Patient savedPatient;
			PatientIdentifier crossborderPatientIdentifier = null;
			if (patientToSave != null && !patientToSave.getIdentifiers().isEmpty()) {
				PatientIdentifier found = null;
				for (PatientIdentifier p : patientToSave.getIdentifiers()) {
					if (p.getIdentifierType().equals(crossborderPatientIdType)) {
						found = p;
						break;
					}
				}
				crossborderPatientIdentifier = found;
			}
			
			if (crossborderPatientIdentifier != null) {
				savedPatient = cbPatientService.updatePatient(patientToSave, crossborderPatientIdentifier.getIdentifier());
			} else {
				savedPatient = cbPatientService.createPatient(patientToSave);
			}
			
			responseObj.put("status", "200");
			responseObj.put("clientNumber", savedPatient.getPatientIdentifier(crossborderPatientIdType).getIdentifier());
			
		}
		catch (RuntimeException ex) {
			responseObj.put("status", "400");
			responseObj.put("message", ex.getMessage());
		}
		
		return responseObj;
	}
	
	private Patient createDummyPatient(String params) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = null;
		String identificationType = "";
		String identificationNumber = "";
		String nupiNumber = "";
		Patient patient = null;
		try {
			objectNode = (ObjectNode) mapper.readTree(params);
			if (objectNode != null) {
				ArrayNode resultsArrayNode = (ArrayNode) objectNode.get("identifications");
				patient = new Patient();
				patient.setUuid(UUID.randomUUID().toString());
				patient.setGender(StringUtils.left(objectNode.get("gender").asText(), 1).toUpperCase());
				patient.setBirthdate(new Date());
				patient.setBirthdateEstimated(false);
				patient.setDead(false);
				patient.setVoided(false);
				patient.setDeathDate(null);
				PersonName personName = new PersonName();
				personName.setGivenName(objectNode.get("firstName").asText());
				personName.setMiddleName(objectNode.get("middleName").asText());
				personName.setFamilyName(objectNode.get("lastName").asText());
				personName.setUuid(UUID.randomUUID().toString());
				patient.addName(personName);
				if (resultsArrayNode.size() > 0) {
					for (int i = 0; i < resultsArrayNode.size(); i++) {
						
						identificationType = resultsArrayNode.get(i).get("identificationType").asText();
						identificationNumber = resultsArrayNode.get(i).get("identificationNumber").asText();
						
						// Get client by identificationType:  Identifier type used
						PatientIdentifierType natIdentifierType = MetadataUtils.existing(PatientIdentifierType.class,
						    CommonMetadata._PatientIdentifierType.NATIONAL_ID);
						PatientIdentifierType birtCertIdentifierType = MetadataUtils.existing(PatientIdentifierType.class,
						    CommonMetadata._PatientIdentifierType.BIRTH_CERTIFICATE_NUMBER);
						
						PatientService patientService = Context.getPatientService();
						List<Patient> patients = patientService.getPatients(null, identificationNumber.trim(),
						    Arrays.asList(natIdentifierType, birtCertIdentifierType), false);
						
						if (patients.size() > 0) {
							patient = patients.get(0);
							
							// Got patient
							// Check whether patient already has NUPI
							PatientIdentifierType nupiIdentifierType = MetadataUtils.existing(PatientIdentifierType.class,
							    CbConstants._PatientIdentifierType.CROSS_BORDER_IDENTIFIER_UUID);
							PatientIdentifier nupiObject = patient.getPatientIdentifier(nupiIdentifierType);
							
							nupiNumber = nupiObject.getIdentifier();
						} else {
							PatientIdentifier patientIdentifier = new PatientIdentifier();
							patientIdentifier.setPatient(patient);
							if (identificationType.equals("national-id")) {
								patientIdentifier.setIdentifierType(natIdentifierType);
							} else {
								patientIdentifier.setIdentifierType(birtCertIdentifierType);
							}
							patientIdentifier.setIdentifier(identificationNumber);
							
							patient.addIdentifier(patientIdentifier);
						}
						
					}
					
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return patient;
	}
	
	/**
	 * Processes POST Request to determine if client already has NUPI
	 * 
	 * @param params the POST payload
	 * @return NUPI from the processed data
	 */
	public Patient getPatientUsingNationalIdentifiers(String params) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = null;
		String identificationType = "";
		String identificationNumber = "";
		String nupiNumber = "";
		Patient patient = null;
		try {
			objectNode = (ObjectNode) mapper.readTree(params);
			if (objectNode != null) {
				ArrayNode resultsArrayNode = (ArrayNode) objectNode.get("identifications");
				if (resultsArrayNode.size() > 0) {
					for (int i = 0; i < resultsArrayNode.size(); i++) {
						
						identificationType = resultsArrayNode.get(i).get("identificationType").asText();
						identificationNumber = resultsArrayNode.get(i).get("identificationNumber").asText();
						
						// Get client by identificationType:  Identifier type used
						PatientIdentifierType natIdentifierType = MetadataUtils.existing(PatientIdentifierType.class,
						    CbConstants._PatientIdentifierType.NATIONAL_ID);
						PatientIdentifierType birtCertIdentifierType = MetadataUtils.existing(PatientIdentifierType.class,
						    CbConstants._PatientIdentifierType.BIRTH_CERTIFICATE_NUMBER);
						
						PatientService patientService = Context.getPatientService();
						List<Patient> patients = patientService.getPatients(null, identificationNumber.trim(),
						    Arrays.asList(natIdentifierType, birtCertIdentifierType), false);
						
						if (patients.size() > 0) {
							patient = patients.get(0);
							
							// Got patient
							// Check whether patient already has NUPI
							PatientIdentifierType nupiIdentifierType = MetadataUtils.existing(PatientIdentifierType.class,
							    CbConstants._PatientIdentifierType.CROSS_BORDER_IDENTIFIER_UUID);
							PatientIdentifier nupiObject = patient.getPatientIdentifier(nupiIdentifierType);
							
							nupiNumber = nupiObject.getIdentifier();
							
						}
						
					}
					
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return patient;
	}
}
