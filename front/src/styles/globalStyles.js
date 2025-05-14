import { createTheme } from '@mui/material/styles';

const baseTheme = createTheme();

export const globalStyles = {
    ...baseTheme,
    components: {
        MuiCssBaseline: {
            styleOverrides: {
                body: {
                    backgroundColor: '#121212',
                    color: '#ffffff',
                },
                a: {
                    color: '#ff5500',
                    textDecoration: 'none',
                    '&:hover': {
                        textDecoration: 'underline',
                    },
                },
            },
        },
        MuiButton: {
            styleOverrides: {
                root: {
                    fontWeight: 'bold',
                    textTransform: 'none',
                },
            },
        },
    },
};