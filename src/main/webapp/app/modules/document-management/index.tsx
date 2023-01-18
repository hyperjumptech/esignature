import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import DocumentManagement from './document-management';

const SignerRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route index element={<DocumentManagement />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default SignerRoutes;
