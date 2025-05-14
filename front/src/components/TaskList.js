import React from 'react';
import {
    List,
    ListItem,
    ListItemText,
    Checkbox,
    IconButton,
    Typography,
    ListItemSecondaryAction
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { format } from 'date-fns';

const TaskList = ({ tasks, onEdit, onDelete }) => {
    if (tasks.length === 0) {
        return (
            <Typography variant="body1" sx={{ color: 'white', textAlign: 'center' }}>
                No tasks found. Create your first task!
            </Typography>
        );
    }

    return (
        <List sx={{
            backgroundColor: '#1e1e1e',
            borderRadius: 1,
            border: '1px solid #333'
        }}>
            {tasks.map((task) => (
                <ListItem
                    key={task.id}
                    sx={{
                        borderBottom: '1px solid #333',
                        '&:last-child': {
                            borderBottom: 'none'
                        }
                    }}
                >
                    <Checkbox
                        edge="start"
                        sx={{ color: '#ff5500' }}
                    />
                    <ListItemText
                        primary={
                            <Typography variant="body1" sx={{ color: 'white' }}>
                                {task.name}
                            </Typography>
                        }
                        secondary={
                            <>
                                <Typography variant="body2" sx={{ color: 'gray', mt: 0.5 }}>
                                    {task.info}
                                </Typography>
                                <Typography variant="caption" sx={{ color: '#ff5500', display: 'block', mt: 0.5 }}>
                                    Due: {format(new Date(task.deadline), 'PPpp')}
                                </Typography>
                            </>
                        }
                    />
                    <ListItemSecondaryAction>
                        <IconButton
                            edge="end"
                            aria-label="edit"
                            onClick={() => onEdit && onEdit(task)}
                            sx={{ color: '#ff5500' }}
                        >
                            <EditIcon />
                        </IconButton>
                        <IconButton
                            edge="end"
                            aria-label="delete"
                            onClick={() => onDelete && onDelete(task.id)}
                            sx={{ color: '#ff5500' }}
                        >
                            <DeleteIcon />
                        </IconButton>
                    </ListItemSecondaryAction>
                </ListItem>
            ))}
        </List>
    );
};

export default TaskList;