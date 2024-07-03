package com.motorph.ems.service;

import com.motorph.pms.model.Employment;

public interface EmploymentService {

    //EMPLOYMENT SERVICES

    /**
     * Add new employment details
     * @param employmentDetails the new employment details
     */
    Employment addNewEmployment(Employment employmentDetails);

    /**
     * Retrieve employment details by employee ID
     * @param employeeID the employee ID whose employment is to be retrieved
     * @return the employment details
     */
    Employment getEmploymentByEmployeeId(Long employeeID);

    /**
     * Update employment details
     * @param updatedEmployment the updated employment details
     * @return the updated employment details
     */
    Employment updateEmployment(Employment updatedEmployment);

    /**
     * Delete employment details
     * @param employeeID the employee ID whose employment is to be deleted
     */
    void deleteEmployment(Long employeeID);
}
