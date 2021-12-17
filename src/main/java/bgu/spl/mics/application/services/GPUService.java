package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Pair;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;
    private Queue<Pair<TrainModelEvent,Integer>> TrainModelEventQueue;
    private Queue<Pair<TestModelEvent,Integer>> TestModelEventQueue;
    int clock;
    private Event currentEvent;

    public GPUService(GPU gpu) {
        super("GPU - " + (gpu.getId()) + " Service");
        this.gpu = gpu;
        TrainModelEventQueue = new LinkedList<>();
        TestModelEventQueue = new LinkedList<>();
        clock=0;
        currentEvent=null;
    }

    @Override
    protected void initialize() {
        subscribeEvent(TrainModelEvent.class, (TrainModelEvent)->{TrainModelEvent(TrainModelEvent);});
        subscribeEvent(TestModelEvent.class, (TestModelEvent)->{TestModel(TestModelEvent);});
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{tick();});
        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast)->{complete(currentEvent,null) ;terminate();});
    }

    private void tick(){
        clock++;
        gpu.tick();
        if(gpu.isReady()) {
            if(gpu.isFinishTrainModel())
                complete(currentEvent,gpu.getModel());
            nextEvent();
        }
    }

    private void TrainModelEvent(TrainModelEvent event){
        if(!gpu.isReady()) {
            Pair <TrainModelEvent,Integer> pair = new Pair(event,clock);
            TrainModelEventQueue.add(pair);
        }
        else {
            currentEvent=event;
            gpu.TrainModelEvent(event.getModel());
        }
    }

    private void TestModel(TestModelEvent event){
        if(!gpu.isReady()){
            Pair <TestModelEvent,Integer> pair = new Pair(event,clock);
            TestModelEventQueue.add(pair);
        }
        else {
            currentEvent=event;
            gpu.TestModel(event.getModel());
            System.out.println("TestModelEvent, from GPU:"+ gpu.getId()+"  , the test from model name:"+event.getModel().getName()+" , test result:"+event.getModel().getResultString());
            if(event.getModel().getResultString() == "Good")
                complete(event,true);
            else{
                complete(event,false);
            }
            nextEvent();
        }
    }

    public void nextEvent(){
        if(TrainModelEventQueue.isEmpty() && !TestModelEventQueue.isEmpty())
            TestModel(TestModelEventQueue.poll().getFirst());
        else if(!TrainModelEventQueue.isEmpty() && TestModelEventQueue.isEmpty())
            TrainModelEvent(TrainModelEventQueue.poll().getFirst());
        else if(!TrainModelEventQueue.isEmpty() && (TrainModelEventQueue.peek().getSecond() <= TestModelEventQueue.peek().getSecond()))
            TrainModelEvent(TrainModelEventQueue.poll().getFirst());
        else if(!TestModelEventQueue.isEmpty())
            TestModel(TestModelEventQueue.poll().getFirst());
    }

    public GPU getGpu() {
        return gpu;
    }
}
