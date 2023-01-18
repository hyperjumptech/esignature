import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './storage-blob-attachment.reducer';

export const StorageBlobAttachmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storageBlobAttachmentEntity = useAppSelector(state => state.storageBlobAttachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageBlobAttachmentDetailsHeading">
          <Translate contentKey="esigningApp.storageBlobAttachment.detail.title">StorageBlobAttachment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storageBlobAttachmentEntity.id}</dd>
          <dt>
            <span id="attachmentName">
              <Translate contentKey="esigningApp.storageBlobAttachment.attachmentName">Attachment Name</Translate>
            </span>
          </dt>
          <dd>{storageBlobAttachmentEntity.attachmentName}</dd>
          <dt>
            <span id="recordType">
              <Translate contentKey="esigningApp.storageBlobAttachment.recordType">Record Type</Translate>
            </span>
          </dt>
          <dd>{storageBlobAttachmentEntity.recordType}</dd>
          <dt>
            <span id="recordId">
              <Translate contentKey="esigningApp.storageBlobAttachment.recordId">Record Id</Translate>
            </span>
          </dt>
          <dd>{storageBlobAttachmentEntity.recordId}</dd>
          <dt>
            <Translate contentKey="esigningApp.storageBlobAttachment.sblob">Sblob</Translate>
          </dt>
          <dd>{storageBlobAttachmentEntity.sblob ? storageBlobAttachmentEntity.sblob.filename : ''}</dd>
          <dt>
            <Translate contentKey="esigningApp.storageBlobAttachment.storageBlob">Storage Blob</Translate>
          </dt>
          <dd>{storageBlobAttachmentEntity.storageBlob ? storageBlobAttachmentEntity.storageBlob.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/storage-blob-attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage-blob-attachment/${storageBlobAttachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageBlobAttachmentDetail;
