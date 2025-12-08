package system.app;

import java.util.*;

public class RecoveryPlan {
    private String studentID;
    private String studentName;
    private String courseID;
    private String courseName;
    private String description;
    private List<Milestone> milestones = new ArrayList<>();

    public RecoveryPlan(String studentID, String studentName, String courseID, String courseName, String description) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.courseID = courseID;
        this.courseName = courseName;
        this.description = description;
    }

    public String getStudentID() { return studentID; }
    public String getStudentName() { return studentName; }
    public String getCourseID() { return courseID; }
    public String getCourseName() { return courseName; }
    public String getDescription() { return description; }
    public List<Milestone> getMilestones() { return milestones; }

    public void addMilestone(Milestone m) { milestones.add(m); }
    public void setMilestones(List<Milestone> list) { milestones = list; }
}