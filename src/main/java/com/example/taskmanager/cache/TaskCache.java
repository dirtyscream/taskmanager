package com.example.taskmanager.cache;

import com.example.taskmanager.models.Task;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaskCache {
    @SuppressWarnings("checkstyle:ConstantName")
    private static final Logger logger = LoggerFactory.getLogger(TaskCache.class);

    private final Map<Long, List<Task>> cache;
    private static final long MAX_CACHE_SIZE = 1_000_000_000L; // 1GB
    private long currentCacheSize = 0;

    public TaskCache() {
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, List<Task>> eldest) {
                if (currentCacheSize > MAX_CACHE_SIZE) {
                    currentCacheSize -= estimateSize(eldest.getValue());
                    logger.info("Removed eldest cache entry for projectId={} due to size limit",
                            eldest.getKey());
                    return true;
                }
                return false;
            }
        });
    }

    public List<Task> getTasks(Long projectId) {
        logger.info("Get from cache for projectId: {}", projectId);
        return cache.get(projectId);
    }

    public void putTasks(Long projectId, List<Task> tasks) {
        cache.put(projectId, tasks);
        long size = estimateSize(tasks);
        currentCacheSize += size;
        logger.info("Cached {} tasks for projectId={} (estimated size: {} bytes)",
                tasks.size(), projectId, size);
    }

    public void invalidate(Long projectId) {
        List<Task> removed = cache.remove(projectId);
        if (removed != null) {
            long size = estimateSize(removed);
            currentCacheSize -= size;
            logger.info("Invalidated cache for projectId={} (removed size: {} bytes)", projectId, size);
        }
    }

    private long estimateSize(List<Task> tasks) {
        return tasks.size() * 200L;
    }
}
