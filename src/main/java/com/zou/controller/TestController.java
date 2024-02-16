package com.zou.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
public class TestController {

    @GetMapping("/info")
    public ResponseEntity<String> getInfo() throws Exception {
        System.out.println("getInfo start---->");
        InetAddress localhost = InetAddress.getLocalHost();
        String hostname = localhost.getHostAddress();
        System.out.println("当前 IP 地址: " + hostname);
        Thread.sleep(2000);
        System.out.println("getInfo end---->");
        return ResponseEntity.ok("getInfo");
    }
}
