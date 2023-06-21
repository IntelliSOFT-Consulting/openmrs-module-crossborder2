package org.openmrs.module.crossborder2.metadata;

import static org.openmrs.module.crossborder2.CbConstants._EncounterType.CB_SCREENING_ENCOUNTER_TYPE_UUID;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;

import org.openmrs.module.crossborder2.CbConstants;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

@Component
public class CbMetadata extends AbstractMetadataBundle {
	
	private static final String CB_SCREENING_FORM_UUID = "099d5e12-18fc-11eb-86f3-231df7469c4e";
	
	private static final String CB_REFERRAL_FORM_UUID = "01839000-c275-11ed-9fc5-0b220ae9bf59";
	
	public static class _Privilege {
		
		public static final String APP_CROSS_BORDER_ADMIN = "App: crossborder2.cb";
	}
	
	public static final class _Role {
		
		public static final String APPLICATION_CROSS_BORDER_ADMIN = "Cross-Border app administration";
	}
	
	@Override
	public void install() throws Exception {
		
		install(encounterType("Cross Border Screening", "Cross Border Screening encounter type", CbConstants._EncounterType.CB_SCREENING_ENCOUNTER_TYPE_UUID));

		install(encounterType("Cross Border Referral","Cross Border Referral encounter type", CbConstants._EncounterType.CB_REFERRAL_ENCOUNTER_TYPE_UUID ));
		
		install(form("Cross Border Screening", null, CbConstants._EncounterType.CB_SCREENING_ENCOUNTER_TYPE_UUID, "1",
		    CB_SCREENING_FORM_UUID));
		install(form("Cross Border Referral", null, CbConstants._EncounterType.CB_REFERRAL_ENCOUNTER_TYPE_UUID, "1", CB_REFERRAL_FORM_UUID));
		
		install(privilege(_Privilege.APP_CROSS_BORDER_ADMIN, "Able to access Cross-Border App"));
		
		install(role(_Role.APPLICATION_CROSS_BORDER_ADMIN, "Can access Cross-Border app",
		    idSet(org.openmrs.module.kenyaemr.metadata.SecurityMetadata._Role.API_PRIVILEGES_VIEW_AND_EDIT),
		    idSet(_Privilege.APP_CROSS_BORDER_ADMIN)));
	}
}
