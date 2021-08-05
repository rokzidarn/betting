package com.paurus.betting.rest;

import com.paurus.betting.service.PersistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persist/")
@Slf4j
public class PersistController {

    @Autowired
    PersistService persistService;

    @RequestMapping(value = "/trigger", method = RequestMethod.GET)
    public ResponseEntity<String> trigger() {

        log.debug("Start of persisting data!");
        persistService.trigger();
        log.debug("End of persisting data!");

        return null;
    }
}
