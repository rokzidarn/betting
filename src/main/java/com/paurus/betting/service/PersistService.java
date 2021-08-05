package com.paurus.betting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class PersistService implements IPersistService {

    @Autowired
    private IPersistRepository persistRepository;

    @Override
    public void trigger() {
        try (Stream<String> stream = Files.lines(Paths.get("data/fo_random_partial.txt"))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
