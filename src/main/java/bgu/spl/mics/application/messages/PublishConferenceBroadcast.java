package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.services.GPUService;

import java.util.PriorityQueue;
import java.util.Queue;

public class PublishConferenceBroadcast implements Broadcast {

    private String[] modelNames;

    public PublishConferenceBroadcast(PriorityQueue<String> modelNames) {
        this.modelNames = new String[modelNames.size()];
        for (int i=0; i< modelNames.size(); i++){
            this.modelNames[i] = modelNames.poll();
        }
    }

    @Override
    public void act(GPUService m) {

    }
}
