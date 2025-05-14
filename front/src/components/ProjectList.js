import React from 'react';
import {
    Card,
    CardContent,
    Typography,
    Button,
    Grid,
    IconButton,
    Box
} from '@mui/material';
import { Link } from 'react-router-dom';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const ProjectList = ({ projects, onEdit, onDelete, onClick }) => {
    if (projects.length === 0) {
        return (
            <Typography variant="body1" sx={{ color: 'white', textAlign: 'center' }}>
                No projects found. Create your first project!
            </Typography>
        );
    }

    return (
        <Grid container spacing={3}>
            {projects.map((project) => (
                <Grid item xs={12} sm={6} md={4} key={project.id}>
                    <Card sx={{
                        backgroundColor: '#1e1e1e',
                        color: 'white',
                        height: '100%',
                        display: 'flex',
                        flexDirection: 'column'
                    }}>
                        <CardContent sx={{ flexGrow: 1 }}>
                            <Typography variant="h5" component="div" sx={{ color: '#ff5500' }}>
                                {project.name}
                            </Typography>
                            <Typography variant="body2" sx={{ mt: 1, mb: 2 }}>
                                {project.description || 'No description'}
                            </Typography>
                        </CardContent>
                        <Box sx={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            p: 2,
                            borderTop: '1px solid #333'
                        }}>
                            <Button
                                component={Link}
                                to={`/projects/${project.id}`}
                                variant="outlined"
                                size="small"
                                sx={{
                                    color: '#ff5500',
                                    borderColor: '#ff5500',
                                    '&:hover': { borderColor: '#cc4400' }
                                }}
                                onClick={() => onClick && onClick(project.id)}
                            >
                                View Tasks
                            </Button>
                            <Box>
                                <IconButton
                                    aria-label="edit"
                                    size="small"
                                    onClick={() => onEdit && onEdit(project)}
                                    sx={{ color: '#ff5500' }}
                                >
                                    <EditIcon fontSize="small" />
                                </IconButton>
                                <IconButton
                                    aria-label="delete"
                                    size="small"
                                    onClick={() => onDelete && onDelete(project.id)}
                                    sx={{ color: '#ff5500' }}
                                >
                                    <DeleteIcon fontSize="small" />
                                </IconButton>
                            </Box>
                        </Box>
                    </Card>
                </Grid>
            ))}
        </Grid>
    );
};

export default ProjectList;