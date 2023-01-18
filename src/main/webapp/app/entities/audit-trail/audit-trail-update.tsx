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
import { IAuditTrail } from 'app/shared/model/audit-trail.model';
import { getEntity, updateEntity, createEntity, reset } from './audit-trail.reducer';

export const AuditTrailUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const documents = useAppSelector(state => state.document.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const auditTrailEntity = useAppSelector(state => state.auditTrail.entity);
  const loading = useAppSelector(state => state.auditTrail.loading);
  const updating = useAppSelector(state => state.auditTrail.updating);
  const updateSuccess = useAppSelector(state => state.auditTrail.updateSuccess);

  const handleClose = () => {
    navigate('/audit-trail');
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
    values.time = convertDateTimeToServer(values.time);

    const entity = {
      ...auditTrailEntity,
      ...values,
      document: documents.find(it => it.id.toString() === values.document.toString()),
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          time: displayDefaultDateTime(),
        }
      : {
          ...auditTrailEntity,
          time: convertDateTimeFromServer(auditTrailEntity.time),
          document: auditTrailEntity?.document?.id,
          user: auditTrailEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="esigningApp.auditTrail.home.createOrEditLabel" data-cy="AuditTrailCreateUpdateHeading">
            <Translate contentKey="esigningApp.auditTrail.home.createOrEditLabel">Create or edit a AuditTrail</Translate>
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
                  id="audit-trail-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('esigningApp.auditTrail.activity')}
                id="audit-trail-activity"
                name="activity"
                data-cy="activity"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.auditTrail.description')}
                id="audit-trail-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.auditTrail.ipaddress')}
                id="audit-trail-ipaddress"
                name="ipaddress"
                data-cy="ipaddress"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.auditTrail.time')}
                id="audit-trail-time"
                name="time"
                data-cy="time"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="audit-trail-document"
                name="document"
                data-cy="document"
                label={translate('esigningApp.auditTrail.document')}
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
                id="audit-trail-user"
                name="user"
                data-cy="user"
                label={translate('esigningApp.auditTrail.user')}
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
              <ValidatedField
                id="audit-trail-document"
                name="document"
                data-cy="document"
                label={translate('esigningApp.auditTrail.document')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/audit-trail" replace color="info">
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

export default AuditTrailUpdate;
