package edu.northeastern.process.controller;

import edu.northeastern.base.event.CommonEvent;
import edu.northeastern.process.reqres.EventRequest;
import edu.northeastern.process.reqres.UpdateEventRequest;
import edu.northeastern.process.serivce.EventService;
import edu.northeastern.process.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jim Z on 12/25/20 17:40
 */
@RestController
public class EventController {

    @Autowired
    EventService eventService;

    @RequestMapping(value = "/event/testJson", method = RequestMethod.POST)
    public EventRequest testJson(@RequestBody EventRequest req) {
        return req;
    }

    @RequestMapping(value = "/event/add", method = RequestMethod.POST)
    public String createEvent(@RequestBody EventRequest req) {
        String res = eventService.createEvent(req);
        return res;
    }

    @RequestMapping(value = "/event/delete/{eventID}", method = RequestMethod.GET)
    public String deleteEvent(@PathVariable String eventID) {
        String res = eventService.deleteEvent(eventID);
        return res;
    }

    @RequestMapping(value = "/event/update", method = RequestMethod.POST)
    public String updateEvent(@RequestBody UpdateEventRequest updateEventRequest) {
        String res = eventService.updateCommonEvent(updateEventRequest);
        return res;
    }
}
