package edu.northeastern.process.serivce;

import edu.northeastern.base.manager.ActorManager;
import edu.northeastern.base.manager.SensorManager;
import edu.northeastern.process.beans.DemoEntity;
import edu.northeastern.process.dao.DemoRepo;
import edu.northeastern.process.sensors.DemoSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Jim Z on 12/3/20 21:09
 */

@Service
public class DemoService {
    @Autowired
    DemoRepo demoRepo;

    @Value("${logging.file.name}")
    private String logfile;

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    public void startProcess() {

        /*
         First init all monitor process
          1. init database table
          2. create sensor for this service
          3. start scheduled database table monitor
          4. start scheduled
         */
        createDemo("This is the sample creation by demo process", "test");
        initSensor("demo");
        listenTable();
        listenLog();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         Demo process procedures:
            1. do random int ( random number from 0-100)
            2. if 66 -> update database
            3. if others -> {
                3.1 do random again
                3.2 log the number
            }
            Sensors function:
            4. listen log if meet number is 88 give alert
            5. continue listen database table if table.success == true give alert
            6. if run step1 100 times then give alert
         */
        int cntRandom = 0;
        for (int i = 0; i < 3000; i++) {
            int random = new Random().nextInt(100);
            if (random == 66) {
                // update the cnt
                DemoEntity o = demoRepo.findByEmail("test");
                int tcnt = o.getCnt();
                if (o.getCnt() == 5) {
                    o.setSuccess(true);
                }
                o.setCnt(o.getCnt() + 1);
                demoRepo.save(o);
            } else {
                // TODO: log the number
                logger.info("random number is **"+random+"**");
            }

            if (++cntRandom == 100) {
                // give alert
                updateInfoToSensor("demo", "call random int 100 times");
                // clear the cnt
                cntRandom = 0;
            }
        }

        // in the end stop the sensor
//        removeSensor("demo");
    }

    private boolean createDemo(String data, String email) {
        demoRepo.save(new DemoEntity(data, email));
        return true;
    }

    private void listenTable() {
        ActorManager.getSensorManager().tell(new SensorManager.Schedule(
                "databasedemo",
                "demo",
                new DemoSensor.Query(demoRepo.findByEmail("test")),
                "do database query every 10 secs",
                "*/10 * * ? * *",
                null,
                TimeZone.getDefault()
        ));
    }

    private void listenLog() {
        ActorManager.getSensorManager().tell(new SensorManager.Schedule(
                "logdemo",
                "demo",
                new DemoSensor.Log(logfile),
                "Run bash command to grep output from logfile",
                "*/1 * * ? * *",
                null,
                TimeZone.getDefault()
        ));
    }

    private void initSensor(String key) {
        ActorManager.getSensorManager().tell(new SensorManager.Add(key, DemoSensor.create()));
    }

    private void updateInfoToSensor(String key, String info) {
        ActorManager.getSensorManager().tell(new SensorManager.Send(key, new DemoSensor.Number(info)));
    }

    private void removeSensor(String key) {
        ActorManager.getSensorManager().tell(new SensorManager.Stop(key));
    }
}
