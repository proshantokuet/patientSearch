package org.openmrs.module.patientsearch.converter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.module.patientsearch.PatentSearch;

public class PatientSearchDataConverter {
	
	public JSONObject toConvert(PatentSearch patientSearch) throws JSONException {
		JSONObject patientsearchObject = new JSONObject();
		patientsearchObject.putOpt("healthId", patientSearch.getHealthId());
		patientsearchObject.putOpt("firstName", patientSearch.getFirstName());
		patientsearchObject.putOpt("phoneNo", patientSearch.getPhoneNo());
		patientsearchObject.putOpt("fatherName", patientSearch.getFatherName());
		patientsearchObject.putOpt("gender", patientSearch.getGender());
		patientsearchObject.putOpt("registeredDate", patientSearch.getRegisteredDate());
		patientsearchObject.putOpt("district", patientSearch.getDistrict());
		patientsearchObject.putOpt("age", patientSearch.getAge());
		patientsearchObject.putOpt("lastName", patientSearch.getLastName());
		patientsearchObject.putOpt("patientUuid", patientSearch.getPatientUuid());
		return patientsearchObject;
	}
	
	
	public JSONArray toConvert(List<PatentSearch> patientSearchs) throws JSONException {
		JSONArray patientsearchArray = new JSONArray();
		for (PatentSearch patientSearch : patientSearchs) {
			patientsearchArray.put(toConvert(patientSearch));
		}
		return patientsearchArray;
	}
}
