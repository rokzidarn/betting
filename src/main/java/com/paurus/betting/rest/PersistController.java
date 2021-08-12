package com.paurus.betting.rest;

import com.paurus.betting.service.PersistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/trigger/rank", method = RequestMethod.GET)
    public ResponseEntity<String> trigger_rank() {

        log.debug("Start of persisting data!");
        long start = System.currentTimeMillis();
        persistService.trigger_rank();
        long end = System.currentTimeMillis();
        log.debug("End of persisting data!");

        return new ResponseEntity<>("Time elapsed: " + (end - start) + " ms", HttpStatus.OK);
    }

    @RequestMapping(value = "/trigger/thread", method = RequestMethod.GET)
    public ResponseEntity<String> trigger_thread() {

        log.debug("Start of persisting data!");
        long start = System.currentTimeMillis();
        persistService.trigger_thread();
        long end = System.currentTimeMillis();
        log.debug("End of persisting data!");

        return new ResponseEntity<>("Time elapsed: " + (end - start) + " ms", HttpStatus.OK);
    }

    @RequestMapping(value = "/trigger/final", method = RequestMethod.GET)
    public ResponseEntity<String> trigger_final() {

        log.debug("Start of persisting data!");
        long start = System.currentTimeMillis();
        persistService.trigger_final();
        long end = System.currentTimeMillis();
        log.debug("End of persisting data!");

        return new ResponseEntity<>("Time elapsed: " + (end - start) + " ms", HttpStatus.OK);
    }

    @RequestMapping(value = "/trigger/rank/test", method = RequestMethod.GET)
    public ResponseEntity<String> rank_test(@RequestParam(name = "p") String prevRank, @RequestParam(name = "n") String nextRank) {
        String r = persistService.rank(prevRank, nextRank);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
