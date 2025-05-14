import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import { Link } from 'react-router-dom';

const Navbar = () => {
    return (
        <AppBar position="static" sx={{ backgroundColor: 'black' }}>
            <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1, color: '#ff5500' }}>
                    Task Manager
                </Typography>
                <Button color="inherit" component={Link} to="/" sx={{ color: 'white' }}>
                    Projects
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;