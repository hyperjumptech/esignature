import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './text-block.reducer';

export const TextBlockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const textBlockEntity = useAppSelector(state => state.textBlock.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="textBlockDetailsHeading">
          <Translate contentKey="esigningApp.textBlock.detail.title">TextBlock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{textBlockEntity.id}</dd>
          <dt>
            <span id="textType">
              <Translate contentKey="esigningApp.textBlock.textType">Text Type</Translate>
            </span>
          </dt>
          <dd>{textBlockEntity.textType}</dd>
          <dt>
            <span id="body">
              <Translate contentKey="esigningApp.textBlock.body">Body</Translate>
            </span>
          </dt>
          <dd>{textBlockEntity.body}</dd>
          <dt>
            <span id="styling">
              <Translate contentKey="esigningApp.textBlock.styling">Styling</Translate>
            </span>
          </dt>
          <dd>{textBlockEntity.styling}</dd>
          <dt>
            <Translate contentKey="esigningApp.textBlock.user">User</Translate>
          </dt>
          <dd>{textBlockEntity.user ? textBlockEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/text-block" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/text-block/${textBlockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TextBlockDetail;
