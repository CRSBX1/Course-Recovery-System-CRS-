package system.app;

public class Milestone {
    private String milestoneID;
    private String weekRange;
    private String taskDescription;
    private boolean completed;

    public Milestone(String milestoneID, String weekRange, String taskDescription, boolean completed) {
        this.milestoneID = milestoneID;
        this.weekRange = weekRange;
        this.taskDescription = taskDescription;
        this.completed = completed;
    }

    public String getMilestoneID() { return milestoneID; }
    public String getWeekRange() { return weekRange; }
    public String getTaskDescription() { return taskDescription; }
    public boolean isCompleted() { return completed; }

    public void setWeekRange(String weekRange) { this.weekRange = weekRange; }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return milestoneID + "|" + weekRange + "|" + taskDescription + "|" + completed;
    }
}