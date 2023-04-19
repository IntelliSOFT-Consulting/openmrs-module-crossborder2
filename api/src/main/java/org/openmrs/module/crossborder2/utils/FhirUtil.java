package org.openmrs.module.crossborder2.utils;

import static org.apache.commons.lang3.Validate.notNull;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.utils.client.EFhirClientException;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.crossborder2.CbConstants;

public class FhirUtil {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	// locking object
	private final static Object s_lockObject = new Object();
	
	// Instance
	private static FhirUtil s_instance = null;
	
	/**
	 * Creates a new message utility
	 */
	private FhirUtil() {
	}
	
	/**
	 * Get an instance of the message utility
	 */
	public static FhirUtil getInstance() {
		if (s_instance == null)
			synchronized (s_lockObject) {
				if (s_instance == null)
					s_instance = new FhirUtil();
			}
		return s_instance;
	}
	
	/**
	 * @param fhirPatient The FHIR patient to be parsed
	 * @return The OpenMRS patient
	 * @summary Parse a FHIR patient into an OpenMRS patient
	 */
	// TODO: replace with fhir2 functionality (translator MpiPatient <-> Patient)
	public Patient parseFhirPatient(org.hl7.fhir.r4.model.Patient fhirPatient) {
		Patient patient = new Patient();
		
		notNull(patient, "The existing Openmrs Patient object should not be null");
		notNull(fhirPatient, "The Patient object should not be null");
		
		//		Set UUID
		//		patient.setUuid(fhirPatient.getId());
		
		// Attempt to copy names
		for (HumanName name : fhirPatient.getName()) {
			PersonName pn = this.interpretFhirName(name);
			patient.addName(pn);
		}
		
		// Copy gender
		if (Enumerations.AdministrativeGender.FEMALE.equals(fhirPatient.getGender()))
			patient.setGender("F");
		else if (Enumerations.AdministrativeGender.MALE.equals(fhirPatient.getGender()))
			patient.setGender("M");
		else
			patient.setGender("U");
		
		// Copy DOB
		if (fhirPatient.hasBirthDate()) {
			patient.setBirthdate(fhirPatient.getBirthDate());
			if (fhirPatient.getBirthDateElement().getValueAsString().length() < 10) // Approx
				patient.setBirthdateEstimated(true);
		}
		
		// Death details
		if (fhirPatient.hasDeceased()) {
			try {
				fhirPatient.getDeceasedBooleanType();
				patient.setDead(fhirPatient.getDeceasedBooleanType().booleanValue());
			}
			catch (FHIRException ignored) {}
			try {
				fhirPatient.getDeceasedDateTimeType();
				patient.setDead(true);
				patient.setDeathDate(fhirPatient.getDeceasedDateTimeType().getValue());
			}
			catch (FHIRException ignored) {}
		}
		
		return patient;
	}
	
	/**
	 * Interpret the FHIR HumanName as a Patient Name
	 * 
	 * @return The interpreted name
	 */
	private PersonName interpretFhirName(HumanName name) {
		if (name == null) {
			return null;
		}
		PersonName pn = new PersonName();
		pn.setUuid(name.getId());
		
		if (name.getFamily() == null || name.getFamily().isEmpty())
			pn.setFamilyName("(NULL)");
		else
			pn.setFamilyName(name.getFamily());
		
		// Given name
		if (!name.hasGiven())
			pn.setGivenName("(NULL)");
		else
			pn.setGivenName(name.getGivenAsSingleString());
		
		pn.setPrefix(name.getPrefixAsSingleString());
		
		if (HumanName.NameUse.OFFICIAL.equals(name.getUse()))
			pn.setPreferred(true);
		
		return pn;
	}
	
	public IGenericClient getClient() throws EFhirClientException {
		FhirContext ctx = FhirContext.forR4();
		
		IGenericClient client = ctx.newRestfulGenericClient(Context.getAdministrationService().getGlobalProperty(
		    CbConstants.PROP_MPI_SERVER_URL, "http://hapi.fhir.org/baseR4"));
		
		client.setEncoding(EncodingEnum.JSON);
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		
		// Basic Auth
		client.registerInterceptor(new BasicAuthInterceptor(Context.getAdministrationService().getGlobalProperty(
		    CbConstants.PROP_MPI_SERVER_USERNAME, "postman"), Context.getAdministrationService().getGlobalProperty(
		    CbConstants.PROP_MPI_PASSWORD, "postman")));
		
		return client;
	}
}
