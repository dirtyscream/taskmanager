package com.example.taskmanager.cache;

import com.example.taskmanager.models.Task;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class TaskCache {
    private final Map<Long, List<Task>> cache;
    private static final long MAX_CACHE_SIZE = 1_000_000_000L; // 1GB
    private long currentCacheSize = 0;

    public TaskCache() {
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, List<Task>> eldest) {
                if (currentCacheSize > MAX_CACHE_SIZE) {
                    currentCacheSize -= estimateSize(eldest.getValue());
                    return true;
                }
                return false;
            }
        });
    }

    public List<Task> getTasks(Long projectId) {
        return cache.get(projectId);
    }

    public void putTasks(Long projectId, List<Task> tasks) {
        cache.put(projectId, tasks);
        currentCacheSize += estimateSize(tasks);
    }

    public void invalidate(Long projectId) {
        List<Task> removed = cache.remove(projectId);
        if (removed != null) {
            currentCacheSize -= estimateSize(removed);
        }
    }

    private long estimateSize(List<Task> tasks) {
        return tasks.size() * 200L;
    }
}
