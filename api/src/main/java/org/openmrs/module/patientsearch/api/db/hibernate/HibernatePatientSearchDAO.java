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
package org.openmrs.module.patientsearch.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.module.patientsearch.PatentSearch;
import org.openmrs.module.patientsearch.UserInfo;
import org.openmrs.module.patientsearch.api.db.PatientSearchDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * It is a default implementation of  {@link PatientSearchDAO}.
 */
public class HibernatePatientSearchDAO implements PatientSearchDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
	/**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<PatentSearch> getPateintInformationByQuery(String firstName, String mobileNo, String district, int limit) {
		List<PatentSearch> patientList = new ArrayList<PatentSearch>();
		
		String filterString = "";
		if (!firstName.isEmpty() && mobileNo.isEmpty() && district.isEmpty()) {
			filterString = "WHERE  pname.given_name LIKE '"+ firstName +"%'";
		}
		else if (firstName.isEmpty() && !mobileNo.isEmpty() && district.isEmpty()) {
			filterString = "WHERE  temp1.phoneno LIKE '"+ mobileNo +"%'";
		}
		else if (firstName.isEmpty() && mobileNo.isEmpty() && !district.isEmpty()) {
			filterString = "WHERE  paddress.address2 = '"+ district +"'";
		}
		
		if (!firstName.isEmpty() && !mobileNo.isEmpty()) {
			
			filterString = "";
			filterString = "WHERE  pname.given_name LIKE '"+ firstName +"%'" +" AND temp1.phoneno LIKE '"+ mobileNo +"%'";
		}
		else if (!firstName.isEmpty() && !district.isEmpty()) {
			
			filterString = "";
			filterString = "WHERE  pname.given_name LIKE '"+ firstName +"%'" +" AND paddress.address2 = '"+ district +"'";
		}
		else if (!mobileNo.isEmpty() && !district.isEmpty()) {
			
			filterString = "";
			filterString = "WHERE  temp1.phoneno LIKE '"+ mobileNo +"%'" +" AND paddress.address2 = '"+ district +"'";
		}
		 if (!firstName.isEmpty() && !mobileNo.isEmpty() && !district.isEmpty()) {
			filterString = "";
			filterString = "WHERE  pname.given_name LIKE '"+ firstName +"%' \n" +
	                "        AND temp1.phoneno LIKE '"+ mobileNo +"%' \n" +
	                "        AND paddress.address2 = '"+ district +"'";
		}
		 
		String patientSql =  "SELECT pi.identifier         AS healthId, \n" +
                "       pname.given_name      AS firstName, \n" +
                "       pname.family_name      AS lastName, \n" +
                "       temp1.phoneno         AS phoneNo, \n" +
                "       temp3.fatherName AS fatherName, \n" +
                "       p.gender              AS gender, \n" +
                "       pname.date_created  AS registeredDate,\n" +
                "       paddress.address2 as district,\n" +
                "       IFNULL(TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE()),0) AS age,\n" +
                "       p.uuid           AS patientUuid \n" +
                "       \n" +
                "FROM   person_name pname \n" +
                "       JOIN patient_identifier pi \n" +
                "              ON pname.person_id = pi.patient_id \n" +
                "        JOIN person p \n" +
                "              ON p.person_id = pi.patient_id\n" +
                "\t    JOIN person_address paddress\n" +
                "              ON paddress.person_id = pi.patient_id\n" +
                "        JOIN (SELECT pat.person_attribute_type_id, \n" +
                "                         pat.value AS phoneNo, \n" +
                "                         pat.person_id \n" +
                "                  FROM   person_attribute pat \n" +
                "                  WHERE  pat.person_attribute_type_id = 26) AS temp1 \n" +
                "              ON pi.patient_id = temp1.person_id \n" +
                "        JOIN (SELECT pa.person_attribute_type_id, \n" +
                "                         pa.value AS fatherName, \n" +
                "                         pa.person_id \n" +
                "                  FROM   person_attribute pa \n" +
                "                  WHERE  pa.person_attribute_type_id = 28) AS temp3 \n" +
                "              ON pi.patient_id = temp3.person_id \n" +
                 filterString + " and p.gender <> 'H' and pname.preferred = 1 and paddress.preferred = 1 LIMIT "+limit+",30;";
		
		patientList = sessionFactory.getCurrentSession().createSQLQuery(patientSql).addScalar("healthId", StandardBasicTypes.STRING)
				.addScalar("firstName", StandardBasicTypes.STRING).addScalar("lastName", StandardBasicTypes.STRING)
				.addScalar("phoneNo", StandardBasicTypes.STRING).addScalar("fatherName", StandardBasicTypes.STRING).addScalar("gender", StandardBasicTypes.STRING)
				.addScalar("registeredDate", StandardBasicTypes.DATE).addScalar("district", StandardBasicTypes.STRING)
				.addScalar("age", StandardBasicTypes.INTEGER).addScalar("patientUuid", StandardBasicTypes.STRING)
				.setResultTransformer(new AliasToBeanResultTransformer(PatentSearch.class)).list();
		return patientList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> getUserInfor(String userName) {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		String sql="select u.user_id userId, u.username userName,tml.location_id wardId,l.name wardName,l1.name uninName,"
				+ " l1.location_id unionId from openmrs.users u join openmrs.team_member tm on "
				+ " u.person_id = tm.person_id join openmrs.team_member_location tml"
				+ "  on tm.team_id = tml.team_member_id join openmrs.location l "
				+ " on tml.location_id = l.location_id left join openmrs.location l1 on "
				+ " l.parent_location = l1.location_id where u.username=:username";
		userInfos =sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("userId", StandardBasicTypes.INTEGER)
		.addScalar("userName", StandardBasicTypes.STRING).addScalar("wardId", StandardBasicTypes.INTEGER)
		.addScalar("wardName", StandardBasicTypes.STRING).addScalar("uninName", StandardBasicTypes.STRING).addScalar("unionId", StandardBasicTypes.INTEGER)
		.setString("username", userName)
		.setResultTransformer(new AliasToBeanResultTransformer(UserInfo.class)).list();
		return userInfos;
	}
}