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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    public void trigger_baseline() {  // basic sequential insert into DB, unsorted
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
    public void trigger_batch() {  // improvement, now inserting in batches (example: 25 entities in one query)
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
    public void trigger_dll() {  // improvement, sorting, using basic doubly linked list
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
    public void trigger_rank() {  // improvement, sorting, using lexorank algorithm
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

            if (next != null && prev != null && prev.getRank() != null && next.getRank() != null) {
                entity.setRank(lexorank(prev.getRank(), next.getRank()));  // calculate rank, defines position, based on rank of previous/next
            } else if (next == null && prev != null && prev.getRank() != null) {
                entity.setRank(prev.getRank() + "0");
            } else {
                entity.setRank("-");
                // TODO: what if a new first occurs, rebalance needed
            }

            persistRepository.save(entity);
        }
    }

    @Override
    public void trigger_thread() {  // improvement, multiple threads working on persisting at the same time
        ExecutorService executor = Executors.newFixedThreadPool(5);

        try (Stream<String> read = Files.lines(Paths.get("data/fo_random.txt"))) {
            Stream<String> stream = read.skip(1);
            Iterator<String> iterator = stream.iterator();

            while (iterator.hasNext()) {
                String[] data = iterator.next().split("\\|");

                Runnable task = () -> {
                    MatchData entity = createEntity(data);
                    persistRepository.save(entity);
                };

                executor.execute(task);
            }

            try {
                System.out.println("Attempt to shutdown executor");
                executor.shutdown();
                executor.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.err.println("Tasks interrupted");
            } finally {
                if (!executor.isTerminated()) {
                    System.err.println("Cancelling non-finished tasks");
                }
                executor.shutdownNow();
                System.out.println("Shutdown finished");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void trigger_final() {  // 302536
        String[] alphanumeric = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy".split("");

        Supplier<Stream<String>> read = () -> {  // use same stream multiple times
            try {
                return Files.lines(Paths.get("data/fo_random.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };

        // initialization, gather some data, first N entities
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
        all.sort(comparator);  // sort entities

        for (int i = 0; i < alphanumeric.length; i++) {
            all.get(i).setRank(alphanumeric[i]);  // now sorted, assign base ranks
        }
        persistRepository.saveAll(all);

        ExecutorService executor = Executors.newFixedThreadPool(5);  // start with multithreading

        Stream<String> stream = read.get().skip(alphanumeric.length + 1);
        Iterator<String> iterator = stream.iterator();
        while (iterator.hasNext()) {
            MatchData entity = createEntity(iterator.next().split("\\|"));
            int index = Collections.binarySearch(all, entity, comparator);
            if (index < 0) {
                index = -index - 1;  // adds to appropriate position
            }
            all.add(index, entity);

            // TODO: improve rank
            MatchData next = null, prev = null;
            if (index > 0) {
                prev = all.get(index - 1);
            }
            if (index < all.size() - 1) {
                next = all.get(index + 1);
            }

            if (next != null && prev != null && prev.getRank() != null && next.getRank() != null) {
                entity.setRank(lexorank(prev.getRank(), next.getRank()));  // calculate rank, defines position, based on rank of previous/next
            } else if (next == null && prev != null && prev.getRank() != null) {
                entity.setRank(prev.getRank() + "0");
            } else {
                entity.setRank("-");
            }

            Runnable task = () -> {
                persistRepository.save(entity);
            };

            executor.execute(task);  // multiple threads persisting data in parallel
        }

        try {
            System.out.println("Attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("Cancelling non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("Shutdown finished");
        }
    }

    /**
     * LexoRank is ranking system that Jira Software uses which provides the ability to rank issues, i.e. lexicographical ordering
     *
     * @param prev - rank of previous element in sorted list
     * @param next - rank of next element in sorted list
     * @return calculated rank
     */
    @Override
    public String lexorank(String prev, String next) {
        int min = 33;
        int max = 126;

        int p = 0;
        int n = 0;
        int pos;
        String str;

        for (pos = 0; p == n; pos++) {
            p = pos < prev.length() ? prev.charAt(pos) : min;
            n = pos < next.length() ? next.charAt(pos) : max;
        }

        str = prev.substring(0, pos - 1);
        if (p == min) {
            while (n == min + 1) {
                n = pos < next.length() ? next.charAt(pos++) : max;
                str += '0';
            }
            if (n == min + 2) {
                str += '0';
                n = max;
            }
        } else if (p + 1 == n) {
            str += Character.toString(Character.toChars(p)[0]);
            n = max;
            while ((p = pos < prev.length() ? prev.charAt(pos++) : min) == max - 1) {
                str += 'z';
            }
        }
        return str + Character.toChars((int) Math.ceil((p + n) / 2))[0];
    }
}
