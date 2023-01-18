import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SignatureBlock from './signature-block';
import SignatureBlockDetail from './signature-block-detail';
import SignatureBlockUpdate from './signature-block-update';
import SignatureBlockDeleteDialog from './signature-block-delete-dialog';

const SignatureBlockRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SignatureBlock />} />
    <Route path="new" element={<SignatureBlockUpdate />} />
    <Route path=":id">
      <Route index element={<SignatureBlockDetail />} />
      <Route path="edit" element={<SignatureBlockUpdate />} />
      <Route path="delete" element={<SignatureBlockDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SignatureBlockRoutes;
