import bgu.spl.mics.application.objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        gpu=new GPU("RTX2080", Cluster.getInstance());
        Data data = new Data("Text", 20000);
        DataBatch DB1 = new DataBatch(0,data);
        DataBatch DB2 = new DataBatch(1000,data);
        DataBatch DB3 = new DataBatch(2000,data);
        DataBatch DB4 = new DataBatch(3000,data);
        Model m = new Model("testModel",data, new Student("amit","a","PhD"));
        gpu.setModel(m);
        gpu.DivideDataBatch();
        gpu.SendDataBatch();
        gpu.ReceiveProcessedData(DB1);
        gpu.ReceiveProcessedData(DB2);
        gpu.ReceiveProcessedData(DB3);
        gpu.ReceiveProcessedData(DB4);
        gpu.tick();
        assertEquals(4, gpu.getProcessingDataBatch().size());
        assertEquals(0, gpu.getCountPDB());
        gpu.tick();
        assertEquals(0, gpu.getProcessingDataBatch().size());
        assertEquals(4, gpu.getCountPDB());

        gpu.ReceiveProcessedData(DB1);
        gpu.ReceiveProcessedData(DB2);
        gpu.tick();
        assertEquals(2, gpu.getProcessingDataBatch().size());
        assertEquals(4, gpu.getCountPDB());
        gpu.ReceiveProcessedData(DB3);
        gpu.ReceiveProcessedData(DB4);

        assertEquals(4, gpu.getProcessingDataBatch().size());
        assertEquals(4, gpu.getCountPDB());

        gpu.tick();
        assertEquals(2, gpu.getProcessingDataBatch().size());
        assertEquals(6, gpu.getCountPDB());
        gpu.tick();
        assertEquals(0, gpu.getProcessingDataBatch().size());
        assertEquals(8, gpu.getCountPDB());
    }

    @Test
    void finish() {
        Data data = new Data("Text", 2000);
        DataBatch DB = new DataBatch(0,data);
        Model m = new Model("testModel",data, new Student("amit","a","PhD"));
        gpu.setModel(m);
        gpu.DivideDataBatch();
        gpu.SendDataBatch();
        gpu.ReceiveProcessedData(DB);
        for(int i=0; i<4; i++)
            gpu.tick();
        gpu.Finish();
        assertNull(gpu.getModel());
        assertEquals(0,gpu.getProcessingDataBatch().size());
        assertEquals(0,gpu.getUnProcessedDataBatch().length);
        assertEquals(0,gpu.getIndexUPDB());
        assertEquals(0,gpu.getCountPDB());
        assertEquals(0,gpu.getTrainingTime());
        assertTrue(gpu.isReady());
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