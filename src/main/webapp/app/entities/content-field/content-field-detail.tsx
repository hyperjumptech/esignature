import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content-field.reducer';

export const ContentFieldDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contentFieldEntity = useAppSelector(state => state.contentField.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentFieldDetailsHeading">
          <Translate contentKey="esigningApp.contentField.detail.title">ContentField</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contentFieldEntity.id}</dd>
          <dt>
            <span id="contentType">
              <Translate contentKey="esigningApp.contentField.contentType">Content Type</Translate>
            </span>
          </dt>
          <dd>{contentFieldEntity.contentType}</dd>
          <dt>
            <span id="bbox">
              <Translate contentKey="esigningApp.contentField.bbox">Bbox</Translate>
            </span>
          </dt>
          <dd>{contentFieldEntity.bbox}</dd>
          <dt>
            <span id="createDate">
              <Translate contentKey="esigningApp.contentField.createDate">Create Date</Translate>
            </span>
          </dt>
          <dd>
            {contentFieldEntity.createDate ? (
              <TextFormat value={contentFieldEntity.createDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createBy">
              <Translate contentKey="esigningApp.contentField.createBy">Create By</Translate>
            </span>
          </dt>
          <dd>{contentFieldEntity.createBy}</dd>
          <dt>
            <span id="updateDate">
              <Translate contentKey="esigningApp.contentField.updateDate">Update Date</Translate>
            </span>
          </dt>
          <dd>
            {contentFieldEntity.updateDate ? (
              <TextFormat value={contentFieldEntity.updateDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updateBy">
              <Translate contentKey="esigningApp.contentField.updateBy">Update By</Translate>
            </span>
          </dt>
          <dd>{contentFieldEntity.updateBy}</dd>
          <dt>
            <Translate contentKey="esigningApp.contentField.document">Document</Translate>
          </dt>
          <dd>{contentFieldEntity.document ? contentFieldEntity.document.title : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.contentField.signatory">Signatory</Translate>
          </dt>
          <dd>{contentFieldEntity.signatory ? contentFieldEntity.signatory.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/content-field" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content-field/${contentFieldEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentFieldDetail;
