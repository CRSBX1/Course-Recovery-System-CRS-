package system.app;
import java.util.*;
import java.io.*;
import java.time.*;

public class FileUtils {

    private FileUtils(){
        throw new AssertionError("This class can't be instantiated since its made for utility purposes");
    }

    @FunctionalInterface
    public interface lineParser<T>{
        T parser(String line);
    }

    public static <T> List<T> readFromFile(String filename, lineParser<T> parse) throws IOException{
        ArrayList<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null){
                list.add(parse.parser(line));
            }
        }
        catch(Exception e){
            System.out.println("Error when reading file: " + e.getMessage());
        }
        return list;
    }

    public static Student parseStudent(String line){
        String[] parts = line.split(",");
        return new Student(parts[0],parts[1],parts[2],parts[3],Integer.parseInt(parts[4]),Integer.parseInt(parts[5]));
    }

    public static Course parseCourse(String line){
        String[] parts = line.split(",");
        return new Course(parts[0],parts[1],Integer.parseInt(parts[2]),Integer.parseInt(parts[3]),parts[4],Integer.parseInt(parts[5]));
    }

    public static CourseEnrollment parseEnroll(String line){
        String[] parts = line.split(",");
        ArrayList<String> components = new ArrayList<>();
        if (parts.length > 8) {
            for (int i = 8; i < parts.length; i++) {
                components.add(parts[i].trim());
            }
        }
        double assignment = 0.0;
        double mid = 0.0;
        double fin = 0.0;
        int attempt = 0;
        if (parts.length > 6) {
            try { assignment = Double.parseDouble(parts[6]); } catch (Exception ignored) {}
        }
        if (parts.length > 7) {
            try { attempt = Integer.parseInt(parts[7]); } catch (Exception ignored) {}
        }
        return new CourseEnrollment(
                parts[0],
                parts[1],
                parts[2],
                LocalDate.parse(parts[3]),
                parts[4],
                assignment,
                mid,
                fin,
                attempt,
                components
        );
    }
}
