import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContentField } from 'app/shared/model/content-field.model';
import { getEntities as getContentFields } from 'app/entities/content-field/content-field.reducer';
import { ISentinelBlock } from 'app/shared/model/sentinel-block.model';
import { getEntity, updateEntity, createEntity, reset } from './sentinel-block.reducer';

export const SentinelBlockUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentFields = useAppSelector(state => state.contentField.entities);
  const sentinelBlockEntity = useAppSelector(state => state.sentinelBlock.entity);
  const loading = useAppSelector(state => state.sentinelBlock.loading);
  const updating = useAppSelector(state => state.sentinelBlock.updating);
  const updateSuccess = useAppSelector(state => state.sentinelBlock.updateSuccess);

  const handleClose = () => {
    navigate('/sentinel-block');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getContentFields({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...sentinelBlockEntity,
      ...values,
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
          ...sentinelBlockEntity,
          contentField: sentinelBlockEntity?.contentField?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="esigningApp.sentinelBlock.home.createOrEditLabel" data-cy="SentinelBlockCreateUpdateHeading">
            <Translate contentKey="esigningApp.sentinelBlock.home.createOrEditLabel">Create or edit a SentinelBlock</Translate>
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
                  id="sentinel-block-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('esigningApp.sentinelBlock.blockType')}
                id="sentinel-block-blockType"
                name="blockType"
                data-cy="blockType"
                type="text"
              />
              <ValidatedField
                label={translate('esigningApp.sentinelBlock.placeholder')}
                id="sentinel-block-placeholder"
                name="placeholder"
                data-cy="placeholder"
                type="text"
              />
              <ValidatedField
                id="sentinel-block-contentField"
                name="contentField"
                data-cy="contentField"
                label={translate('esigningApp.sentinelBlock.contentField')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sentinel-block" replace color="info">
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

export default SentinelBlockUpdate;
