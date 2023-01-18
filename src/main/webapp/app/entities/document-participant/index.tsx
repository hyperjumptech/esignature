import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocumentParticipant from './document-participant';
import DocumentParticipantDetail from './document-participant-detail';
import DocumentParticipantUpdate from './document-participant-update';
import DocumentParticipantDeleteDialog from './document-participant-delete-dialog';

const DocumentParticipantRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocumentParticipant />} />
    <Route path="new" element={<DocumentParticipantUpdate />} />
    <Route path=":id">
      <Route index element={<DocumentParticipantDetail />} />
      <Route path="edit" element={<DocumentParticipantUpdate />} />
      <Route path="delete" element={<DocumentParticipantDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocumentParticipantRoutes;
