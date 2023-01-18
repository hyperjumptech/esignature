import './sidebar.scss';

import React, { useState } from 'react';
import { Storage, Translate } from 'react-jhipster';
import LoadingBar from 'react-redux-loading-bar';
import { Collapse, Nav, Navbar, NavbarToggler } from 'reactstrap';

import { useAppDispatch } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { Brand, Documents, Home, Sign } from './sidebar-components';

export interface ISidebarProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Sidebar = (props: ISidebarProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const dispatch = useAppDispatch();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="app-sidebar" className="d-flex flex-column align-items-center white-text h-100">
      <LoadingBar className="loading-bar" />
      <Brand />
      <Navbar data-cy="navbar" expand="md">
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="sidebar-tabs" className="d-flex flex-column" navbar>
            <Sign />
            <hr />
            <Home />
            <Documents />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Sidebar;
