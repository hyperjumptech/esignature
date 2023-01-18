import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IContentField } from 'app/shared/model/content-field.model';
import { getEntities as getContentFields } from 'app/entities/content-field/content-field.reducer';
import { ISignatureBlock } from 'app/shared/model/signature-block.model';
import { getEntity, updateEntity, createEntity, reset } from './signature-block.reducer';

export const SignatureBlockUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const contentFields = useAppSelector(state => state.contentField.entities);
  const signatureBlockEntity = useAppSelector(state => state.signatureBlock.entity);
  const loading = useAppSelector(state => state.signatureBlock.loading);
  const updating = useAppSelector(state => state.signatureBlock.updating);
  const updateSuccess = useAppSelector(state => state.signatureBlock.updateSuccess);

  const handleClose = () => {
    navigate('/signature-block');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getContentFields({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...signatureBlockEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      contentField: contentFields.find(it => it.id.toString() === values.contentField.toString()),
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
          ...signatureBlockEntity,
          user: signatureBlockEntity?.user?.id,
          contentField: signatureBlockEntity?.contentField?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="esigningApp.signatureBlock.home.createOrEditLabel" data-cy="SignatureBlockCreateUpdateHeading">
            <Translate contentKey="esigningApp.signatureBlock.home.createOrEditLabel">Create or edit a SignatureBlock</Translate>
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
                  id="signature-block-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('esigningApp.signatureBlock.styling')}
                id="signature-block-styling"
                name="styling"
                data-cy="styling"
                type="textarea"
              />
              <ValidatedField
                label={translate('esigningApp.signatureBlock.pubKey')}
                id="signature-block-pubKey"
                name="pubKey"
                data-cy="pubKey"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('esigningApp.signatureBlock.pubKeyFingerprint')}
                id="signature-block-pubKeyFingerprint"
                name="pubKeyFingerprint"
                data-cy="pubKeyFingerprint"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="signature-block-user"
                name="user"
                data-cy="user"
                label={translate('esigningApp.signatureBlock.user')}
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
                id="signature-block-contentField"
                name="contentField"
                data-cy="contentField"
                label={translate('esigningApp.signatureBlock.contentField')}
                type="select"
              >
                <option value="" key="0" />
                {contentFields
                  ? contentFields.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/signature-block" replace color="info">
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

export default SignatureBlockUpdate;
