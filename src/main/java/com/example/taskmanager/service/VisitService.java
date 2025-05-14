package com.example.taskmanager.service;

import com.example.taskmanager.repository.VisitRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VisitService {
    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @PostConstruct
    public void init() {
        log.debug("Initializing Visit Cache");
        visitRepository.deleteAll();
    }

    public synchronized void incrementVisit(String url) {
        visitRepository.incrementVisitCount(url);
    }

    public long getVisitCount(String url) {
        return visitRepository.getVisitCountByUrl(url)
                .orElse(0);
    }
}