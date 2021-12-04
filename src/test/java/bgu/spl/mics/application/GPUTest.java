package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GPUTest {

    GPU gpu;
    @BeforeEach
    void setUp() {
        gpu= new GPU("RTX3090", Cluster.getInstance());
        assertFalse(gpu.isReady());
    }

    @Test
    void tick() {
        int time = gpu.getTimeClock();
        gpu.tick();
        assertEquals(time+1, gpu.getTimeClock());
    }

    @Test
    void trainModel() {
    }

    @Test
    void finish() {
    }

    @Test
    void divideDataBatch() {
        int size = gpu.getUnProcessedDataBatch().length;
        assertEquals(size,0);
        Data data = new Data("Text", 2000);
        Model m = new Model("testModel",data, new Student("amit","a","PhD"));
        gpu.setModel(m);
        gpu.DivideDataBatch();
        size = gpu.getUnProcessedDataBatch().length;
        assertEquals(size,2);

        Data data2 = new Data("Text", 2500);
        m=new Model("testModel",data, new Student("amit","a","PhD"));
        gpu=new GPU("RTX3090", Cluster.getInstance());
        gpu.setModel(m);
        gpu.DivideDataBatch();
        size = gpu.getUnProcessedDataBatch().length;
        assertEquals(size,3);
    }

    @Test
    void sendDataBatch() {
        Cluster cluster = Cluster.getInstance();
        int size= cluster.getDataBATCH_ForCPU().size();
        assertEquals(size,0);
        gpu.SendDataBatch();
        size= cluster.getDataBATCH_ForCPU().size();
        assertNotEquals(0,size);
    }

    @Test
    void receiveProcessedData() {
        Data data = new Data("TEXT",7500);
        DataBatch databatch = new DataBatch(1000,data);
        int ProcessingDataBatch_Size = gpu.getProcessingDataBatch().size();
        int processData = gpu.getCountPDB();
        gpu.ReceiveProcessedData(databatch);
        assertTrue(ProcessingDataBatch_Size+1== gpu.getProcessingDataBatch().size());
        assertEquals(processData+1, gpu.getCountPDB());
    }

}