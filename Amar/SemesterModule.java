import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class Course {
    private String courseName;
    private String courseInstructor;
    private int courseCapacity;
    Course(){
        //default Constructor
    }
    Course(Course object){
        this.courseName=object.getCourseName();
        this.courseInstructor=object.getCourseInstructor();
        this.courseCapacity=object.getCapacity();

    }
    Course(String name ,String instructor ,int cap){
        this.courseName=name;
        this.courseInstructor=instructor;
        this.courseCapacity=cap;
    }
    public void CourseSchedule(String Sessions){
        String Dates="\n 1 (Mon)       2 (Tue)         3  (Wed)         4  (Thur)        5 (Fri)"+
                     "\n   7:00a.m         10.00a.m        7:00a.m          2:00p.m         10:00a.m    \n";
        System.out.println(Dates+"\n"+Sessions);
    }
    

    String getCourseName(){
        return courseName;
    }
    String getCourseInstructor(){
        return courseInstructor;
    }
    int getCapacity(){
        return courseCapacity;
    }

}

class Student extends Course{

    private String firstName;
    private String lastName;
    private String StudentID;
    private int ID;
    private String info;
    private String password; 

    public Student(){
        ID=0;
        firstName="default";
    }
    public Student(String firstN ,String lastN ,String studentID,int Num){
        this.firstName=firstN;
        this.lastName=lastN;
        this.ID=Num;
        this.StudentID=studentID;
        this.info ="\n-------------------------------" +
                        "\nFirstName: " +firstName +
                        "\nlastName: "  +lastName  +
                        "\nPersonalID: "+ID        +
                        "\nStudentID:"   +StudentID +
                        "\n-------------------------------";
    }

    public Student(String firstN ,String lastN ,int Num,String Gradyear){
        this.firstName=firstN;
        this.lastName=lastN;
        this.ID=Num;
        this.StudentID=Num+""+Gradyear;
        this.info ="\n-------------------------------" +
                        "\nFirstName: " +firstName +
                        "\nlastName: "  +lastName  +
                        "\nPersonalID: "+ID        +
                        "\nStudentID:"   +StudentID +
                        "\n-------------------------------";
        Semester.saveUserdata(firstName,lastName , StudentID,ID);
    }

    public String getName(){
        return firstName;
    }
    public String getlastName(){
        return lastName;
    }

     // Method to display student information
    public void myInfo(){
        System.out.print(info);
    }

    public String getStudentID(){
        return StudentID;
    }

    // Method to set password for student
    public void setPassword(Student currentStudents){
            try (Scanner input = new Scanner(System.in)) {
                System.out.println("Enter Password: ");
                String UserInput=input.next();
                System.out.print("Confirm Password: ");
                String Confirm =input.next();

                if(Confirm.equals(UserInput)){
                    this.password=Confirm;
                    Semester.saveLogindata(currentStudents,password);
                }
                else{System.out.println("Password does not match");
                    password=null;}
            } 
            catch (InputMismatchException e) {
                // Handle input mismatch exception
                System.out.println("Invalid input format");
            }
    }
    
} 
class Semester
{
        final static String REG_COURSES = "RegCourses.txt";
        final static String USER_DATA = "UserData.txt";
        final static String COURSE_DATA = "CourseData.txt";
        final static String LOGIN_DATA = "LoginData.txt";
        final static String COURSE_SCHEDULE = "CourseSchedule.txt";


        static HashMap<String, Set <Course>> RegisteredCourses =new HashMap<>();
        static HashMap<String,Course> DropCourse =new HashMap<>();
        static HashMap<String,String> CSchedule=new HashMap<>();
        
        static HashMap<Integer,Course> courses =new HashMap<>();
        static HashMap<String,Student> students =new HashMap<>();
        static Set<String> UserInfo =new HashSet<>();
        static HashMap<String ,String> LoginInfo =new HashMap<>();
        static int courseCount=1;
    
