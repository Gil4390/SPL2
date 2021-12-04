package bgu.spl.mics.application.objects;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }

    private int name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;

    public Student(String name, String department, String degree){

    }

    /**
     * @return the Type by the string type
     * <p>
     * @param type the type
     */
    private Student.Degree FromStringToType(String type){
        switch (type) {
            case ("MSc"):
                return Student.Degree.MSc;
            case ("PhD"):
                return Student.Degree.PhD;
            default:
                return null;
        }
    }

}
