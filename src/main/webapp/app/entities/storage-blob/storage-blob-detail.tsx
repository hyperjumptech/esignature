import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './storage-blob.reducer';

export const StorageBlobDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storageBlobEntity = useAppSelector(state => state.storageBlob.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageBlobDetailsHeading">
          <Translate contentKey="esigningApp.storageBlob.detail.title">StorageBlob</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.id}</dd>
          <dt>
            <span id="key">
              <Translate contentKey="esigningApp.storageBlob.key">Key</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.key}</dd>
          <dt>
            <span id="path">
              <Translate contentKey="esigningApp.storageBlob.path">Path</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.path}</dd>
          <dt>
            <span id="filename">
              <Translate contentKey="esigningApp.storageBlob.filename">Filename</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.filename}</dd>
          <dt>
            <span id="contentType">
              <Translate contentKey="esigningApp.storageBlob.contentType">Content Type</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.contentType}</dd>
          <dt>
            <span id="metadata">
              <Translate contentKey="esigningApp.storageBlob.metadata">Metadata</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.metadata}</dd>
          <dt>
            <span id="byteSize">
              <Translate contentKey="esigningApp.storageBlob.byteSize">Byte Size</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.byteSize}</dd>
          <dt>
            <span id="checksum">
              <Translate contentKey="esigningApp.storageBlob.checksum">Checksum</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.checksum}</dd>
          <dt>
            <span id="createDate">
              <Translate contentKey="esigningApp.storageBlob.createDate">Create Date</Translate>
            </span>
          </dt>
          <dd>
            {storageBlobEntity.createDate ? <TextFormat value={storageBlobEntity.createDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createBy">
              <Translate contentKey="esigningApp.storageBlob.createBy">Create By</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.createBy}</dd>
          <dt>
            <span id="updateDate">
              <Translate contentKey="esigningApp.storageBlob.updateDate">Update Date</Translate>
            </span>
          </dt>
          <dd>
            {storageBlobEntity.updateDate ? <TextFormat value={storageBlobEntity.updateDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updateBy">
              <Translate contentKey="esigningApp.storageBlob.updateBy">Update By</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.updateBy}</dd>
          <dt>
            <Translate contentKey="esigningApp.storageBlob.document">Document</Translate>
          </dt>
          <dd>{storageBlobEntity.document ? storageBlobEntity.document.title : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.storageBlob.document">Document</Translate>
          </dt>
          <dd>{storageBlobEntity.document ? storageBlobEntity.document.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/storage-blob" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage-blob/${storageBlobEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageBlobDetail;
