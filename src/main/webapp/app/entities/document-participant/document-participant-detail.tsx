import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document-participant.reducer';

export const DocumentParticipantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentParticipantEntity = useAppSelector(state => state.documentParticipant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentParticipantDetailsHeading">
          <Translate contentKey="esigningApp.documentParticipant.detail.title">DocumentParticipant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{documentParticipantEntity.id}</dd>
          <dt>
            <span id="isOwner">
              <Translate contentKey="esigningApp.documentParticipant.isOwner">Is Owner</Translate>
            </span>
          </dt>
          <dd>{documentParticipantEntity.isOwner ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="esigningApp.documentParticipant.document">Document</Translate>
          </dt>
          <dd>{documentParticipantEntity.document ? documentParticipantEntity.document.title : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.documentParticipant.user">User</Translate>
          </dt>
          <dd>{documentParticipantEntity.user ? documentParticipantEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.documentParticipant.document">Document</Translate>
          </dt>
          <dd>{documentParticipantEntity.document ? documentParticipantEntity.document.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/document-participant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document-participant/${documentParticipantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentParticipantDetail;
