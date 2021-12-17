package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Pair;
import bgu.spl.mics.application.objects.Student;

import java.util.concurrent.TimeUnit;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private Student student;
    Thread thread;
    private boolean terminated;

    public StudentService(Student student) {
        super("Student - " + student.getId() + " Service");
        this.student = student;
        thread = new Thread( ()->activateModels());
        terminated=false;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(PublishConferenceBroadcast.class, this::PublishConferenceBroadcast);
        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast)->{terminated=true; terminate();});
        act();
    }

    public void act() {
        thread.start();
    }

    public void activateModels(){
        for (Model m : this.student.getModels()) {
            Model model = TrainModel(m);
            if(model!=null & !terminated){
                this.student.getTrainedModels().add(model);
                try {
                    if (!terminated && TestModel(model)) {
                        PublishResults(model);
                    }
                }
                catch (NullPointerException e){
                    System.out.println("Model return null for this micro service: "+getName() + "in methode: activateModels");
                };
                System.out.println("student: " + student.getId() + ", model:" + m.getName() + ", was tested and return the result: " + model.getResultString());
            }
            else{
                System.out.println("Model return null for this micro service: "+getName() + " in methode: activateModels");
            }
        }
    }

    public Model TrainModel(Model model){
        if(model != null) {
            System.out.println("student id:" + student.getId() + ", send TrainModel event with model name:" + model.getName());
            TrainModelEvent trainEvent = new TrainModelEvent(this.student.getId(), model);
            Model returnModel =null;
            while(returnModel==null & !terminated)
                returnModel= sendEvent(trainEvent).get(100, TimeUnit.MILLISECONDS);
            return returnModel;
        }
        System.out.println("Model return null for this micro service: "+getName() + "in methode: TrainModel");
        return null;
    }

    public Boolean TestModel(Model model){
        if(model != null){
            System.out.println("student id:"+student.getId()+", send TestModel event with model name:"+model.getName());
            TestModelEvent testEvent = new TestModelEvent(this.student.getId(), model);
            return sendEvent(testEvent).get();
        }
        System.out.println("Model return null for this micro service: "+getName() + "in methode: TestModel");
        return false;
    }

    public void PublishResults(Model model){
        if(model != null){
            System.out.println("student id:" + student.getId() + ", send PublishResults event with model name:" + model.getName());
            PublishResultsEvent publishEvent = new PublishResultsEvent(this.student.getId(), model.getName());
            sendEvent(publishEvent);
        }
        else
            System.out.println("Model return null for this micro service: "+getName() + "in methode: PublishResults");
    }

    private void PublishConferenceBroadcast(PublishConferenceBroadcast event){
        System.out.println("the student id: " + student.getId() +",got an PublishConferenceBroadcast");
        for (Pair<String,Integer> pair:event.getModels()) {
            if(student.getId()==pair.getSecond())
                student.setPublications(student.getPublications()+1, pair.getFirst());
            else{
                student.setPapersRead(student.getPapersRead()+1);
            }
        }
    }
}
