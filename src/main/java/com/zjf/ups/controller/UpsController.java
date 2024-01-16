package com.zjf.ups.controller;

import com.zjf.ups.common.RestResult;
import com.zjf.ups.service.UpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ups")
public class UpsController {

    @Autowired
    private UpsService upsService;

    @GetMapping("/onlineStatusChecker")
    public RestResult<String> onlineStatusChecker() throws Exception {
        upsService.OnlineStatusChecker();
        return new RestResult<String>();
    }
}
