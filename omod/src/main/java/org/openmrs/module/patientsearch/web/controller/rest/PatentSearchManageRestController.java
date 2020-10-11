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
import org.openmrs.module.patientsearch.UserInfo;
import org.openmrs.module.patientsearch.api.PatientSearchService;
import org.openmrs.module.patientsearch.converter.PatientSearchDataConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RequestMapping("/rest/v1/patient-search")
@RestController
public class PatentSearchManageRestController extends MainResourceController {
	public static Gson gson = new GsonBuilder().create();
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
	
	@RequestMapping(value = "/user-location", method = RequestMethod.GET)
	public ResponseEntity<String> getUserLocation(@RequestParam String userName
														) throws Exception {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		
		try {
			userInfos = Context.getService(PatientSearchService.class).getUserInfor(userName);
			
		}
		catch (Exception e) {
			return new ResponseEntity<String>(gson.toJson(e.getMessage()), HttpStatus.OK);
		}
	
		return new ResponseEntity<String>(gson.toJson(userInfos), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	public ResponseEntity<String> patientSync(@RequestParam String union,@RequestParam int patientId
														) throws Exception {
		List<PatentSearch> patientSyncList = new ArrayList<PatentSearch>();
		
		try {
			patientSyncList = Context.getService(PatientSearchService.class).patientSync(union, patientId);
			
		}
		catch (Exception e) {
			return new ResponseEntity<String>(gson.toJson(e.getMessage()), HttpStatus.OK);
		}
	
		return new ResponseEntity<String>(gson.toJson(patientSyncList), HttpStatus.OK);
	}
}
