import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/projects';

// Получить все задачи проекта
export const getTasks = async (projectId) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/${projectId}/tasks`);
        return response.data;
    } catch (error) {
        console.error(`Error fetching tasks for project ${projectId}:`, error);
        throw error;
    }
};

// Получить задачу по ID
export const getTaskById = async (projectId, taskId) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/${projectId}/tasks/${taskId}`);
        return response.data;
    } catch (error) {
        console.error(`Error fetching task ${taskId} in project ${projectId}:`, error);
        throw error;
    }
};

// Создать новую задачу
export const createTask = async (projectId, taskData) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/${projectId}/tasks`, taskData);
        return response.data;
    } catch (error) {
        console.error(`Error creating task in project ${projectId}:`, error);
        throw error;
    }
};

// Массовое создание задач
export const createTasksBulk = async (projectId, tasksData) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/${projectId}/tasks/bulk`, tasksData);
        return response.data;
    } catch (error) {
        console.error(`Error bulk creating tasks in project ${projectId}:`, error);
        throw error;
    }
};

// Обновить задачу
export const updateTask = async (projectId, taskId, taskData) => {
    try {
        const response = await axios.put(`${API_BASE_URL}/${projectId}/tasks/${taskId}`, taskData);
        return response.data;
    } catch (error) {
        console.error(`Error updating task ${taskId} in project ${projectId}:`, error);
        throw error;
    }
};

// Удалить задачу
export const deleteTask = async (projectId, taskId) => {
    try {
        await axios.delete(`${API_BASE_URL}/${projectId}/tasks/${taskId}`);
        return true;
    } catch (error) {
        console.error(`Error deleting task ${taskId} in project ${projectId}:`, error);
        throw error;
    }
};