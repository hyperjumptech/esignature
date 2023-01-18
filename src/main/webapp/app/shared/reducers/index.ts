import { ReducersMapObject } from '@reduxjs/toolkit';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import applicationProfile from './application-profile';
import authentication from './authentication';
import locale from './locale';

import entitiesReducers from 'app/entities/reducers';
import activate from 'app/modules/account/activate/activate.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
import password from 'app/modules/account/password/password.reducer';
import register from 'app/modules/account/register/register.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import documentManagement from 'app/modules/document-management/document-management.reducer';
import sign from 'app/modules/sign/sign.reducer';
import signer from 'app/modules/signers/signer.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer: ReducersMapObject = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  loadingBar,
  signer,
  documentManagement,
  sign,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  ...entitiesReducers,
};

export default rootReducer;
