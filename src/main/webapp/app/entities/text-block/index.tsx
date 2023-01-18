import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TextBlock from './text-block';
import TextBlockDetail from './text-block-detail';
import TextBlockUpdate from './text-block-update';
import TextBlockDeleteDialog from './text-block-delete-dialog';

const TextBlockRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TextBlock />} />
    <Route path="new" element={<TextBlockUpdate />} />
    <Route path=":id">
      <Route index element={<TextBlockDetail />} />
      <Route path="edit" element={<TextBlockUpdate />} />
      <Route path="delete" element={<TextBlockDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TextBlockRoutes;
