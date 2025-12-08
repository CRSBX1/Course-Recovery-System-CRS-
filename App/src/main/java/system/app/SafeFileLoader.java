package system.app;

import java.io.IOException;

/**
 * SafeFileLoader - Wrapper around DataRepository loading with error handling
 * This ensures data is loaded safely without crashing the application
 */
public class SafeFileLoader {

    /**
     * Load all data using the existing DataRepository methods
     * This maintains consistency by using the same parsing logic everywhere
     */
    public static void loadAllData() {
        try {
            // Use DataRepository's existing load methods
            DataRepository.loadStudentData();
            DataRepository.loadCourseData();
            DataRepository.loadEnrollData();
            
            // Link all the data together
            DataRepository.linkAll();
            
            System.out.println("Data loaded successfully:");
            System.out.println("- Students: " + DataRepository.studentList.size());
            System.out.println("- Courses: " + DataRepository.courseList.size());
            System.out.println("- Enrollments: " + DataRepository.enrollList.size());
            
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            
            // Initialize empty lists to prevent null pointer exceptions
            if (DataRepository.studentList == null) {
                DataRepository.studentList = new java.util.ArrayList<>();
            }
            if (DataRepository.courseList == null) {
                DataRepository.courseList = new java.util.ArrayList<>();
            }
            if (DataRepository.enrollList == null) {
                DataRepository.enrollList = new java.util.ArrayList<>();
            }
        }
    }
}