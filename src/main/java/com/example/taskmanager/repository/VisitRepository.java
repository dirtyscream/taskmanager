package com.example.taskmanager.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class VisitRepository {
    private final Map<String, Integer> visitCountMap = new ConcurrentHashMap<>();

    public Optional<Integer> getVisitCountByUrl(String url) {
        return Optional.ofNullable(visitCountMap.get(url));
    }

    public synchronized void incrementVisitCount(String url) {
        int count = visitCountMap.getOrDefault(url, 0) + 1;
        visitCountMap.put(url, count);
    }

    public void deleteAll() {
        visitCountMap.clear();
    }
}