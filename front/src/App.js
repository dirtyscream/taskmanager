import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { globalStyles } from './styles/globalStyles';
import ProjectsPage from './pages/ProjectPage';
import ProjectDetailPage from './pages/ProjectDetailPage';
import Navbar from './components/Navbar';

const theme = createTheme(globalStyles);

function App() {
  return (
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Router>
          <Navbar />
          <Routes>
            <Route path="/" element={<ProjectsPage />} />
            <Route path="/projects/:projectId" element={<ProjectDetailPage />} />
          </Routes>
        </Router>
      </ThemeProvider>
  );
}

export default App;