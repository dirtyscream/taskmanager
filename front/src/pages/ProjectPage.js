import React, { useEffect, useState } from 'react';
import { Container, Box, Button, Typography, CircularProgress, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import ProjectList from '../components/ProjectList';
import ProjectForm from '../components/ProjectForm';
import {
    getProjects,
    createProject,
    updateProject,
    deleteProject
} from '../api/projects';

const ProjectsPage = () => {
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [openForm, setOpenForm] = useState(false);
    const [editingProject, setEditingProject] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchProjects();
    }, []);

    const fetchProjects = async () => {
        try {
            setLoading(true);
            const data = await getProjects();
            setProjects(data);
            setError(null);
        } catch (err) {
            setError('Failed to load projects. Please try again later.');
            console.error('Error fetching projects:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleCreateProject = async (projectData) => {
        try {
            const newProject = await createProject(projectData);
            setProjects([...projects, newProject]);
            setOpenForm(false);
        } catch (err) {
            setError('Failed to create project');
            console.error('Error creating project:', err);
        }
    };

    const handleUpdateProject = async (projectData) => {
        try {
            const updatedProject = await updateProject(editingProject.id, projectData);
            setProjects(projects.map(p => p.id === updatedProject.id ? updatedProject : p));
            setEditingProject(null);
            setOpenForm(false);
        } catch (err) {
            setError('Failed to update project');
            console.error('Error updating project:', err);
        }
    };

    const handleDeleteProject = async (projectId) => {
        try {
            await deleteProject(projectId);
            setProjects(projects.filter(p => p.id !== projectId));
        } catch (err) {
            setError('Failed to delete project');
            console.error('Error deleting project:', err);
        }
    };

    const handleEditClick = (project) => {
        setEditingProject(project);
        setOpenForm(true);
    };

    const handleFormSubmit = (projectData) => {
        if (editingProject) {
            handleUpdateProject(projectData);
        } else {
            handleCreateProject(projectData);
        }
    };

    const handleProjectClick = (projectId) => {
        navigate(`/projects/${projectId}`);
    };

    return (
        <Container maxWidth="lg">
            <Box sx={{ my: 4 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 4 }}>
                    <Typography variant="h3" component="h1" sx={{ color: '#ff5500' }}>
                        Projects
                    </Typography>
                    <Button
                        variant="contained"
                        onClick={() => {
                            setEditingProject(null);
                            setOpenForm(true);
                        }}
                        sx={{
                            backgroundColor: '#ff5500',
                            '&:hover': { backgroundColor: '#cc4400' }
                        }}
                    >
                        New Project
                    </Button>
                </Box>

                {error && (
                    <Alert severity="error" sx={{ mb: 3 }}>
                        {error}
                    </Alert>
                )}

                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center' }}>
                        <CircularProgress sx={{ color: '#ff5500' }} />
                    </Box>
                ) : (
                    <ProjectList
                        projects={projects}
                        onEdit={handleEditClick}
                        onDelete={handleDeleteProject}
                        onClick={handleProjectClick}
                    />
                )}

                <ProjectForm
                    open={openForm}
                    onClose={() => setOpenForm(false)}
                    onSubmit={handleFormSubmit}
                    initialData={editingProject}
                />
            </Box>
        </Container>
    );
};

export default ProjectsPage;