import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StorageBlobAttachment from './storage-blob-attachment';
import StorageBlobAttachmentDetail from './storage-blob-attachment-detail';
import StorageBlobAttachmentUpdate from './storage-blob-attachment-update';
import StorageBlobAttachmentDeleteDialog from './storage-blob-attachment-delete-dialog';

const StorageBlobAttachmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StorageBlobAttachment />} />
    <Route path="new" element={<StorageBlobAttachmentUpdate />} />
    <Route path=":id">
      <Route index element={<StorageBlobAttachmentDetail />} />
      <Route path="edit" element={<StorageBlobAttachmentUpdate />} />
      <Route path="delete" element={<StorageBlobAttachmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StorageBlobAttachmentRoutes;
