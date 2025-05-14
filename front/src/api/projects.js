import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/projects';

// Получить все проекты
export const getProjects = async () => {
    try {
        const response = await axios.get(API_BASE_URL);
        return response.data;
    } catch (error) {
        console.error('Error fetching projects:', error);
        throw error;
    }
};

// Получить проект по ID
export const getProjectById = async (id) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error fetching project with ID ${id}:`, error);
        throw error;
    }
};

// Создать новый проект
export const createProject = async (projectData) => {
    try {
        const response = await axios.post(API_BASE_URL, projectData);
        return response.data;
    } catch (error) {
        console.error('Error creating project:', error);
        throw error;
    }
};

// Обновить проект
export const updateProject = async (id, projectData) => {
    try {
        const response = await axios.put(`${API_BASE_URL}/${id}`, projectData);
        return response.data;
    } catch (error) {
        console.error(`Error updating project with ID ${id}:`, error);
        throw error;
    }
};

// Удалить проект
export const deleteProject = async (id) => {
    try {
        await axios.delete(`${API_BASE_URL}/${id}`);
        return true;
    } catch (error) {
        console.error(`Error deleting project with ID ${id}:`, error);
        throw error;
    }
};

// Получить проекты с задачами по имени задачи
export const getProjectsWithTasks = async (taskName) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/info`, {
            params: { taskName }
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching projects with tasks:', error);
        throw error;
    }
};