package org.openmrs.module.crossborder2.openhim.listener;

import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.event.Event;
import org.openmrs.event.EventListener;
import org.openmrs.module.crossborder2.openhim.CbEncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.Arrays;
import java.util.List;

@Component("cb.EncounterEventListener")
public class EncounterEventListener implements EventListener {
	
	//TODO: Isolate to global properties
	private static final String USERNAME = "admin";
	
	private static final String PASSWORD = "Admin123";
	
	private static final List<String> ENCOUNTERS_OF_INTEREST = Arrays.asList("85019fbe-9339-49f7-8341-e9a04311bb99",
	    "4f02dfed-a2ec-40c2-b546-85dab5831871");
	
	@Autowired
	private CbEncounterService encounterService;
	
	@Override
	public void onMessage(Message message) {
		try {
			Context.openSession();
			Context.authenticate(USERNAME, PASSWORD);
			MapMessage mapMessage = (MapMessage) message;
			String uuid = ((MapMessage) message).getString("uuid");
			if (!ENCOUNTERS_OF_INTEREST.isEmpty()) {
				for (String encounterTypeUuid : ENCOUNTERS_OF_INTEREST) {
					String messageAction = mapMessage.getString("action");
					Encounter encounter = Context.getEncounterService().getEncounterByUuid(uuid);
					if (encounterTypeUuid.equals(encounter.getEncounterType().getUuid().toString())) {
						// TODO: obtain crossBorderId dynamically
						if (Event.Action.CREATED.toString().equals(messageAction)) {
							encounterService.createEncounter(encounter, "KE-2023-02-7B732");
						} else if (Event.Action.UPDATED.toString().equals(messageAction)) {
							encounterService.updateEncounter(encounter, "KE-2023-02-7B732");
						}
					}
				}
				
			}
			Context.closeSession();
		}
		catch (JMSException e) {
			System.out.println("Error occurred" + e.getErrorCode());
		}
	}
}
