import './signer.scss';

import React from 'react';
import { StepLayout } from './signer-components';
import SignerUpload from './signer-upload';

function SignerLayout() {
  return (
    <div className="d-flex flex-column signer-layout">
      <StepLayout currentPage={1} />
      <SignerUpload />
    </div>
  );
}

export default SignerLayout;
