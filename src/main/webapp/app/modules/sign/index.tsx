import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Sign from './sign';

const SignRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path=":id">
        <Route index element={<Sign />} />
      </Route>
    </ErrorBoundaryRoutes>
  </div>
);

export default SignRoutes;
