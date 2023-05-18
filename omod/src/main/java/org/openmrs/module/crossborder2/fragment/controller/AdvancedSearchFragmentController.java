package org.openmrs.module.crossborder2.fragment.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.util.PersonByNameComparator;
import org.springframework.web.bind.annotation.RequestParam;

public class AdvancedSearchFragmentController {
	
	protected static final Log log = LogFactory.getLog(AdvancedSearchFragmentController.class);
	
	/**
	 * Gets a patient by their id
	 * 
	 * @param patient the patient
	 * @param ui the UI utils
	 * @return the simplified patient
	 */
	public SimpleObject patient(@RequestParam("id") Patient patient, UiUtils ui) {
		SimpleObject ret = ui.simplifyObject(patient);
		
		// Simplify and attach active visit to patient object
		List<Visit> activeVisits = Context.getVisitService().getActiveVisitsByPatient(patient);
		ret.put("activeVisit", activeVisits.size() > 0 ? ui.simplifyObject(activeVisits.get(0)) : null);
		return ret;
	}
	
	/**
	 * Searches for patients by name, identifier, age, visit status
	 * 
	 * @param query the name or identifier
	 * @param which all|checked-in|non-accounts
	 * @param ui the UI utils
	 * @return the simple patients
	 */
	public List<SimpleObject> patients(@RequestParam(value = "q", required = false) String query,
	        @RequestParam(value = "which", required = false, defaultValue = "all") String which, UiUtils ui) {
		
		// Return empty list if we don't have enough input to search on
		if (StringUtils.isBlank(query) && "all".equals(which)) {
			return Collections.emptyList();
		}
		
		// Run main patient search query based on id/name
		List<Patient> matchedByNameOrID = Context.getPatientService().getPatients(query);
		
		// Gather up active visits for all patients. These are attached to the returned patient representations.
		Map<Patient, Visit> patientActiveVisits = getActiveVisitsByPatients();
		
		List<Patient> matched = new ArrayList<Patient>();
		
		// If query wasn't long enough to be searched on, and they've requested checked-in patients, return the list
		// of checked in patients
		if (StringUtils.isBlank(query) && "checked-in".equals(which)) {
			matched.addAll(patientActiveVisits.keySet());
			Collections.sort(matched, new PersonByNameComparator()); // Sort by person name
		} else {
			if ("all".equals(which)) {
				matched = matchedByNameOrID;
			} else if ("checked-in".equals(which)) {
				for (Patient patient : matchedByNameOrID) {
					if (patientActiveVisits.containsKey(patient)) {
						matched.add(patient);
					}
				}
			} else if ("non-accounts".equals(which)) {
				Set<Person> accounts = new HashSet<Person>();
				accounts.addAll(getUsersByPersons(query).keySet());
				accounts.addAll(getProvidersByPersons(query).keySet());
				
				for (Patient patient : matchedByNameOrID) {
					if (!accounts.contains(patient)) {
						matched.add(patient);
					}
				}
			}
		}
		
		// Simplify and attach active visits to patient objects
		List<SimpleObject> simplePatients = new ArrayList<SimpleObject>();
		for (Patient patient : matched) {
			SimpleObject simplePatient = ui.simplifyObject(patient);
			
			Visit activeVisit = patientActiveVisits.get(patient);
			simplePatient.put("activeVisit", activeVisit != null ? ui.simplifyObject(activeVisit) : null);
			
			simplePatients.add(simplePatient);
		}
		
		return simplePatients;
	}
	
	/**
	 * Helper method to get users organised by person
	 * 
	 * @param query the name query
	 * @return the map of persons to users
	 */
	protected Map<Person, User> getUsersByPersons(String query) {
		Map<Person, User> personToUsers = new HashMap<Person, User>();
		for (User user : Context.getUserService().getUsers(query, null, true)) {
			if (!"daemon".equals(user.getUsername())) {
				personToUsers.put(user.getPerson(), user);
			}
		}
		return personToUsers;
	}
	
	/**
	 * Helper method to get all providers organised by person
	 * 
	 * @param query the name query
	 * @return the map of persons to providers
	 */
	protected Map<Person, Provider> getProvidersByPersons(String query) {
		Map<Person, Provider> personToProviders = new HashMap<Person, Provider>();
		List<Provider> providers = Context.getProviderService().getProviders(query, null, null, null);
		for (Provider p : providers) {
			if (p.getPerson() != null) {
				personToProviders.put(p.getPerson(), p);
			}
		}
		return personToProviders;
	}
	
	/**
	 * Helper method to get all active visits organised by patient
	 * 
	 * @return the map of patients to active visits
	 */
	protected Map<Patient, Visit> getActiveVisitsByPatients() {
		List<Visit> activeVisits = Context.getVisitService().getVisits(null, null, null, null, null, null, null, null, null,
		    false, false);
		Map<Patient, Visit> patientToVisits = new HashMap<Patient, Visit>();
		for (Visit visit : activeVisits) {
			patientToVisits.put(visit.getPatient(), visit);
		}
		return patientToVisits;
	}
}
