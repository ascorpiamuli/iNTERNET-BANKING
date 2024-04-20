import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
class Course {
    private String courseCode;
    private int courseno;
    private String title;
    private String instructor;
    private String time;
    private int capacity;
    private int enrolledStudents;
    private String coursename;

    public Course(String courseCode,String coursename,String title,int courseno, String instructor, String time, int capacity) {
        this.courseCode = courseCode;
        this.title = title;
        this.instructor = instructor;
        this.time = time;
        this.capacity = capacity;
        this.enrolledStudents = 0;
        this.coursename=coursename;
        this.courseno=courseno;
    }

    // Getters and setters for course attributes
    public boolean isFull() {
        return enrolledStudents >= capacity;
    }

    public void enrollStudent() {
        if (!isFull()) {
            enrolledStudents++;
        }
    }

    public void dropStudent() {
        if (enrolledStudents > 0) {
            enrolledStudents--;
        }
    }

    // Additional methods as needed
    ArrayList<String> coursearr=new ArrayList<>();
    ArrayList<String> codearr=new ArrayList<>();
    ArrayList<String> instructorarr=new ArrayList<>();
    public void addCourse(Scanner scanner){
        System.out.println("Enter the Number of Courses.");
        courseno=scanner.nextInt();
        try {
            if (courseno>0) {
                for (int i = 0; i < courseno; i++) {
                    scanner.nextLine();
                    System.out.println("Enter the Course Title:");
                    title=scanner.nextLine();
                    coursearr.add(title);
                    System.out.println("Enter Coursecode for Course:"+coursearr.get(i));
                    courseCode=scanner.nextLine();
                    System.out.println("Enter Instructor for Course:"+coursearr.get(i));
                    instructor=scanner.nextLine();
                    codearr.add(courseCode);
                    instructorarr.add(instructor);
                }
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        }
 
    }
}
class Student {
    private String studentID;
    private String name;
    private List<Course> registeredCourses;

    public Student(String studentID, String name) {
        this.studentID = studentID;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public void registerCourse(Course course,Scanner scanner) {
        if (!course.isFull()) {
            System.out.println("Please Enter the Number of courses For Registration:");
            course=
            registeredCourses.add(course);
           // course.enrollStudent();
        } else {
            System.out.println("Course is full.");
        }
    }

    public void dropCourse(Course course) {
        if (registeredCourses.contains(course)) {
            registeredCourses.remove(course);
            course.dropStudent();
        }
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    // Additional methods as needed
}

class Semester {
    private List<Course> courses;

    public Semester() {
        this.courses = new ArrayList<>();
    }

    public void addCourse(Scanner scanner) {
        System.out.println("Please Enter The Name of the Student:");
        name=scanner.nextLine();
        courses.add(coursename);
    }

    public List<Course> viewAvailableCourses() {
        return courses;
    }

    // Additional methods for handling course registration
}


public class SemesterRegistrationModule {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Semester semester = new Semester();
        // Example of adding courses. In a real application, this might be loaded from a database or input by the user.
        semester.addCourse(new Course("CS101", "Introduction to Programming", "Dr. Smith", "MWF 10-11", 30));
        semester.addCourse(new Course("MA101", "Calculus I", "Dr. Johnson", "TTh 9-10:30", 30));

        // Main interaction loop
        boolean running = true;
        while (running) {
            System.out.println("1. View available courses\n2. Register for a course\n3. Drop a course\n4. View registered courses\n5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    // Implement viewing available courses
                    break;
                case 2:
                    // Implement course registration logic
                    break;
                case 3:
                    // Implement course dropping logic
                    break;
                case 4:
                    // Implement viewing registered courses logic
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
