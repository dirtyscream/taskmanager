import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Container,
    Box,
    Typography,
    Button,
    CircularProgress,
    Alert,
    IconButton
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import TaskList from '../components/TaskList';
import TaskForm from '../components/TaskForm';
import { getProjectById } from '../api/projects';
import {
    getTasks,
    createTask,
    updateTask,
    deleteTask,
    createTasksBulk
} from '../api/tasks';

const ProjectDetailPage = () => {
    const { projectId } = useParams();
    const navigate = useNavigate();
    const [project, setProject] = useState(null);
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [openForm, setOpenForm] = useState(false);
    const [editingTask, setEditingTask] = useState(null);
    const [openBulkForm, setOpenBulkForm] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const [projectData, tasksData] = await Promise.all([
                    getProjectById(projectId),
                    getTasks(projectId)
                ]);
                setProject(projectData);
                setTasks(tasksData);
                setError(null);
            } catch (err) {
                setError('Failed to load project data');
                console.error('Error fetching project details:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [projectId]);

    const handleCreateTask = async (taskData) => {
        try {
            const newTask = await createTask(projectId, taskData);
            setTasks([...tasks, newTask]);
            setOpenForm(false);
        } catch (err) {
            setError('Failed to create task');
            console.error('Error creating task:', err);
        }
    };

    const handleUpdateTask = async (taskData) => {
        try {
            const updatedTask = await updateTask(projectId, editingTask.id, taskData);
            setTasks(tasks.map(t => t.id === updatedTask.id ? updatedTask : t));
            setEditingTask(null);
            setOpenForm(false);
        } catch (err) {
            setError('Failed to update task');
            console.error('Error updating task:', err);
        }
    };

    const handleDeleteTask = async (taskId) => {
        try {
            await deleteTask(projectId, taskId);
            setTasks(tasks.filter(t => t.id !== taskId));
        } catch (err) {
            setError('Failed to delete task');
            console.error('Error deleting task:', err);
        }
    };

    const handleBulkCreate = async (tasksData) => {
        try {
            const newTasks = await createTasksBulk(projectId, tasksData);
            setTasks([...tasks, ...newTasks]);
            setOpenBulkForm(false);
        } catch (err) {
            setError('Failed to create tasks');
            console.error('Error creating tasks in bulk:', err);
        }
    };

    const handleEditClick = (task) => {
        setEditingTask(task);
        setOpenForm(true);
    };

    const handleFormSubmit = (taskData) => {
        if (editingTask) {
            handleUpdateTask(taskData);
        } else {
            handleCreateTask(taskData);
        }
    };

    if (loading) {
        return (
            <Container maxWidth="lg">
                <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                    <CircularProgress sx={{ color: '#ff5500' }} />
                </Box>
            </Container>
        );
    }

    if (!project) {
        return (
            <Container maxWidth="lg">
                <Alert severity="error">Project not found</Alert>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg">
            <Box sx={{ my: 4 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <IconButton onClick={() => navigate('/')} sx={{ color: '#ff5500', mr: 1 }}>
                        <ArrowBackIcon />
                    </IconButton>
                    <Typography variant="h4" component="h1" sx={{ color: '#ff5500' }}>
                        {project.name}
                    </Typography>
                </Box>

                <Typography variant="body1" sx={{ mb: 4 }}>
                    {project.description}
                </Typography>

                {error && (
                    <Alert severity="error" sx={{ mb: 3 }}>
                        {error}
                    </Alert>
                )}

                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 4 }}>
                    <Typography variant="h5" component="h2">
                        Tasks
                    </Typography>
                    <Box>
                        <Button
                            variant="outlined"
                            onClick={() => setOpenBulkForm(true)}
                            sx={{
                                mr: 2,
                                color: '#ff5500',
                                borderColor: '#ff5500',
                                '&:hover': { borderColor: '#cc4400' }
                            }}
                        >
                            Bulk Add
                        </Button>
                        <Button
                            variant="contained"
                            onClick={() => {
                                setEditingTask(null);
                                setOpenForm(true);
                            }}
                            sx={{
                                backgroundColor: '#ff5500',
                                '&:hover': { backgroundColor: '#cc4400' }
                            }}
                        >
                            New Task
                        </Button>
                    </Box>
                </Box>

                <TaskList
                    tasks={tasks}
                    onEdit={handleEditClick}
                    onDelete={handleDeleteTask}
                />

                <TaskForm
                    open={openForm}
                    onClose={() => setOpenForm(false)}
                    onSubmit={handleFormSubmit}
                    initialData={editingTask}
                />
            </Box>
        </Container>
    );
};

export default ProjectDetailPage;