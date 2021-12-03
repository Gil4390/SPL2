package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {

    CPU cpu;
    @BeforeEach
    void setUp() {
        cpu = new CPU(8,new Cluster());
    }

    @Test
    void receiveUnProcessedData() {
        assertNull(cpu.databatch);
        DataBatch data = new DataBatch();
        cpu.ReceiveUnProcessedData(data);
        assertEquals(cpu.databatch, data);
    }

    @Test
    void tickAndCompute() {

        for (int i = 0; i < cpu.endProcessedTime; i++) {
            int time = cpu.processedTime;
            cpu.tickAndCompute();
            assertEquals(time+1, cpu.processedTime);
            assertFalse(cpu.isReady());
        }
        assertTrue(cpu.isReady());
        assertNull(cpu.databatch);
        assertEquals(0, cpu.endProcessedTime);

    }

    @Test
    void isReady() {
        if(cpu.databatch == null){
            assertFalse(cpu.isReady());
        }
        else{
            assertTrue(cpu.isReady());
        }


    }
}