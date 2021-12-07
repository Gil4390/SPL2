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

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;


    public Student(String name, String department, String degree){
        this.name = name;
        this.department = department;
        this.status = FromStringToType(degree);
        publications = 0;
        papersRead = 0;
    }


    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Degree getStatus() {
        return status;
    }

    public int getPublications() {
        return publications;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setStatus(Degree status) {
        this.status = status;
    }

    public void setPublications(int publications) {
        this.publications = publications;
    }

    public void setPapersRead(int papersRead) {
        this.papersRead = papersRead;
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

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", status=" + status +
                ", publications=" + publications +
                ", papersRead=" + papersRead +
                '}';
    }
}
