package org.openmrs.module.crossborder2.metadata;

import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.form;

@Component
public class CbMetadata extends AbstractMetadataBundle {
	
	private static final String CB_SCREENING_FORM_UUID = "099d5e12-18fc-11eb-86f3-231df7469c4e";
	
	private static final String CB_REFERRAL_FORM_UUID = "01839000-c275-11ed-9fc5-0b220ae9bf59";
	
	@Override
	public void install() throws Exception {
		install(form("Cross Border Screening", null, HivMetadata._EncounterType.HIV_CONSULTATION, "1",
		    CB_SCREENING_FORM_UUID));
		install(form("Cross Border Referral", null, HivMetadata._EncounterType.HIV_CONSULTATION, "1", CB_REFERRAL_FORM_UUID));
	}
}
