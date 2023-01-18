import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = props => {
  if (props.isHorizontal) {
    return (
      <>
        <MenuItem to="/document">
          <Translate contentKey="global.menu.entities.document" />
        </MenuItem>
        <MenuItem to="/document-participant">
          <Translate contentKey="global.menu.entities.documentParticipant" />
        </MenuItem>
        <MenuItem to="/signature-block">
          <Translate contentKey="global.menu.entities.signatureBlock" />
        </MenuItem>
        <MenuItem to="/sentinel-block">
          <Translate contentKey="global.menu.entities.sentinelBlock" />
        </MenuItem>
        <MenuItem to="/text-block">
          <Translate contentKey="global.menu.entities.textBlock" />
        </MenuItem>
        <MenuItem to="/content-field">
          <Translate contentKey="global.menu.entities.contentField" />
        </MenuItem>
        <MenuItem to="/storage-blob">
          <Translate contentKey="global.menu.entities.storageBlob" />
        </MenuItem>
        <MenuItem to="/storage-blob-attachment">
          <Translate contentKey="global.menu.entities.storageBlobAttachment" />
        </MenuItem>
      </>
    );
  }

  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/document">
        <Translate contentKey="global.menu.entities.document" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/document-participant">
        <Translate contentKey="global.menu.entities.documentParticipant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/signature-block">
        <Translate contentKey="global.menu.entities.signatureBlock" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sentinel-block">
        <Translate contentKey="global.menu.entities.sentinelBlock" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/text-block">
        <Translate contentKey="global.menu.entities.textBlock" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/content-field">
        <Translate contentKey="global.menu.entities.contentField" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/storage-blob">
        <Translate contentKey="global.menu.entities.storageBlob" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/storage-blob-attachment">
        <Translate contentKey="global.menu.entities.storageBlobAttachment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/audit-trail">
        <Translate contentKey="global.menu.entities.auditTrail" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
