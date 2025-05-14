import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    Box
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';

const TaskForm = ({ open, onClose, onSubmit, initialData }) => {
    const [formData, setFormData] = React.useState(
        initialData || {
            name: '',
            info: '',
            deadline: new Date()
        }
    );

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleDateChange = (date) => {
        setFormData({ ...formData, deadline: date });
    };

    const handleSubmit = () => {
        onSubmit(formData);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle sx={{
                backgroundColor: '#1e1e1e',
                color: '#ff5500',
                borderBottom: '1px solid #333'
            }}>
                {initialData ? 'Edit Task' : 'Create Task'}
            </DialogTitle>
            <DialogContent sx={{
                backgroundColor: '#1e1e1e',
                paddingTop: '20px !important'
            }}>
                <TextField
                    autoFocus
                    margin="dense"
                    name="name"
                    label="Task Name"
                    type="text"
                    fullWidth
                    variant="outlined"
                    value={formData.name}
                    onChange={handleChange}
                    sx={{
                        '& .MuiOutlinedInput-root': {
                            '& fieldset': { borderColor: '#555' },
                            '&:hover fieldset': { borderColor: '#ff5500' },
                        },
                        input: { color: '#eee' },
                        label: { color: '#aaa' },
                        mb: 2
                    }}
                />
                <TextField
                    margin="dense"
                    name="info"
                    label="Description"
                    type="text"
                    fullWidth
                    multiline
                    rows={3}
                    variant="outlined"
                    value={formData.info}
                    onChange={handleChange}
                    sx={{
                        '& .MuiOutlinedInput-root': {
                            '& fieldset': { borderColor: '#555' },
                            '&:hover fieldset': { borderColor: '#ff5500' },
                        },
                        input: { color: '#eee' },
                        label: { color: '#aaa' },
                        textarea: { color: '#eee' },
                        mb: 2
                    }}
                />
                <Box sx={{ mt: 2 }}>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DateTimePicker
                            label="Deadline"
                            value={formData.deadline}
                            onChange={handleDateChange}
                            slotProps={{
                                textField: {
                                    variant: 'outlined',
                                    InputProps: {
                                        sx: {
                                            color: '#eee',
                                            '& .MuiOutlinedInput-notchedOutline': {
                                                borderColor: '#555', // светло-серая рамка
                                            },
                                            '&:hover .MuiOutlinedInput-notchedOutline': {
                                                borderColor: '#ff5500',
                                            },
                                            '& .MuiSvgIcon-root': {
                                                color: '#fff', // иконка календаря
                                            }
                                        },
                                    },
                                    InputLabelProps: {
                                        sx: {
                                            color: '#aaa',
                                        }
                                    }
                                },
                                popper: {
                                    sx: {
                                        '& .MuiPaper-root': {
                                            backgroundColor: '#1e1e1e',
                                            color: '#eee',
                                            '& .MuiPickersCalendarHeader-label': {
                                                color: '#eee'
                                            },
                                            '& .MuiTypography-root': {
                                                color: '#aaa'
                                            },
                                            '& .MuiPickersDay-root': {
                                                color: '#eee',
                                                '&.Mui-selected': {
                                                    backgroundColor: '#ff5500'
                                                }
                                            }
                                        }
                                    }
                                }
                            }}
                        />

                    </LocalizationProvider>
                </Box>
            </DialogContent>
            <DialogActions sx={{
                backgroundColor: '#1e1e1e',
                borderTop: '1px solid #333'
            }}>
                <Button onClick={onClose} sx={{ color: '#aaa' }}>
                    Cancel
                </Button>
                <Button onClick={handleSubmit} sx={{ color: '#ff5500' }}>
                    {initialData ? 'Update' : 'Create'}
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default TaskForm;