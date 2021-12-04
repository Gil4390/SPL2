package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GPUTest {

    GPU gpu;
    @BeforeEach
    void setUp() {
        gpu= new GPU("RTX3090", new Cluster());
        assertFalse(gpu.isReady());
    }

    @Test
    void initialize() {
        //assertFalse(gpu.getCallback()==callback)
        assertTrue(gpu.isReady());
    }

    @Test
    void tick() {
        int time = gpu.getTimeClock();
        gpu.tick();
        assertEquals(time+1, gpu.getTimeClock());
    }

    @Test
    void reciveModel() {
        Model m = new Model();
        gpu.ReciveModel(m);
        assertTrue(gpu.getModel()==m);
    }

    @Test
    void receiveProcessedData() {
        DataBatch databatch = new DataBatch();
        int ProcessingDataBatch_Size = gpu.getProcessingDataBatch().size();
        gpu.ReceiveProcessedData(databatch);
        assertTrue(ProcessingDataBatch_Size+1== gpu.getProcessingDataBatch().size());
    }
}