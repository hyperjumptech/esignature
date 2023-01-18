import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './audit-trail.reducer';

export const AuditTrailDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const auditTrailEntity = useAppSelector(state => state.auditTrail.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="auditTrailDetailsHeading">
          <Translate contentKey="esigningApp.auditTrail.detail.title">AuditTrail</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{auditTrailEntity.id}</dd>
          <dt>
            <span id="activity">
              <Translate contentKey="esigningApp.auditTrail.activity">Activity</Translate>
            </span>
          </dt>
          <dd>{auditTrailEntity.activity}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="esigningApp.auditTrail.description">Description</Translate>
            </span>
          </dt>
          <dd>{auditTrailEntity.description}</dd>
          <dt>
            <span id="ipaddress">
              <Translate contentKey="esigningApp.auditTrail.ipaddress">Ipaddress</Translate>
            </span>
          </dt>
          <dd>{auditTrailEntity.ipaddress}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="esigningApp.auditTrail.time">Time</Translate>
            </span>
          </dt>
          <dd>{auditTrailEntity.time ? <TextFormat value={auditTrailEntity.time} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="esigningApp.auditTrail.document">Document</Translate>
          </dt>
          <dd>{auditTrailEntity.document ? auditTrailEntity.document.title : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.auditTrail.user">User</Translate>
          </dt>
          <dd>{auditTrailEntity.user ? auditTrailEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.auditTrail.document">Document</Translate>
          </dt>
          <dd>{auditTrailEntity.document ? auditTrailEntity.document.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/audit-trail" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/audit-trail/${auditTrailEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuditTrailDetail;
