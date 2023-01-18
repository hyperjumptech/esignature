import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContentField from './content-field';
import ContentFieldDetail from './content-field-detail';
import ContentFieldUpdate from './content-field-update';
import ContentFieldDeleteDialog from './content-field-delete-dialog';

const ContentFieldRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ContentField />} />
    <Route path="new" element={<ContentFieldUpdate />} />
    <Route path=":id">
      <Route index element={<ContentFieldDetail />} />
      <Route path="edit" element={<ContentFieldUpdate />} />
      <Route path="delete" element={<ContentFieldDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContentFieldRoutes;
