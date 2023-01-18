import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AddSigner from './signer-add';
import SignerDeleteDialog from './signer-delete-modal';
import SignerUpload from './signer-upload';

const SignerRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route index element={<SignerUpload />} />
      <Route path="add" element={<AddSigner />} />
      <Route path=":id">
        <Route path="delete" element={<SignerDeleteDialog />} />
      </Route>
    </ErrorBoundaryRoutes>
  </div>
);

export default SignerRoutes;
