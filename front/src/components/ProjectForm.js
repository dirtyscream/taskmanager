import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button
} from '@mui/material';

const ProjectForm = ({ open, onClose, onSubmit, initialData }) => {
    const [formData, setFormData] = React.useState(
        initialData || {
            name: '',
            description: ''
        }
    );

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
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
                {initialData ? 'Edit Project' : 'Create Project'}
            </DialogTitle>
            <DialogContent sx={{
                backgroundColor: '#1e1e1e',
                paddingTop: '20px !important'
            }}>
                <TextField
                    autoFocus
                    margin="dense"
                    name="name"
                    label="Project Name"
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
                    name="description"
                    label="Description"
                    type="text"
                    fullWidth
                    multiline
                    rows={4}
                    variant="outlined"
                    value={formData.description}
                    onChange={handleChange}
                    sx={{
                        '& .MuiOutlinedInput-root': {
                            '& fieldset': { borderColor: '#555' },
                            '&:hover fieldset': { borderColor: '#ff5500' },
                        },
                        input: { color: '#eee' },
                        label: { color: '#aaa' },
                        textarea: { color: '#eee' }
                    }}
                />
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

export default ProjectForm;