package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

public class TrainedBroadcast implements Broadcast {
    private Model model;
    public TrainedBroadcast (Model m){
        model = m;
    }

    public Model getModel() {
        return model;
    }
}
