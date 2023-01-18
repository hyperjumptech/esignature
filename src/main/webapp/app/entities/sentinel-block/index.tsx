import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SentinelBlock from './sentinel-block';
import SentinelBlockDetail from './sentinel-block-detail';
import SentinelBlockUpdate from './sentinel-block-update';
import SentinelBlockDeleteDialog from './sentinel-block-delete-dialog';

const SentinelBlockRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SentinelBlock />} />
    <Route path="new" element={<SentinelBlockUpdate />} />
    <Route path=":id">
      <Route index element={<SentinelBlockDetail />} />
      <Route path="edit" element={<SentinelBlockUpdate />} />
      <Route path="delete" element={<SentinelBlockDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SentinelBlockRoutes;
