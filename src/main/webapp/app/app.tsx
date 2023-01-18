import 'app/config/dayjs.ts';
import 'react-toastify/dist/ReactToastify.css';
import './app.scss';
import './fonts.scss';

import React, { useEffect } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import { Card } from 'reactstrap';

import { AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import AppRoutes from 'app/routes';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import Footer from 'app/shared/layout/footer/footer';
import { getProfile } from 'app/shared/reducers/application-profile';
import { getSession } from 'app/shared/reducers/authentication';
import Header from './shared/layout/header/header';
import Sidebar from './shared/layout/sidebar/sidebar';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

export const App = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);

  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const ribbonEnv = useAppSelector(state => state.applicationProfile.ribbonEnv);
  const isInProduction = useAppSelector(state => state.applicationProfile.inProduction);
  const isOpenAPIEnabled = useAppSelector(state => state.applicationProfile.isOpenAPIEnabled);

  const paddingTop = '0px';
  return (
    <BrowserRouter basename={baseHref}>
      <div className="d-flex flex-row w-100 align-self-stretch" style={{ minHeight: '100vh' }}>
        <div className="sidebar" style={{ width: '250px' }}>
          <ErrorBoundary>
            <Sidebar
              isAuthenticated={isAuthenticated}
              isAdmin={isAdmin}
              currentLocale={currentLocale}
              ribbonEnv={ribbonEnv}
              isInProduction={isInProduction}
              isOpenAPIEnabled={isOpenAPIEnabled}
            />
          </ErrorBoundary>
        </div>
        <div className="w-100">
          <div className="app-container" style={{ paddingTop }}>
            <ToastContainer position={toast.POSITION.TOP_LEFT} className="toastify-container" toastClassName="toastify-toast" />
            <ErrorBoundary>
              <Header
                isAuthenticated={isAuthenticated}
                isAdmin={isAdmin}
                currentLocale={currentLocale}
                ribbonEnv={ribbonEnv}
                isInProduction={isInProduction}
                isOpenAPIEnabled={isOpenAPIEnabled}
              />
            </ErrorBoundary>
            <div className="container-fluid view-container" id="app-view-container">
              <Card className="jh-card">
                <ErrorBoundary>
                  <AppRoutes />
                </ErrorBoundary>
              </Card>
              <Footer />
            </div>
          </div>
        </div>
      </div>

      {/* <Row className="flex">
        <Col md={2} className="">
          {' '}

        </Col>
        <Col md={10}>

        </Col>
      </Row> */}
    </BrowserRouter>
  );
};

export default App;
