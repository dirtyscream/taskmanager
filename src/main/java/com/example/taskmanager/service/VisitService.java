package com.example.taskmanager.service;

import com.example.taskmanager.repository.VisitRepository;
import com.example.taskmanager.schemas.VisitDTO;
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
        VisitDTO visitDto = visitRepository.findByUrl(url)
                .orElseGet(() -> {
                    VisitDTO newVisitDto = new VisitDTO();
                    newVisitDto.setUrl(url);
                    newVisitDto.setVisitCount(0);
                    return newVisitDto;
                });
        visitDto.setVisitCount(visitDto.getVisitCount() + 1);
        visitRepository.save(visitDto);
    }

    public long getVisitCount(String url) {
        return visitRepository.findByUrl(url)
                .map(VisitDTO::getVisitCount)
                .orElse(0L);
    }
}