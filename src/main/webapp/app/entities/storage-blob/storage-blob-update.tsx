import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDocument } from 'app/shared/model/document.model';
import { getEntities as getDocuments } from 'app/entities/document/document.reducer';
import { IStorageBlob } from 'app/shared/model/storage-blob.model';
import { getEntity, updateEntity, createEntity, reset } from './storage-blob.reducer';

export const StorageBlobUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const documents = useAppSelector(state => state.document.entities);
  const storageBlobEntity = useAppSelector(state => state.storageBlob.entity);
  const loading = useAppSelector(state => state.storageBlob.loading);
  const updating = useAppSelector(state => state.storageBlob.updating);
  const updateSuccess = useAppSelector(state => state.storageBlob.updateSuccess);

  const handleClose = () => {
    navigate('/storage-blob');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDocuments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createDate = convertDateTimeToServer(values.createDate);
    values.updateDate = convertDateTimeToServer(values.updateDate);

    const entity = {
      ...storageBlobEntity,
      ...values,
      document: documents.find(it => it.id.toString() === values.document.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createDate: displayDefaultDateTime(),
          updateDate: displayDefaultDateTime(),
        }
      : {
          ...storageBlobEntity,
          createDate: convertDateTimeFromServer(storageBlobEntity.createDate),
          updateDate: convertDateTimeFromServer(storageBlobEntity.updateDate),
          document: storageBlobEntity?.document?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="esigningApp.storageBlob.home.createOrEditLabel" data-cy="StorageBlobCreateUpdateHeading">
            <Translate contentKey="esigningApp.storageBlob.home.createOrEditLabel">Create or edit a StorageBlob</Translate>
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
                  id="storage-blob-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('esigningApp.storageBlob.key')} id="storage-blob-key" name="key" data-cy="key" type="text" />
              <ValidatedField
                label={translate('esigningApp.storageBlob.path')}
                id="storage-blob-path"
                name="path"
                data-cy="path"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.filename')}
                id="storage-blob-filename"
                name="filename"
                data-cy="filename"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.contentType')}
                id="storage-blob-contentType"
                name="contentType"
                data-cy="contentType"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.metadata')}
                id="storage-blob-metadata"
                name="metadata"
                data-cy="metadata"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.byteSize')}
                id="storage-blob-byteSize"
                name="byteSize"
                data-cy="byteSize"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.checksum')}
                id="storage-blob-checksum"
                name="checksum"
                data-cy="checksum"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.createDate')}
                id="storage-blob-createDate"
                name="createDate"
                data-cy="createDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.createBy')}
                id="storage-blob-createBy"
                name="createBy"
                data-cy="createBy"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.updateDate')}
                id="storage-blob-updateDate"
                name="updateDate"
                data-cy="updateDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('esigningApp.storageBlob.updateBy')}
                id="storage-blob-updateBy"
                name="updateBy"
                data-cy="updateBy"
                type="text"
              />
              <ValidatedField
                id="storage-blob-document"
                name="document"
                data-cy="document"
                label={translate('esigningApp.storageBlob.document')}
                type="select"
              >
                <option value="" key="0" />
                {documents
                  ? documents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="storage-blob-document"
                name="document"
                data-cy="document"
                label={translate('esigningApp.storageBlob.document')}
                type="select"
              >
                <option value="" key="0" />
                {documents
                  ? documents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/storage-blob" replace color="info">
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

export default StorageBlobUpdate;
