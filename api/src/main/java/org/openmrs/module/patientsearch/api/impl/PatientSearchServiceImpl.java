/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.patientsearch.api.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.patientsearch.PatentSearch;
import org.openmrs.module.patientsearch.UserInfo;
import org.openmrs.module.patientsearch.api.PatientSearchService;
import org.openmrs.module.patientsearch.api.db.PatientSearchDAO;

import java.util.List;

/**
 * It is a default implementation of {@link PatientSearchService}.
 */
public class PatientSearchServiceImpl extends BaseOpenmrsService implements PatientSearchService {
	

	protected final Log log = LogFactory.getLog(this.getClass());
	
	private PatientSearchDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(PatientSearchDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public PatientSearchDAO getDao() {
	    return dao;
    }
    
	@Override
	public List<PatentSearch> getPateintInformationByQuery(String firstName,
			String mobileNo, String district, int limit) {
		return dao.getPateintInformationByQuery(firstName, mobileNo, district, limit);
	}

	@Override
	public List<UserInfo> getUserInfor(String userName) {
		// TODO Auto-generated method stub
		return dao.getUserInfor(userName);
	}

	@Override
	public List<PatentSearch> patientSync(String union,int patientId) {
		
		return dao.patientSync(union,patientId);
	}

}