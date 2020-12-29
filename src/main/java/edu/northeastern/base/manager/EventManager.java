package edu.northeastern.base.manager;

import edu.northeastern.base.event.Event;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Jim Z on 12/27/20 14:25
 */
public class EventManager {

    private Map<String, Event> events;

    private static EventManager instance;
    private EventManager() {
        this.events = new Hashtable<>();
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void checkExpiredEvents() {
        Date cur = new Date();
        for (String key : this.events.keySet()) {
            Event e = events.get(key);
            if (e.getExpireTime().before(cur)) {
                removeEvent(e.getKey());
                ActorManager.getAlertSensor().tell(new AlertSensor.Alert(
                        "Event: "+ e.getKey() +" is expired"
                ));
            }
        }
    }

    public void giveFinalNotice() {

    }

    public Event getEvent(String key) {
        return this.events.get(key);
    }

    public Event removeEvent(String key) {
        return this.events.remove(key);
    }

    public Event addEvent(Event event) {
        String key = event.getKey();
        if (this.events.containsKey(key)) {
            return null;
        }
        this.events.put(event.getKey(), event);
        return event;
    }
}
