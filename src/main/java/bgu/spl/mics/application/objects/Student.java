package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.services.StudentService;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }

    private final int id;
    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private StudentService studentService;

    private Queue<Model> models;
    private Queue<Model> TrainedModels;

    public Student(String name, String department, String degree, int id){
        this.name = name;
        this.department = department;
        this.status = FromStringToType(degree);
        publications = 0;
        papersRead = 0;
        this.models = new LinkedList<>();
        this.TrainedModels = new LinkedList<>();
        this.id = id;

        studentService = new StudentService(this);

        Thread studentThread = new Thread(studentService);
        studentThread.start();
    }

    public void act(){
        for (Model m : this.models){
            Model model = TrainModel(m);
            TrainedModels.add(model);
            if(TestModel(model)){
                if(PublishModel(model.getName())){
                    model.setPublished(true);
                }
            }
        }

    }

    public void AddModel(Model m){
        this.models.add(m);
    }

    public Model TrainModel(Model model){
        Model trainedModel = studentService.TrainModel(model);
        return trainedModel;
    }

    public Boolean TestModel(Model model){
        Boolean testedModel = studentService.TestModel(model);
        return testedModel;
    }

    public Boolean PublishModel(String name){
        return studentService.PublishResults(name);
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

    public int getId() {
        return id;
    }

    public Queue<Model> getModels() {
        return models;
    }

    public Queue<Model> getTrainedModels() {
        return TrainedModels;
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
