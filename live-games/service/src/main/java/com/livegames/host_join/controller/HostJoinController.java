package com.livegames.host_join.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
class HostJoinController {

    @GetMapping(value="/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String sample(){
        return "index";
    }
}