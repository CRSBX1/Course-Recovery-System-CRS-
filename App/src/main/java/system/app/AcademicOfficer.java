package system.app;
import system.app.User;

public class AcademicOfficer extends User {
    private String officeID;
    private String department;

    public AcademicOfficer(String id, String username, String password, UserRole role, String createdBy, String officeID, String department) {
        super(id, username, password, role, createdBy);
        this.officeID = officeID;
        this.department = department;
    }

    public boolean enrollStudent(Student student){
        EligibilityChecker check = new EligibilityChecker();
        if(check.checkProgressionEligibility(student)){
            return true;
        }
        return false;
    }
}
