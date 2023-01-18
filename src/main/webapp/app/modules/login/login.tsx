import React from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { login } from 'app/shared/reducers/authentication';
import { useForm } from 'react-hook-form';
import { translate, Translate, ValidatedField } from 'react-jhipster';
import { Alert, Button, Col, Form, Row } from 'reactstrap';

export const Login = () => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));

  const handleClose = () => {
    navigate('/');
  };

  const submitLogin = ({ username, password, rememberMe }) => {
    handleLogin(username, password, rememberMe);
  };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const handleLoginSubmit = e => {
    handleSubmit(submitLogin)(e);
  };

  const { from } = (location.state as any) || { from: { pathname: '/', search: location.search } };
  if (isAuthenticated) {
    return <Navigate to={from} replace />;
  }
  return (
    <div>
      <Row className="justify-content-center text-center">
        <Col md="8">
          <div>
            <img src="../../../content/images/logo.svg" alt="Logo" />
          </div>
          <h1 id="register-title" data-cy="registerTitle">
            <Translate contentKey="login.title">Sign in</Translate>
          </h1>
        </Col>
      </Row>
      <Row>
        <Col>
          <Form onSubmit={handleLoginSubmit}>
            <div className="justify-content-center">
              <Row>
                <Col md="12">
                  {loginError ? (
                    <Alert color="danger" data-cy="loginError">
                      <Translate contentKey="login.messages.error.authentication">
                        <strong>Failed to sign in!</strong> Please check your credentials and try again.
                      </Translate>
                    </Alert>
                  ) : null}
                </Col>
                <Col md="12">
                  <ValidatedField
                    name="username"
                    label={translate('global.form.username.label')}
                    placeholder={translate('global.form.username.placeholder')}
                    required
                    autoFocus
                    data-cy="username"
                    validate={{ required: 'Username cannot be empty!' }}
                    register={register}
                    error={errors.username}
                    isTouched={touchedFields.username}
                  />
                  <ValidatedField
                    name="password"
                    type="password"
                    label={translate('login.form.password')}
                    placeholder={translate('login.form.password.placeholder')}
                    required
                    data-cy="password"
                    validate={{ required: 'Password cannot be empty!' }}
                    register={register}
                    error={errors.password}
                    isTouched={touchedFields.password}
                  />
                  <ValidatedField
                    name="rememberMe"
                    type="checkbox"
                    check
                    label={translate('login.form.rememberme')}
                    value={true}
                    register={register}
                  />
                </Col>
              </Row>
              <div className="mt-1">&nbsp;</div>
              <div className="justify-content-center text-center">
                <Alert color="warning">
                  <Link to="/account/reset/request" data-cy="forgetYourPasswordSelector">
                    <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>
                  </Link>
                </Alert>
                <Alert color="warning">
                  <span>
                    <Translate contentKey="global.messages.info.register.noaccount">You don&apos;t have an account yet?</Translate>
                  </span>{' '}
                  <Link to="/account/register">
                    <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                  </Link>
                </Alert>
                <Button color="secondary" onClick={handleClose} tabIndex={1}>
                  <Translate contentKey="entity.action.cancel">Cancel</Translate>
                </Button>{' '}
                <Button color="primary" type="submit" data-cy="submit">
                  <Translate contentKey="login.form.button">Sign in</Translate>
                </Button>
              </div>
            </div>
          </Form>
        </Col>
      </Row>
    </div>
  );
};

export default Login;
