/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

/**
 *
 * @author Lenovo
 */
public class AcademicOfficer extends User_old {
    private String officeID;
    private String department;
    
    public boolean enrollStudent(Student student){
        EligibilityChecker check = new EligibilityChecker();
        if(check.checkProgressionEligibility(student)){
            //send email to student that they're allowed to enroll
            return true;
        }
        return false;
        
    }
    
}
