package com.paurus.betting.service;

import com.paurus.betting.model.MatchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersistService implements IPersistService {

    @Autowired
    private IPersistRepository persistRepository;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public MatchData createEntity(String[] split) {
        return MatchData.builder()
                .matchId(split[0])
                .marketId(Integer.parseInt(split[1]))
                .outcomeId(split[2])
                .specifiers(split.length > 3 ? split[3] : "null")
                .build();
    }

    @Override
    public void trigger_baseline() {
        try (Stream<String> read = Files.lines(Paths.get("data/fo_random_partial.txt"))) {
            Stream<String> stream = read.skip(1);

            stream.forEach(element -> {
                MatchData entity = createEntity(element.split("\\|"));
                persistRepository.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void trigger_batch() {
        int batch_size = 25;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();

        try (Stream<String> read = Files.lines(Paths.get("data/fo_random_partial.txt"))) {
            Stream<String> stream = read.skip(1);
            int i = 0;
            Iterator<String> iterator = stream.iterator();
            tx.begin();

            while (iterator.hasNext()) {
                if (i > 0 && i % batch_size == 0) {
                    tx.commit();
                    tx.begin();

                    entityManager.flush();
                    entityManager.clear();
                }

                MatchData entity = createEntity(iterator.next().split("\\|"));
                entityManager.persist(entity);
                i++;
            }

            tx.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void trigger_dll() {
        try (Stream<String> read = Files.lines(Paths.get("data/fo_random_partial.txt"))) {
            Stream<String> stream = read.skip(1);
            Iterator<String> iterator = stream.iterator();
            List<MatchData> all = new ArrayList<>();

            MatchData first = createEntity(iterator.next().split("\\|"));
            persistRepository.save(first);
            all.add(first);

            while (iterator.hasNext()) {
                MatchData entity = createEntity(iterator.next().split("\\|"));
                all.add(entity);
                MatchData next, prev;
                List<MatchData> updates = new ArrayList<>();

                Comparator<MatchData> comparator = Comparator.comparing(MatchData::getMatchId);
                all.sort(comparator);
                int index = Collections.binarySearch(all, entity, comparator);

                if (index == 0) {  // first
                    next = all.get(index + 1);
                    entity.setNext(next.getMatchId());
                    next.setPrev(entity.getMatchId());
                    updates.add(next);
                } else if (index == all.size() - 1) {  // last
                    prev = all.get(index - 1);
                    entity.setPrev(prev.getMatchId());
                    prev.setNext(entity.getMatchId());
                    updates.add(prev);
                } else {  // between
                    next = all.get(index + 1);
                    entity.setNext(next.getMatchId());
                    next.setPrev(entity.getMatchId());
                    prev = all.get(index - 1);
                    entity.setPrev(prev.getMatchId());
                    prev.setNext(entity.getMatchId());
                    updates.add(next);
                    updates.add(prev);
                }
                updates.add(entity);

                persistRepository.saveAll(updates);
            }

            /*
            for (MatchData tmp : all) {
                System.out.println(tmp.getMatchId());
            }
            */

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void trigger_rank() {
        String[] alphanumeric = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy".split("");

        Supplier<Stream<String>> read = () -> {
            try {
                return Files.lines(Paths.get("data/fo_random_partial.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };

        Stream<String> initStream = read.get().skip(1).limit(alphanumeric.length);

        List<String> initList = initStream.collect(Collectors.toList());
        List<MatchData> all = new ArrayList<>();
        for (String element : initList) {
            all.add(createEntity(element.split("\\|")));
        }

        Comparator<MatchData> comparator = Comparator.comparing(MatchData::getMatchId)
                .thenComparing(MatchData::getMarketId)
                .thenComparing(MatchData::getOutcomeId)
                .thenComparing(MatchData::getSpecifiers);
        all.sort(comparator);

        for (int i = 0; i < alphanumeric.length; i++) {  // TODO: use every 6th char, better balancing?
            all.get(i).setRank(alphanumeric[i]);
        }
        persistRepository.saveAll(all);

        Stream<String> stream = read.get().skip(alphanumeric.length + 1);
        Iterator<String> iterator = stream.iterator();
        while (iterator.hasNext()) {
            MatchData entity = createEntity(iterator.next().split("\\|"));
            all.add(entity);
            all.sort(comparator);
            int index = Collections.binarySearch(all, entity, comparator);

            MatchData next = null, prev = null;
            if (index > 0) {
                prev = all.get(index - 1);
            }
            if (index < all.size() - 1) {
                next = all.get(index + 1);
            }

            if (next != null && prev != null) {
                 entity.setRank(rank(prev.getRank(), next.getRank()));
            } else if (next == null && prev != null) {
                entity.setRank(prev.getRank() + "0");
            } else {
                entity.setRank("-");
                // TODO: what if a new first occurs, rebalance needed
            }

            persistRepository.save(entity);
        }
    }

    @Override
    public String rank(String prevRank, String nextRank) {
        StringBuilder rank = new StringBuilder();
        int i = 0;

        while (true) {
            char prevChar = i >= prevRank.length() ? '0' : prevRank.charAt(i);
            char nextChar = i >= nextRank.length() ? 'z' : nextRank.charAt(i);

            if (prevChar == nextChar) {
                rank.append(prevChar);
                i++;
                continue;
            }

            char midChar = (char) ((prevChar + nextChar) / 2);
            if (midChar == prevChar || midChar == nextChar) {
                rank.append(prevChar);
                i++;
                continue;
            }

            rank.append(midChar);
            break;
        }

        // if (rank.toString().compareTo(nextRank) >= 0) // rebalance needed
        return rank.toString();
    }
}
