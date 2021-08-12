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
    public ResponseEntity<Outgoing> calculateTaxByGeneralRate(@RequestBody Incoming request) {
        try {
            //Outgoing response = taxationService.generalByRate(request);
            Outgoing response = taxationService.general(request, "rate");
            if (response.getError() == null) {
                taxationRepository.save(taxationService.createEntity(request, response));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/general/amount", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Outgoing> calculateTaxByGeneralAmount(@RequestBody Incoming request) {
        try {
            //Outgoing response = taxationService.generalByAmount(request);
            Outgoing response = taxationService.general(request, "amount");
            if (response.getError() == null) {
                taxationRepository.save(taxationService.createEntity(request, response));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/winnings/rate", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Outgoing> calculateTaxByWinningsRate(@RequestBody Incoming request) {
        try {
            //Outgoing response = taxationService.winningsByRate(request);
            Outgoing response = taxationService.winnings(request, "rate");
            if (response.getError() == null) {
                taxationRepository.save(taxationService.createEntity(request, response));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/winnings/amount", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Outgoing> calculateTaxByWinningsAmount(@RequestBody Incoming request) {
        try {
            //Outgoing response = taxationService.winningsByAmount(request);
            Outgoing response = taxationService.winnings(request, "amount");
            if (response.getError() == null) {
                taxationRepository.save(taxationService.createEntity(request, response));
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
