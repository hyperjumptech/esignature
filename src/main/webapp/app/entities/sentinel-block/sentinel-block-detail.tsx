import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sentinel-block.reducer';

export const SentinelBlockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sentinelBlockEntity = useAppSelector(state => state.sentinelBlock.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sentinelBlockDetailsHeading">
          <Translate contentKey="esigningApp.sentinelBlock.detail.title">SentinelBlock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sentinelBlockEntity.id}</dd>
          <dt>
            <span id="blockType">
              <Translate contentKey="esigningApp.sentinelBlock.blockType">Block Type</Translate>
            </span>
          </dt>
          <dd>{sentinelBlockEntity.blockType}</dd>
          <dt>
            <span id="placeholder">
              <Translate contentKey="esigningApp.sentinelBlock.placeholder">Placeholder</Translate>
            </span>
          </dt>
          <dd>{sentinelBlockEntity.placeholder}</dd>
          <dt>
            <Translate contentKey="esigningApp.sentinelBlock.contentField">Content Field</Translate>
          </dt>
          <dd>{sentinelBlockEntity.contentField ? sentinelBlockEntity.contentField.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sentinel-block" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sentinel-block/${sentinelBlockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SentinelBlockDetail;