        void adminAddCourse(int key,String name ,String lec ,int cap){
            courses.put(key,new Course(name,lec,cap));

        }
        void adminAddCourse(String name ,String lec ,int cap){
            courses.put(courseCount,new Course(name,lec,cap));
            saveCoursedata(courseCount,name,lec,cap);
            courseCount++;

        }
        public static void saveCourseSchedule(Student student,String Csessions){
            if (!CSchedule.containsKey(student.getStudentID())) {
                CSchedule.put(student.getStudentID(), Csessions);}
                
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(COURSE_SCHEDULE))) {
                    for(Map.Entry<String,String> entry:CSchedule.entrySet()){
                        writer.write(entry.getKey()+":"+entry.getValue());
                        writer.newLine();
                    }
                    System.out.println("\nSuccessfully saved Course Schedule");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }  
        }
        public static String loadSessions(Student student){
            //chance if not loaded
            if(CSchedule.isEmpty()){loadCourseSchedule();}

            String session = CSchedule.getOrDefault(student.getStudentID(), "No sessions");
            return session;
        
        }
        public static void loadCourseSchedule(){
            try (BufferedReader br = new BufferedReader(new FileReader(COURSE_SCHEDULE))){
                    String line;
                    while ((line=br.readLine()) != null){
                        String[] parts =line.split(":",2);
                        if(parts.length==2){
                            CSchedule.put(parts[0],parts[1]);
                        }
                    }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void saveRegCourses(Student student) {
            if (!DropCourse.isEmpty()) {
                for (Map.Entry<String, Course> dropEntry : DropCourse.entrySet()) {
                    String dropStudentID = dropEntry.getKey();
                    Course dropCourse = dropEntry.getValue();
                    Set<Course> courses = RegisteredCourses.get(dropStudentID);
                        if (courses != null) {
                            courses.remove(dropCourse);
                                if (courses.isEmpty()) {
                                    RegisteredCourses.remove(dropStudentID); // Remove the entry from the map
                                }
                        }
                }
            }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(REG_COURSES))) {
                    for (Map.Entry<String, Set<Course>> entry : RegisteredCourses.entrySet()) {
                        String studentID = entry.getKey();
                        Set<Course> studentCourses = entry.getValue();
                        writer.write(studentID + ":");
                        boolean isFirst = true;
                        for (Course course : studentCourses) {
                            if (!isFirst) {
                                writer.write("/");
                            } else {
                                isFirst = false;
                            }
                            writer.write(course.getCourseName() + "," + course.getCourseInstructor() + "," + course.getCapacity());
                        }
                        writer.newLine();
                        System.out.println("Data written to file: " + studentID + ":" + studentCourses);
                    }
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }   
        }  
        
        public static void loadRegCourses(Student student) {
            try (BufferedReader reader = new BufferedReader(new FileReader(REG_COURSES))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length >= 2) {
                        String studentID = parts[0].trim();
                        String[] courseInfo = parts[1].trim().split("\\/"); // Split courses separated by dot
        
                        Set<Course> courses = new HashSet<>();
                        for (String courseStr : courseInfo) {
                            String[] courseDetails = courseStr.split(",");
                            if (courseDetails.length >= 3) { // Ensure enough elements in courseDetails
                                String courseName = courseDetails[0].trim();
                                String courseInstructor = courseDetails[1].trim();
                                int capacity = Integer.parseInt(courseDetails[2].trim());
                                Course course = new Course(courseName, courseInstructor, capacity);
                                courses.add(course);
                            } else {
                                System.out.println("Invalid course information: " + courseStr);
                                // Handle invalid course information gracefully
                            }
                        }
                        RegisteredCourses.put(studentID, courses);
        
                        // Debugging: Print out the data being loaded
                        System.out.println("Loaded courses for student " + studentID + ": " + courses);
                    } else {
                        System.out.println("Invalid line format/file is Empty: " + line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }        
        }    
        
        
        public static void saveUserdata(String fname, String lastName, String studentID, int ID){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DATA,true))) {
                bw.write(fname+":"+lastName+":"+studentID+":"+ID);
                bw.newLine();
                System.out.println("Successfully saved user data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void saveLogindata(Student StudentInfo ,String password){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOGIN_DATA,true))) {
                bw.write(StudentInfo.getName()+" "+StudentInfo.getlastName()+
                ":"+password);
                bw.newLine();
            } catch (IOException e) {
                
                e.printStackTrace();
            } 
        }

        public static void saveCoursedata(int key,String name, String lec ,int cap){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(COURSE_DATA,true))) {
                bw.write(key+":"+name+":"+lec+":"+cap);
                bw.newLine();
                System.out.println("Successfully added "+name+" by "+lec);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void loadUserdata(){
            try 
                (BufferedReader br = new BufferedReader(new FileReader (USER_DATA))) {
                    String line;
                    while ((line=br.readLine()) != null){
                        String[] parts =line.split(":",4);
                        if(parts.length==4){
                            students.put(parts[2],new Student(parts[0],parts[1],parts[2],Integer.parseInt(parts[3])));
                        }
                    }
                } catch (IOException e) {
                    
                    e.printStackTrace();
                }
        }

        public static void loadLogindata(){
            try 
                (BufferedReader br = new BufferedReader(new FileReader (LOGIN_DATA))){
                    String line;
                    while ((line=br.readLine()) != null){
                        String[] parts=line.split(":",2);
                        if(parts.length==2){
                            LoginInfo.put(parts[0],parts[1]);
                        }
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void loadCourseData(Semester sem){
            try (BufferedReader br = new BufferedReader(new FileReader(COURSE_DATA))) {
                String line;
                while ((line=br.readLine()) != null){
                    String[] parts =line.split(":",4);
                    int k=Integer.parseInt(parts[0]);
                    sem.adminAddCourse(k,parts[1],parts[2],Integer.parseInt(parts[3]));
                    if(k>courseCount){
                        courseCount=k;
                    }
                }
                ++courseCount;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static boolean validateCredentials(String name, String pass, HashMap<String, String> data) {
            return data.containsKey(name) && data.get(name).equals(pass);
        }
        
        // Method to display available courses
        public static void displayCourses(HashMap<Integer,Course> courses) {
            for(Map.Entry<Integer, Course> entry:courses.entrySet()){
                int coursenum=entry.getKey();
                Course course =entry.getValue();
                System.out.println(coursenum+". "+course.getCourseName()+" by "+course.getCourseInstructor());
            }
        }
        
        // Method to register a student for a course
        public String registerStudent(Scanner input,HashMap<Integer,Course> courses,Student cStudent){
        String sesh="no sessions";
        int sesh_choice=0;
        System.out.print("\nEnter the number of the course you want to register for: ");
        int choice = input.nextInt();
        input.nextLine(); // Consume newline

        if (choice >= 1 && choice <= courses.size()) {
            Course selectedCourse = courses.get(choice);
            sesh_choice=choice;
            Set<Course> studentCourses = RegisteredCourses.getOrDefault(cStudent.getStudentID(), new HashSet<>());
            studentCourses.add(selectedCourse); // Add selected course to the set
            RegisteredCourses.put(cStudent.getStudentID(), studentCourses); 
            saveRegCourses(cStudent);
            
            System.out.println("You have successfully registered for the course: ");
            System.out.print(selectedCourse.getCourseName()+" by " + selectedCourse.getCourseInstructor());
            sesh=CourseSchedule(sesh_choice);
        } else {
            System.out.println("Invalid course choice.");
        }
        return sesh;
    }

        public void viewRegCourses(Student student) {
            int z=0;
            if (RegisteredCourses.isEmpty()) {
                loadRegCourses(student);
            }
            for(Map.Entry<String ,Set<Course>>entry :RegisteredCourses.entrySet()){
                String k=student.getStudentID();
                if(entry.getKey().equals(k)){
                    Set<Course> courses=entry.getValue();
                    for(Course course:courses){
                        System.out.println(++z+ ". " + course.getCourseName()+" by "+course.getCourseInstructor());
                    }
                }
            }
        }

        public void dropCourse(Scanner input,Student student) {
            input.nextLine();
            System.out.print("\nEnter the name of the course you want to drop: ");
            String choice = input.nextLine();
            input.nextLine(); // Consume newline

            boolean found=false;
            for(Map.Entry<String ,Set<Course>>entry :RegisteredCourses.entrySet()){
                String k=student.getStudentID();
                if(entry.getKey().equals(k)){
                    Set<Course> courses=entry.getValue();
                    for(Course course:courses){
                        if(course.getCourseName().equals(choice)){
                            found=true;
                            DropCourse.put(k,course);
                            CSchedule.remove(k);
                        }
                    }
                    
                }
            }
            if(!found){
                System.out.println("Invalid course choice.");
            }
            else{
            saveRegCourses(student);
            System.out.println("After removal: " + RegisteredCourses); 
            System.out.println("Dropped Course " + "successfully");
            }
        }

        public String CourseSchedule(int choice){
            String Sessions="no sessions";
            switch(choice){
                case 1:
                    Sessions="   OS              DiscreteMath    Web(online)      Networking       IT Fundamentals";
                    break;
                case 2:
                    Sessions="   IT Fundamentals    OS            DiscreteMath    Web(online)      Networking     ";
                    break;
                case 3:
                    Sessions="   Health           Basic chem     Basic Math      Communication    Ethics and Management";
                    break;
                case 4:
                    Sessions="   DiscreteMath       Data Fundamentals       OS             Web(online)      Networking     ";
                case 5:
                    Sessions="   Security Fundamentals    OS        DiscreteStructures    Web(online)      Networking     ";
            }
            return Sessions;
        }
        public static void displayStudents(){
            int i =0;
            for(Map.Entry<String,Student> entry:students.entrySet()){
                
                Student student =entry.getValue();
                System.out.println(++i +". "+student.getName() +" "+student.getlastName() +" StudentID:"+student.getStudentID()+"\n");
            }
        }
        public static Student whoami(String username){
            Student whoamistudent=null;
            for(Map.Entry<String ,Student> entry :students.entrySet()){
                Student student = entry.getValue();
                String name=student.getName()+" "+student.getlastName();
                if(name.equals(username)){
                    whoamistudent=student;
                }
            }
            return whoamistudent;
        }
        protected static void setStudentpass(String key){
            Student student=students.get(key);
            student.setPassword(student);
        }
}
class SemesterModule{
    public static void main(String[] args){
        
        String AdminPass="admin123";
        try (Scanner input = new Scanner(System.in)) {
            int LoginOption=0;
            
            Semester semester =new Semester();
            String sessions="no sessions";
      
            Student currentStudent =new Student();
            Semester.loadLogindata();
            Semester.loadUserdata();
            Semester.loadCourseData(semester);
            Semester.loadCourseSchedule();

            final String WELCOME =
                    "\n-------------------------------" +
                            "\nWELCOME TO THE COURSE SCHEDULER" +
                            "\n-------------------------------" +
                            "\n[0] Exit the program" +
                            "\n[1] Log in as an Admin" +
                            "\n[2] Log in as a  Faculty" +
                            "\n[3] Log in as a  Student" + 
                            "\n[4] Register as a New Student\n";
            final String ADMIN =
                    "\n----------------------------" +
                            "\nYOU ARE LOGGED IN AS ADMIN" +
                            "\n----------------------------"+
                            "\n[0] Exit - to COURSE SCHEDULER" + 
                            "\n[1] Add New Course"+
                            "\n[2] Students in Campus"+
                            "\n[3] Change password\n";
            
            final String STUDENT =
                    "\n----------------------------" +
                            "\nYOU ARE LOGGED IN AS STUDENT" +
                            "\n----------------------------" +
                            "\n[0] Exit - to COURSE SCHEDULER" +
                            "\n[1] View Course Schedule" +
                            "\n[2] View My Course List" +
                            "\n[3] View My Information" +
                            "\n[4] Enroll myself to a Course" +
                            "\n[5] Unenroll myself from a Course\n";

                            //-------------------------------------
                            //          START (AMar)
                            //-------------------------------------
                
            System.out.print(WELCOME);
            System.out.println("Option: ");
            LoginOption =input.nextInt();
            while(LoginOption !=0){
                switch(LoginOption){
                    case 1:
                        System.out.print("Enter password :");
                        String admin_pass=input.next();
                        if(AdminPass.equals(admin_pass)){
                            System.out.print(ADMIN);
                            System.out.println("Option: ");
                            LoginOption=input.nextInt();
                                while(LoginOption !=0){
                                    switch(LoginOption){
                                        case 1:
                                            System.out.println("Enter Course Name: ");
                                            input.nextLine(); 
                                            String name =input.nextLine();
                                            System.out.println("Enter Course Instructor: ");
                                            String lec =input.nextLine();
                                            System.out.println("Enter Student Capacity: ");
                                            int cap =input.nextInt();
                                            semester.adminAddCourse(name,lec,cap);
                                            break;
                                        case 2:
                                            Semester.displayStudents();
                                    }
                                    System.out.print(ADMIN);
                                    System.out.println("Option: ");
                                    LoginOption=input.nextInt();
                                }
                            break;
                        }else{System.out.println("Wrong password");}
                        break;
                    case 2:
                        System.out.print("FACULTY");
                        break;
                    case 3:
                        System.out.println("Enter username:");
                        input.nextLine();
                        String username = input.nextLine();
                        input.nextLine();
                        System.out.println("Enter password:");
                        String password = input.nextLine();
        
                        if (Semester.validateCredentials(username, password,Semester.LoginInfo)) {
                            currentStudent=Semester.whoami(username);
                            System.out.println("Successfully logged in.");
                            System.out.print(STUDENT);
                            System.out.println("Option: ");
                            LoginOption=input.nextInt();
                                while(LoginOption !=0){
                                    switch(LoginOption){
                                        case 1:
                                            sessions=Semester.loadSessions(currentStudent);
                                            currentStudent.CourseSchedule(sessions);
                                            break;
                                        case 2:
                                              semester.viewRegCourses(currentStudent);
                                            break;
                                        case 3:
                                            currentStudent.myInfo();
                                            break;
                                        case 4:
                                            Semester.displayCourses(Semester.courses);
                                            sessions=semester.registerStudent(input, Semester.courses,currentStudent);
                                            Semester.saveCourseSchedule(currentStudent,sessions);
                                            break;
                                        case 5:
                                            semester.viewRegCourses(currentStudent);
                                            semester.dropCourse(input,currentStudent);
                                            break;
                                    }
                                    System.out.print(STUDENT);
                                    System.out.println("Option: ");
                                    LoginOption=input.nextInt();
                                }
                            } else {
                                System.out.println("Invalid username or password.");
                            }
                            break;

                    case 4:
                        System.out.println("TO Register:");
                        System.out.println("Enter your full name ,ID number and Graduation year as the following format");
                        System.out.print("Enter your firstName  Lastname (ID)112340  (Grad..yr)2023 in each line:\n");
                        input.nextLine(); // Consume newline left from previous nextInt()
                        String firstName = input.next();
                        String lastName = input.next();
                        String PersonID = input.next();
                        String Gradyr   = input.next();
                        String key=PersonID+""+Gradyr;
                        Semester.students.put(key,(new Student(firstName,lastName,Integer.parseInt(PersonID),Gradyr)));

                        Semester.setStudentpass(key);

                        System.out.print(STUDENT);
                        input.nextLine();
                        System.out.println("Option: ");
                        LoginOption=input.nextInt();
                            while(LoginOption !=0){
                                switch(LoginOption){
                                    case 1:
                                        sessions=Semester.loadSessions(currentStudent);
                                        currentStudent.CourseSchedule(sessions);
                                        break;
                                    case 2:
                                        Semester.loadRegCourses(currentStudent);
                                        semester.viewRegCourses(currentStudent);
                                        break;
                                    case 3:
                                        currentStudent.myInfo();
                                        break;
                                    case 4:
                                        Semester.displayCourses(Semester.courses);
                                        sessions=semester.registerStudent(input, Semester.courses,currentStudent);
                                        Semester.saveCourseSchedule(currentStudent,sessions);
                                        break;
                                    case 5:
                                        semester.viewRegCourses(currentStudent);
                                        semester.dropCourse(input,currentStudent);
                                        break;
                                }
                                System.out.print(STUDENT);
                                System.out.println("Option: ");
                                LoginOption=input.nextInt();
                            }
                }
                System.out.print(WELCOME);
                System.out.println("Option: ");
                LoginOption =input.nextInt();
            }
        }
        System.out.print("Exiting the program");
    }
}