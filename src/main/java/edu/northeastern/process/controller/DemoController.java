package edu.northeastern.process.controller;

import edu.northeastern.process.serivce.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PostLoad;

/**
 * Created by Jim Z on 12/3/20 20:43
 */
@RestController
public class DemoController {

    @Autowired
    DemoService service;

    @RequestMapping(value = "/")
    public ResponseEntity<String> index() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        String ans = "{\"message\":\"Welcome to our REST services\"}";
        return ResponseEntity.ok().body(ans);
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String demoProcess() {
        service.startProcess();
        return "start the demo process";
    }
}
