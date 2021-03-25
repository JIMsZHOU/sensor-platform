package edu.northeastern.process.controller;

import edu.northeastern.process.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Jim Z on 12/25/20 17:31
 */

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/add-{userID}", method = RequestMethod.GET)
    public String addUser(@PathVariable String userID) {
        String res = userService.addUser(userID);
        return res;
    }

    @RequestMapping(value = "/user/getall", method = RequestMethod.GET)
    public List<String> getAllUsers() {
        List<String> allUsers = userService.getAllUser();
        return allUsers;
    }

    @RequestMapping(value = "/user/send/{userID}-{message}", method = RequestMethod.GET)
    public String sendMessage(@PathVariable String userID, @PathVariable String message) {
        String res = userService.sendMessage(userID, message);
        return res;
    }

    @RequestMapping(value = "/user/getMails-{userID}/", method = RequestMethod.GET)
    public List<String> getMails(@PathVariable String userID) {
        return userService.getMails(userID);
    }

    @RequestMapping(value = "/user/read-{userID}", method = RequestMethod.GET)
    public String readMails(@PathVariable String userID) {
        userService.readMails(userID);
        return "read mails for user: " + userID;
    }

}
