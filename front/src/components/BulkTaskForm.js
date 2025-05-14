import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
    FormHelperText,
    Typography,
    IconButton,
    Box
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import { useFormik } from 'formik';
import * as Yup from 'yup';

const validationSchema = Yup.object({
    tasks: Yup.array().of(
        Yup.object({
            name: Yup.string()
                .required('Task name is required')
                .max(50, 'Task name must be at most 50 characters'),
            info: Yup.string()
                .max(1000, 'Info must be at most 1000 characters'),
            deadline: Yup.date()
                .required('Deadline is required')
                .min(new Date(), 'Deadline must be in the future')
        })
    )
});

const BulkTaskForm = ({ open, onClose, onSubmit }) => {
    const formik = useFormik({
        initialValues: {
            tasks: [
                { name: '', info: '', deadline: new Date() }
            ]
        },
        validationSchema,
        onSubmit: (values) => {
            onSubmit(values.tasks);
            formik.resetForm();
        }
    });

    const handleAddTask = () => {
        formik.setValues({
            tasks: [...formik.values.tasks, { name: '', info: '', deadline: new Date() }]
        });
    };

    const handleRemoveTask = (index) => {
        const newTasks = [...formik.values.tasks];
        newTasks.splice(index, 1);
        formik.setValues({ tasks: newTasks });
    };

    const handleClose = () => {
        formik.resetForm();
        onClose();
    };

    return (
        <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
            <DialogTitle sx={{
                backgroundColor: '#1e1e1e',
                color: '#ff5500',
                borderBottom: '1px solid #333'
            }}>
                Bulk Add Tasks
            </DialogTitle>
            <DialogContent sx={{ backgroundColor: '#1e1e1e' }}>
                <form onSubmit={formik.handleSubmit}>
                    {formik.values.tasks.map((task, index) => (
                        <Box key={index} sx={{
                            mb: 3,
                            p: 2,
                            border: '1px solid #333',
                            borderRadius: 1,
                            position: 'relative'
                        }}>
                            {formik.values.tasks.length > 1 && (
                                <IconButton
                                    onClick={() => handleRemoveTask(index)}
                                    sx={{
                                        position: 'absolute',
                                        top: 8,
                                        right: 8,
                                        color: '#ff5500'
                                    }}
                                >
                                    <RemoveIcon />
                                </IconButton>
                            )}

                            <Typography variant="subtitle1" sx={{ color: '#ff5500', mb: 1 }}>
                                Task {index + 1}
                            </Typography>

                            <TextField
                                margin="dense"
                                name={`tasks[${index}].name`}
                                label="Task Name"
                                type="text"
                                fullWidth
                                variant="outlined"
                                value={task.name}
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                error={
                                    formik.touched.tasks?.[index]?.name &&
                                    Boolean(formik.errors.tasks?.[index]?.name)
                                }
                                sx={{
                                    '& .MuiOutlinedInput-root': {
                                        '& fieldset': { borderColor: '#333' },
                                        '&:hover fieldset': { borderColor: '#ff5500' },
                                    },
                                    input: { color: 'white' },
                                    label: { color: 'gray' }
                                }}
                            />
                            {formik.touched.tasks?.[index]?.name && formik.errors.tasks?.[index]?.name && (
                                <FormHelperText error>
                                    {formik.errors.tasks[index].name}
                                </FormHelperText>
                            )}

                            <TextField
                                margin="dense"
                                name={`tasks[${index}].info`}
                                label="Description"
                                type="text"
                                fullWidth
                                multiline
                                rows={2}
                                variant="outlined"
                                value={task.info}
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                error={
                                    formik.touched.tasks?.[index]?.info &&
                                    Boolean(formik.errors.tasks?.[index]?.info)
                                }
                                sx={{
                                    '& .MuiOutlinedInput-root': {
                                        '& fieldset': { borderColor: '#333' },
                                        '&:hover fieldset': { borderColor: '#ff5500' },
                                    },
                                    input: { color: 'white' },
                                    label: { color: 'gray' },
                                    textarea: { color: 'white' }
                                }}
                            />
                            {formik.touched.tasks?.[index]?.info && formik.errors.tasks?.[index]?.info && (
                                <FormHelperText error>
                                    {formik.errors.tasks[index].info}
                                </FormHelperText>
                            )}
                        </Box>
                    ))}

                    <Button
                        startIcon={<AddIcon />}
                        onClick={handleAddTask}
                        sx={{
                            color: '#ff5500',
                            borderColor: '#ff5500',
                            mt: 1
                        }}
                        variant="outlined"
                    >
                        Add Another Task
                    </Button>
                </form>
            </DialogContent>
            <DialogActions sx={{
                backgroundColor: '#1e1e1e',
                borderTop: '1px solid #333'
            }}>
                <Button onClick={handleClose} sx={{ color: 'white' }}>
                    Cancel
                </Button>
                <Button
                    onClick={() => formik.handleSubmit()}
                    sx={{ color: '#ff5500' }}
                    disabled={!formik.isValid || formik.isSubmitting}
                >
                    Create All Tasks
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default BulkTaskForm;