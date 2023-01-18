import EntitiesMenuItems from 'app/entities/menu';
import React from 'react';
import { translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => {
  if (props.isHorizontal) {
    return (
      <ul style={{ listStyleType: 'none', padding: 0 }}>
        <li>
          <div className="px-4 py-2">
            <p className="font-weight-bold">{translate('global.menu.entities.main')}</p>
            <div className="px-2">
              <EntitiesMenuItems isHorizontal={props.isHorizontal} />
            </div>
          </div>
        </li>
      </ul>
    );
  }
  return (
    <NavDropdown
      icon="th-list"
      name={translate('global.menu.entities.main')}
      id="entity-menu"
      data-cy="entity"
      style={{ maxHeight: '80vh', overflow: 'auto' }}
    >
      <EntitiesMenuItems />
    </NavDropdown>
  );
};
