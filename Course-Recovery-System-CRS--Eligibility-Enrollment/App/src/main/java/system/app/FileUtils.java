package system.app;
import java.util.*;
import java.io.*;
import java.time.*;

public class FileUtils {

    private FileUtils(){
        throw new AssertionError("This class can't be instantiated since its made for utility purposes");
    }

    public static String getPath(String fileName){
        return System.getProperty("user.dir")
                + File.separator + fileName;
    }

    @FunctionalInterface
    public interface lineParser<T>{
        T parser(String line);
    }

    @FunctionalInterface
    public interface destination{
        HashMap<Integer,String> dataMap();
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

    public static void writeToFile(String fileName, destination map) throws IOException{
        String path = getPath(new File(fileName).getName());

        try{
            FileWriter clearFile = new FileWriter(path);
            clearFile.close();
        }
        catch(IOException e){
            System.out.println("Error when writing to file: " + e.getMessage());
        }

        try{
            FileWriter write = new FileWriter(path,true);
            for(String s: map.dataMap().values()){
                write.append(s);
            }
            write.close();
        }
        catch(IOException e){
            System.out.println("Error when writing to file: " + e.getMessage());
        }
    }

    public static Student parseStudent(String line){
        String[] parts = line.split(",");
        return new Student(parts[0],parts[1],parts[2],parts[3],
                Integer.parseInt(parts[4]),Integer.parseInt(parts[5]),parts[6]);
    }

    public static Course parseCourse(String line){
        String[] parts = line.split(",");
        return new Course(parts[0],parts[1],Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),parts[4],Integer.parseInt(parts[5]));
    }

    public static CourseEnrollment parseEnroll(String line){
        ArrayList<String> components = new ArrayList<>();
        String[] parts = line.split(",");
        for(int i=9;i<parts.length;i++){
            components.add(parts[i]);
        }
        return new CourseEnrollment(
                parts[0], parts[1], parts[2], LocalDate.parse(parts[3]),
                parts[4], Double.parseDouble(parts[5]),
                Double.parseDouble(parts[6]), Double.parseDouble(parts[7]),
                Integer.parseInt(parts[8]), components
        );
    }

    public static HashMap writeStudent(){
        int counter = 0;
        HashMap<Integer,String> studentMap = new HashMap<>();
        for(Student i: DataRepository.studentList){
            String credentials = i.getStudentID()+","+i.getStudentName()+","+
                    i.getEmail()+","+i.getStudentProgram()+","+
                    i.getYear()+","+i.getSemester()+","+
                    i.getEnrollStatus()+"\n";
            studentMap.put(counter, credentials);
            counter++;
        }
        return studentMap;
    }
}
