package com.paurus.betting.rest;

import com.paurus.betting.json.Incoming;
import com.paurus.betting.json.Outgoing;
import com.paurus.betting.service.ITaxationRepository;
import com.paurus.betting.service.TaxationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/taxation/")
public class TaxationController {

    @Autowired
    ITaxationRepository taxationRepository;

    @Autowired
    TaxationService taxationService;

    @RequestMapping(value = "/general/rate", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Outgoing> calculateTaxByGeneralRate(@RequestBody Incoming request, @RequestParam(name = "trader") String trader) {
        try {
            Outgoing response = taxationService.generalByRate(request, trader);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
