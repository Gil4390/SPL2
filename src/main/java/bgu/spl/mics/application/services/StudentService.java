package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Pair;
import bgu.spl.mics.application.objects.Student;

import java.util.Iterator;
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
    private int modelSendCount;
    public StudentService(Student student) {
        super("Student - " + student.getId() + " Service");
        this.student = student;
        modelSendCount=-1;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(PublishConferenceBroadcast.class, this::PublishConferenceBroadcast);
        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast)->{terminate();});
        subscribeBroadcast(TrainedBroadcast.class, (TrainedBroadcast)->sentToTest(TrainedBroadcast.getModel()));
        subscribeBroadcast(TestedBroadcast.class, (TestedBroadcast)->sentToPublish(TestedBroadcast.getModel()));
        act();
    }

    public void act() {
        modelSendCount++;
        if(this.student.getModels().size() > modelSendCount) {
            Model m = this.student.getModels().get(modelSendCount);
            TrainModelEvent trainEvent = new TrainModelEvent(this.student.getId(), m);
            System.out.println("student id:" + student.getId() + ", send TrainModelEvent with model name:" + m.getName());
            sendEvent(trainEvent);
        }
    }

    private boolean checkIfMyModel(Model m)
    {
        return student.getModels().contains(m);
    }

    private void sentToTest(Model model){
        if(checkIfMyModel(model)){
            student.getTrainedModels().add(model);
            System.out.println("student id:" + student.getId() + ", sentToTest with model name:" + model.getName());
            TestModelEvent testEvent = new TestModelEvent(this.student.getId(), model);
            sendEvent(testEvent);
        }
    }

    private void sentToPublish(Model model){
        if(checkIfMyModel(model)) {
            act();
            if(model.getResultString()=="Good")
                PublishResults(model);
        }
    }

    private void PublishResults(Model model){
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
