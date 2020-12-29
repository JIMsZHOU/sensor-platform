package edu.northeastern.process.serivce;

import akka.actor.typed.ActorRef;
import edu.northeastern.base.condition.CommonCondition;
import edu.northeastern.base.condition.DatabaseCondition;
import edu.northeastern.base.event.CommonEvent;
import edu.northeastern.base.event.DatabaseEvent;
import edu.northeastern.base.event.Event;
import edu.northeastern.base.manager.ActorManager;
import edu.northeastern.base.manager.EventManager;
import edu.northeastern.base.manager.PartitionManager;
import edu.northeastern.base.manager.SensorManager;
import edu.northeastern.process.reqres.EventRequest;
import edu.northeastern.process.reqres.UpdateEventRequest;
import edu.northeastern.process.sensors.CommonSensor;
import edu.northeastern.process.sensors.DatabaseSensor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Created by Jim Z on 12/25/20 17:43
 */
@Service
public class EventService {

    private ActorRef<SensorManager.SensorManagerCommand> eventPartition;

    @PostConstruct
    private void init() {
        String eventPartitionName = ActorManager.eventPartitionName;
        eventPartition = PartitionManager.getInstance().getPartition(eventPartitionName);
        while (eventPartition == null) {
            eventPartition = PartitionManager.getInstance().getPartition(eventPartitionName);
        }
    }

    /**
     * logic for create event by type
     * @param req
     * @return
     */
    public String createEvent(EventRequest req) {
        switch (req.getType()) {
            case "common" :
                return createCommonEvent(req);
            case "database" :
                return createDatabaseEvent(req);
            default:
                return "Event type is un-noticed";
        }
    }


    public String createCommonEvent(EventRequest request) {
        CommonEvent event = new CommonEvent(
                request.getKey(),
                request.getLevel(),
                request.getConditions().stream().map(pairs -> new CommonCondition(pairs.getK(), pairs.getV())).collect(Collectors.toList()),
                request.getToWhom(),
                request.getExpireTime(),
                request.getMessage()
        );
        Event e = EventManager.getInstance().addEvent(event);
        String key = request.getKey();
        if (e == null) {
            return "Event key is already exist";
        } else {
            eventPartition.tell(new SensorManager.Add(key, CommonSensor.create(key)));
            return "Success create updatable common event with key:" + key;
        }
    }

    public String createDatabaseEvent(EventRequest request) {
        DatabaseEvent event = new DatabaseEvent(
                request.getKey(),
                request.getLevel(),
                request.getExpireTime(),
                request.getToWhom(),
                request.getUrl(),
                request.getQuery(),
                request.getConditions().stream().map(pairs -> new DatabaseCondition(pairs.getK(), pairs.getV())).collect(Collectors.toList())
        );
        Event e = EventManager.getInstance().addEvent(event);
        String key = request.getKey();
        if (e == null) {
            return "Event key is already exist";
        } else {
            eventPartition.tell(new SensorManager.Add(key, DatabaseSensor.create(key)));
            eventPartition.tell(new SensorManager.Schedule(
                    key + "-schedule",
                    key,
                    new DatabaseSensor.Matching(),
                    "Check database table",
                    request.getCronExpression(),
                    null,
                    TimeZone.getDefault()
            ));
            return "Success create scheduled database event with key:" + key;
        }
    }

    public String deleteEvent(String eventID) {
        Event e = EventManager.getInstance().removeEvent(eventID);
        if (e == null) {
            return "Event with key:" + eventID + " doesn't exist";
        } else {
            eventPartition.tell(new SensorManager.Stop(eventID));
            eventPartition.tell(new SensorManager.CancelSchedule(eventID+ "-schedule"));
            return "Successfully stop event:" + eventID;
        }
    }

    public String updateCommonEvent(UpdateEventRequest updateEventRequest) {
        String eventID = updateEventRequest.getName();
        Event e = EventManager.getInstance().getEvent(eventID);
        if (e == null) {
            return "Event with key:" + eventID + " doesn't exist";
        } else {
            List<CommonCondition> conditions =  updateEventRequest.getCondition().stream().map(pairs -> new CommonCondition(pairs.getK(), pairs.getV())).collect(Collectors.toList());
            for (CommonCondition condition : conditions) {
                eventPartition.tell(new SensorManager.Send(eventID, new CommonSensor.Update(condition)));
            }
            return "Finished update conditions to listener";
        }
    }
}
