import { createTheme } from '@mui/material/styles';

const SF_FONT_FAMILY = '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif';

export const theme = createTheme({
  typography: {
    fontFamily: SF_FONT_FAMILY, 
    
    h1: {
        fontFamily: SF_FONT_FAMILY,
    },
  },
  components: {
    MuiAppBar: {
      styleOverrides: {
        root: {
          fontFamily: SF_FONT_FAMILY, 
        },
      },
    },
  },
});