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
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IContentField } from 'app/shared/model/content-field.model';
import { getEntity, updateEntity, createEntity, reset } from './content-field.reducer';

export const ContentFieldUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const documents = useAppSelector(state => state.document.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const contentFieldEntity = useAppSelector(state => state.contentField.entity);
  const loading = useAppSelector(state => state.contentField.loading);
  const updating = useAppSelector(state => state.contentField.updating);
  const updateSuccess = useAppSelector(state => state.contentField.updateSuccess);

  const handleClose = () => {
    navigate('/content-field');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDocuments({}));
    dispatch(getUsers({}));
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
      ...contentFieldEntity,
      ...values,
      document: documents.find(it => it.id.toString() === values.document.toString()),
      signatory: users.find(it => it.id.toString() === values.signatory.toString()),
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
          ...contentFieldEntity,
          createDate: convertDateTimeFromServer(contentFieldEntity.createDate),
          updateDate: convertDateTimeFromServer(contentFieldEntity.updateDate),
          document: contentFieldEntity?.document?.id,
          signatory: contentFieldEntity?.signatory?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="esigningApp.contentField.home.createOrEditLabel" data-cy="ContentFieldCreateUpdateHeading">
            <Translate contentKey="esigningApp.contentField.home.createOrEditLabel">Create or edit a ContentField</Translate>
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
                  id="content-field-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('esigningApp.contentField.contentType')}
                id="content-field-contentType"
                name="contentType"
                data-cy="contentType"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.contentField.bbox')}
                id="content-field-bbox"
                name="bbox"
                data-cy="bbox"
                type="textarea"
              />
              <ValidatedField
                label={translate('esigningApp.contentField.createDate')}
                id="content-field-createDate"
                name="createDate"
                data-cy="createDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('esigningApp.contentField.createBy')}
                id="content-field-createBy"
                name="createBy"
                data-cy="createBy"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.contentField.updateDate')}
                id="content-field-updateDate"
                name="updateDate"
                data-cy="updateDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('esigningApp.contentField.updateBy')}
                id="content-field-updateBy"
                name="updateBy"
                data-cy="updateBy"
                type="text"
              />
              <ValidatedField
                id="content-field-document"
                name="document"
                data-cy="document"
                label={translate('esigningApp.contentField.document')}
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
                id="content-field-signatory"
                name="signatory"
                data-cy="signatory"
                label={translate('esigningApp.contentField.signatory')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.email}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/content-field" replace color="info">
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

export default ContentFieldUpdate;
