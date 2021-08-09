package com.paurus.betting.rest;

import com.paurus.betting.service.PersistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value = "/trigger/baseline", method = RequestMethod.GET)
    public ResponseEntity<String> trigger_baseline() {

        log.debug("Start of persisting data!");
        long start = System.currentTimeMillis();
        persistService.trigger_baseline();
        long end = System.currentTimeMillis();
        log.debug("End of persisting data!");

        return new ResponseEntity<>("Time elapsed: " + (end - start) + " ms", HttpStatus.OK);
    }

    @RequestMapping(value = "/trigger/batch", method = RequestMethod.GET)
    public ResponseEntity<String> trigger_batch() {

        log.debug("Start of persisting data!");
        long start = System.currentTimeMillis();
        persistService.trigger_batch();
        long end = System.currentTimeMillis();
        log.debug("End of persisting data!");

        return new ResponseEntity<>("Time elapsed: " + (end - start) + " ms", HttpStatus.OK);
    }

    @RequestMapping(value = "/trigger/dll", method = RequestMethod.GET)
    public ResponseEntity<String> trigger_dll() {

        log.debug("Start of persisting data!");
        long start = System.currentTimeMillis();
        persistService.trigger_dll();
        long end = System.currentTimeMillis();
        log.debug("End of persisting data!");

        return new ResponseEntity<>("Time elapsed: " + (end - start) + " ms", HttpStatus.OK);
    }
}
