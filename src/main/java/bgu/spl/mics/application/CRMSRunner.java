package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import com.google.gson.Gson;

import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Vector<Student> STUDENTS = new Vector<>();
        Vector<GPU> GPUS = new Vector<>();
        Vector<CPU> CPUS = new Vector<>();
        Vector<ConfrenceInformation> CONFERENCES = new Vector<>();
        int TICK_TIME = 0;
        int DURATION = 0;

        String students="";
        String gpus="";
        String cpus="";
        String conferences="";
        String TickTime = "";
        String Duration = "";
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\gil43\\IdeaProjects\\SPL2\\example_input.json"));
            Map<?, ?> map = gson.fromJson(reader, Map.class);
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey().equals("Students"))  students = entry.getValue().toString();
                else if(entry.getKey().equals("GPUS")) gpus = entry.getValue().toString();
                else if(entry.getKey().equals("CPUS")) cpus = entry.getValue().toString();
                else if(entry.getKey().equals("Conferences")) conferences = entry.getValue().toString();
                else if(entry.getKey().equals("TickTime")) TickTime = entry.getValue().toString();
                else if(entry.getKey().equals("Duration")) Duration = entry.getValue().toString();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        students = students.substring(1,students.length()-3);
        for (String str : students.split("]}, ")) {
            String[] str_split1 = str.split("models=");
            String[] str_split = str_split1[0].split(", ");
            String name = str_split[0].substring(6);
            String department = str_split[1].substring(11);
            String status = str_split[2].substring(7);
            Student student = new Student(name, department, status);
            STUDENTS.add(student);
            String[] models = str_split1[1].substring(1, str_split1[1].length()-1).split("}, ");
            for(String m : models){
                String[] m_split = m.split(", ");
                String modelName = m_split[0].substring(6);
                String DataType = m_split[1].substring(5);
                String DataSize = m_split[2].substring(5);
                int DataSizeInt=0;
                if (DataSize.contains("E")) DataSizeInt = new BigDecimal(DataSize).intValue();
                else DataSizeInt = Integer.parseInt(DataSize.substring(0, DataSize.length()-2));

                Data data = new Data(DataType, DataSizeInt);
                Model model = new Model(modelName, data, student);
            }
        }

        gpus = gpus.substring(1,gpus.length()-1);
        for (String str : gpus.split(", ")) {
            GPU gpu = new GPU(str, Cluster.getInstance());
            GPUS.add(gpu);
        }

        cpus = cpus.substring(1,cpus.length()-1);
        for (String str : cpus.split(", ")) {
            CPU cpu = new CPU(Integer.parseInt(str.substring(0,str.length()-2)));
            CPUS.add(cpu);
        }

        conferences = conferences.substring(1,conferences.length()-2);
        for (String str : conferences.split("}, ")) {
            String cName = str.split(", ")[0].substring(6);
            String cDate = str.split(", ")[1].substring(5);
            int cDateInt = Integer.parseInt(cDate.substring(0, cDate.length()-2));
            ConfrenceInformation conference = new ConfrenceInformation(cName, cDateInt);
            CONFERENCES.add(conference);
        }

        TICK_TIME = Integer.parseInt(TickTime.substring(0, TickTime.length()-2));

        DURATION = Integer.parseInt(Duration.substring(0, Duration.length()-2));


        //TODO add cpus gpus to cluster;
        //TODO add ID to cpu

        //TODO Create services and register, subscribe
    }
}
