import React from 'react';
import { Translate } from 'react-jhipster';

import { NavLink as Link } from 'react-router-dom';
import { NavbarBrand, NavItem, NavLink } from 'reactstrap';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="../../../../content/images/logo.svg" alt="Logo" className="brand-color" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex flex-column align-items-center">
      <img src="../../../../content/images/home.svg" alt="Logo" className="brand-color" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Sign = () => (
  <NavItem>
    <NavLink tag={Link} to="/signer" className="d-flex flex-column align-items-center">
      <img src="../../../../content/images/sign.svg" alt="Logo" className="brand-color" />
      <span>
        <Translate contentKey="global.menu.sign">Sign</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Documents = () => (
  <NavItem>
    <NavLink tag={Link} to="/documentManagement" className="d-flex flex-column align-items-center">
      <img src="../../../../content/images/documents.svg" alt="Logo" className="brand-color" />
      <span>
        <Translate contentKey="global.menu.documents">Documents</Translate>
      </span>
    </NavLink>
  </NavItem>
);
