package org.openmrs.module.patientsearch.web.controller.rest;
import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.Context;
import org.json.JSONArray;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.openmrs.module.patientsearch.PatentSearch;
import org.openmrs.module.patientsearch.api.PatientSearchService;
import org.openmrs.module.patientsearch.converter.PatientSearchDataConverter;

@RequestMapping("/rest/v1/patient-search")
@RestController
public class PatentSearchManageRestController extends MainResourceController {
	
	@RequestMapping(value = "/get-all-patient-info", method = RequestMethod.GET)
	public ResponseEntity<String> getAllChildByPatient(@RequestParam String firstName,
														@RequestParam String mobileNo,
														@RequestParam String district,
														@RequestParam int limit) throws Exception {
		List<PatentSearch> patientSearchList = new ArrayList<PatentSearch>();
		JSONArray patientJsonArray = new JSONArray();
		try {
			patientSearchList = Context.getService(PatientSearchService.class).getPateintInformationByQuery(firstName, mobileNo, district, limit);
			if (patientSearchList != null) {
				patientJsonArray = new PatientSearchDataConverter().toConvert(patientSearchList);
			}
		}
		catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage().toString(), HttpStatus.OK);
		}
		return new ResponseEntity<String>(patientJsonArray.toString(), HttpStatus.OK);
	}
}
