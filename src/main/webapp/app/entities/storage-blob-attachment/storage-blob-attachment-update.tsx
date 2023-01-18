import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStorageBlob } from 'app/shared/model/storage-blob.model';
import { getEntities as getStorageBlobs } from 'app/entities/storage-blob/storage-blob.reducer';
import { IStorageBlobAttachment } from 'app/shared/model/storage-blob-attachment.model';
import { getEntity, updateEntity, createEntity, reset } from './storage-blob-attachment.reducer';

export const StorageBlobAttachmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const storageBlobs = useAppSelector(state => state.storageBlob.entities);
  const storageBlobAttachmentEntity = useAppSelector(state => state.storageBlobAttachment.entity);
  const loading = useAppSelector(state => state.storageBlobAttachment.loading);
  const updating = useAppSelector(state => state.storageBlobAttachment.updating);
  const updateSuccess = useAppSelector(state => state.storageBlobAttachment.updateSuccess);

  const handleClose = () => {
    navigate('/storage-blob-attachment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStorageBlobs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...storageBlobAttachmentEntity,
      ...values,
      sblob: storageBlobs.find(it => it.id.toString() === values.sblob.toString()),
      storageBlob: storageBlobs.find(it => it.id.toString() === values.storageBlob.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...storageBlobAttachmentEntity,
          sblob: storageBlobAttachmentEntity?.sblob?.id,
          storageBlob: storageBlobAttachmentEntity?.storageBlob?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="esigningApp.storageBlobAttachment.home.createOrEditLabel" data-cy="StorageBlobAttachmentCreateUpdateHeading">
            <Translate contentKey="esigningApp.storageBlobAttachment.home.createOrEditLabel">
              Create or edit a StorageBlobAttachment
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="storage-blob-attachment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('esigningApp.storageBlobAttachment.attachmentName')}
                id="storage-blob-attachment-attachmentName"
                name="attachmentName"
                data-cy="attachmentName"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlobAttachment.recordType')}
                id="storage-blob-attachment-recordType"
                name="recordType"
                data-cy="recordType"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlobAttachment.recordId')}
                id="storage-blob-attachment-recordId"
                name="recordId"
                data-cy="recordId"
                type="text"
              />
              <ValidatedField
                id="storage-blob-attachment-sblob"
                name="sblob"
                data-cy="sblob"
                label={translate('esigningApp.storageBlobAttachment.sblob')}
                type="select"
              >
                <option value="" key="0" />
                {storageBlobs
                  ? storageBlobs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.filename}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="storage-blob-attachment-storageBlob"
                name="storageBlob"
                data-cy="storageBlob"
                label={translate('esigningApp.storageBlobAttachment.storageBlob')}
                type="select"
              >
                <option value="" key="0" />
                {storageBlobs
                  ? storageBlobs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/storage-blob-attachment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StorageBlobAttachmentUpdate;
