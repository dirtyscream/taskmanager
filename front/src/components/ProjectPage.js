import React, { useEffect, useState } from 'react';
import { Container, Box, Button, Grid } from '@mui/material';
import ProjectList from '../components/ProjectList';
import ProjectForm from '../components/ProjectForm';
import { getProjects, createProject } from '../api/projects';

const ProjectsPage = () => {
    const [projects, setProjects] = useState([]);
    const [openForm, setOpenForm] = useState(false);

    useEffect(() => {
        fetchProjects();
    }, []);

    const fetchProjects = async () => {
        try {
            const data = await getProjects();
            setProjects(data);
        } catch (error) {
            console.error('Failed to fetch projects:', error);
        }
    };

    const handleCreateProject = async (projectData) => {
        try {
            const newProject = await createProject(projectData);
            setProjects([...projects, newProject]);
            setOpenForm(false);
        } catch (error) {
            console.error('Failed to create project:', error);
        }
    };

    return (
        <Container maxWidth="lg">
            <Box sx={{ my: 4 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 4 }}>
                    <Typography variant="h3" component="h1">
                        Projects
                    </Typography>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={() => setOpenForm(true)}
                        sx={{ backgroundColor: '#ff5500', '&:hover': { backgroundColor: '#cc4400' } }}
                    >
                        New Project
                    </Button>
                </Box>

                <ProjectList projects={projects} />

                <ProjectForm
                    open={openForm}
                    onClose={() => setOpenForm(false)}
                    onSubmit={handleCreateProject}
                />
            </Box>
        </Container>
    );
};

export default ProjectsPage;