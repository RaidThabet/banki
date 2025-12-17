import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import heroImage from "../assets/Banki.png"; 

const SF_FONT_FAMILY =
  '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif';

const HEADER_BG_COLOR = '#00C16D';
const PRIMARY_COLOR = '#2D5B48';
const SECONDARY_ACCENT = HEADER_BG_COLOR;

const pages = [
  { label: 'Home', href: 'Home' },
  { label: 'Check Balance', href: 'CheckBalance' },
  { label: 'Add Beneficiary', href: 'AddBeneficiary' },
  { label: 'Transaction', href: 'Transaction' },
  { label: 'History', href: 'History' },
];

const settings = [
  { label: 'Account Info', href: 'AccountInfo' },
  { label: 'Logout', href: '#' },
];

function Navbar() {
  const navigate = useNavigate();
  const location = useLocation(); // <-- current route
  const [anchorElNav, setAnchorElNav] = useState(null);
  const [anchorElUser, setAnchorElUser] = useState(null);

  const handleOpenNavMenu = (event) => setAnchorElNav(event.currentTarget);
  const handleOpenUserMenu = (event) => setAnchorElUser(event.currentTarget);
  const handleCloseNavMenu = () => setAnchorElNav(null);
  const handleCloseUserMenu = () => setAnchorElUser(null);

  const handleLinkClick = (e, href) => {
    e.preventDefault();
    handleCloseNavMenu();
    handleCloseUserMenu();

    if (href && href !== "#") {
      navigate(`/${href.toLowerCase()}`);
    }
  };

  const LOGO_BAR_HEIGHT = { xs: 90, md: 120 };
  const NAV_BAR_HEIGHT = { xs: 56, md: 90 };
  const HEADER_HEIGHT_SM = LOGO_BAR_HEIGHT.xs + NAV_BAR_HEIGHT.xs;
  const HEADER_HEIGHT_MD = LOGO_BAR_HEIGHT.md + NAV_BAR_HEIGHT.md;

  return (
    <>
      <Box sx={{ height: { xs: `${HEADER_HEIGHT_SM}px`, md: `${HEADER_HEIGHT_MD}px` } }} />

      <Box
        sx={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          zIndex: 1200,
          width: '100%',
          boxShadow: 6,
        }}
      >
        <Box
          sx={{
            width: '100%',
            px: 2,
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: HEADER_BG_COLOR,
            borderBottom: `1px solid ${PRIMARY_COLOR}`,
            height: LOGO_BAR_HEIGHT,
          }}
        >
          <img
            src={heroImage}
            alt="Banki Logo"
            style={{ height: '100%', maxWidth: '100%', objectFit: 'contain' }}
          />
        </Box>

        <AppBar position="static" sx={{ backgroundColor: PRIMARY_COLOR, height: NAV_BAR_HEIGHT }}>
          <Container maxWidth="xl">
            <Toolbar disableGutters sx={{ height: NAV_BAR_HEIGHT, position: 'relative' }}>

              {/* MOBILE MENU */}
              <Box sx={{ display: { xs: 'flex', md: 'none' } }}>
                <IconButton size="large" onClick={handleOpenNavMenu} color="inherit">
                  <MenuIcon />
                </IconButton>
                <Menu
                  anchorEl={anchorElNav}
                  anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
                  transformOrigin={{ vertical: 'top', horizontal: 'left' }}
                  open={Boolean(anchorElNav)}
                  onClose={handleCloseNavMenu}
                >
                  {pages.map((page) => (
                    <MenuItem
                      key={page.label}
                      onClick={(e) => handleLinkClick(e, page.href)}
                    >
                      <Typography sx={{ fontFamily: SF_FONT_FAMILY }}>
                        {page.label}
                      </Typography>
                    </MenuItem>
                  ))}
                </Menu>
              </Box>

              {/* CENTERED DESKTOP MENU */}
              <Box
                sx={{
                  position: 'absolute',
                  left: '50%',
                  transform: 'translateX(-50%)',
                  display: { xs: 'none', md: 'flex' },
                  gap: 4,
                }}
              >
                {pages.map((page) => {
                  const isActive =
                    location.pathname === `/${page.href.toLowerCase()}` ||
                    (page.href === 'Home' && location.pathname === '/');

                  return (
                    <Button
                      key={page.label}
                      onClick={(e) => handleLinkClick(e, page.href)}
                      sx={{
                        color: isActive ? SECONDARY_ACCENT : 'white',
                        fontWeight: isActive ? 800 : 600,
                        fontSize: '1.2rem',
                        textTransform: 'uppercase',
                        fontFamily: SF_FONT_FAMILY,
                        '&:hover': {
                          backgroundColor: 'transparent',
                          color: SECONDARY_ACCENT,
                        },
                      }}
                    >
                      {page.label}
                    </Button>
                  );
                })}
              </Box>

              {/* USER MENU – FAR RIGHT */}
              <Box sx={{ ml: 'auto' }}>
                <Tooltip title="Open settings">
                  <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                    <Avatar alt="User" />
                  </IconButton>
                </Tooltip>
                <Menu
                  sx={{ mt: '45px' }}
                  anchorEl={anchorElUser}
                  anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                  transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                  open={Boolean(anchorElUser)}
                  onClose={handleCloseUserMenu}
                >
                  {settings.map((setting) => (
                    <MenuItem
                      key={setting.label}
                      onClick={(e) => handleLinkClick(e, setting.href)}
                    >
                      <Typography sx={{ fontFamily: SF_FONT_FAMILY }}>
                        {setting.label}
                      </Typography>
                    </MenuItem>
                  ))}
                </Menu>
              </Box>

            </Toolbar>
          </Container>
        </AppBar>
      </Box>
    </>
  );
}

export default Navbar;
