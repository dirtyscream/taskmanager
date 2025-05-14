export const theme = {
    palette: {
        primary: {
            main: '#ff5500', // Яркий оранжевый
            dark: '#cc4400',
            contrastText: '#ffffff',
        },
        secondary: {
            main: '#000000', // Черный
            contrastText: '#ffffff',
        },
        background: {
            default: '#121212', // Темный фон
            paper: '#1e1e1e',   // Чуть светлее для карточек
        },
        text: {
            primary: '#ffffff', // Белый текст
            secondary: '#ff5500', // Оранжевый для акцентов
        },
    },
    typography: {
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        h1: {
            color: '#ff5500',
            fontWeight: 700,
        },
        h2: {
            color: '#ff5500',
        },
        // ... остальные стили текста
    },
};