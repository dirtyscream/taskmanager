package com.example.taskmanager.repository;

import com.example.taskmanager.schemas.VisitDTO;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class VisitRepository {
    private final Map<String, VisitDTO> visitMap = new ConcurrentHashMap<>();

    public Optional<VisitDTO> findByUrl(String url) {
        return Optional.ofNullable(visitMap.get(url));
    }

    public synchronized VisitDTO save(VisitDTO visitDto) {
        visitMap.put(visitDto.getUrl(), visitDto);
        return visitDto;
    }

    public void deleteAll() {
        visitMap.clear();
    }
}