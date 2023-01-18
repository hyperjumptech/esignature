import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './signature-block.reducer';

export const SignatureBlockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const signatureBlockEntity = useAppSelector(state => state.signatureBlock.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="signatureBlockDetailsHeading">
          <Translate contentKey="esigningApp.signatureBlock.detail.title">SignatureBlock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{signatureBlockEntity.id}</dd>
          <dt>
            <span id="styling">
              <Translate contentKey="esigningApp.signatureBlock.styling">Styling</Translate>
            </span>
          </dt>
          <dd>{signatureBlockEntity.styling}</dd>
          <dt>
            <span id="pubKey">
              <Translate contentKey="esigningApp.signatureBlock.pubKey">Pub Key</Translate>
            </span>
          </dt>
          <dd>{signatureBlockEntity.pubKey}</dd>
          <dt>
            <span id="pubKeyFingerprint">
              <Translate contentKey="esigningApp.signatureBlock.pubKeyFingerprint">Pub Key Fingerprint</Translate>
            </span>
          </dt>
          <dd>{signatureBlockEntity.pubKeyFingerprint}</dd>
          <dt>
            <Translate contentKey="esigningApp.signatureBlock.user">User</Translate>
          </dt>
          <dd>{signatureBlockEntity.user ? signatureBlockEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.signatureBlock.contentField">Content Field</Translate>
          </dt>
          <dd>{signatureBlockEntity.contentField ? signatureBlockEntity.contentField.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/signature-block" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/signature-block/${signatureBlockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SignatureBlockDetail;
