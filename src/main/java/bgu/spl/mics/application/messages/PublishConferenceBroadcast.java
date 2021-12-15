package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Pair;
import bgu.spl.mics.application.services.GPUService;

import java.util.PriorityQueue;
import java.util.Queue;

public class PublishConferenceBroadcast implements Broadcast {

    private Pair<Model,Integer>[] models;

    public PublishConferenceBroadcast(Queue<Pair<Model,Integer>> models) {
        this.models = new Pair[models.size()];
        for (int i=0; i< models.size(); i++){
            this.models[i] = models.poll();
        }
    }

    public Pair<Model, Integer>[] getModels() {
        return models;
    }

}
