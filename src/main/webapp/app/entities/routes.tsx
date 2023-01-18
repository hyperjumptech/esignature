import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Document from './document';
import DocumentParticipant from './document-participant';
import SignatureBlock from './signature-block';
import SentinelBlock from './sentinel-block';
import TextBlock from './text-block';
import ContentField from './content-field';
import StorageBlob from './storage-blob';
import StorageBlobAttachment from './storage-blob-attachment';
import AuditTrail from './audit-trail';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="document/*" element={<Document />} />
        <Route path="document-participant/*" element={<DocumentParticipant />} />
        <Route path="signature-block/*" element={<SignatureBlock />} />
        <Route path="sentinel-block/*" element={<SentinelBlock />} />
        <Route path="text-block/*" element={<TextBlock />} />
        <Route path="content-field/*" element={<ContentField />} />
        <Route path="storage-blob/*" element={<StorageBlob />} />
        <Route path="storage-blob-attachment/*" element={<StorageBlobAttachment />} />
        <Route path="audit-trail/*" element={<AuditTrail />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
