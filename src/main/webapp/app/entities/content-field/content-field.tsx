import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContentField } from 'app/shared/model/content-field.model';
import { getEntities } from './content-field.reducer';

export const ContentField = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const contentFieldList = useAppSelector(state => state.contentField.entities);
  const loading = useAppSelector(state => state.contentField.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="content-field-heading" data-cy="ContentFieldHeading">
        <Translate contentKey="esigningApp.contentField.home.title">Content Fields</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.contentField.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/content-field/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.contentField.home.createLabel">Create new Content Field</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {contentFieldList && contentFieldList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.contentField.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.contentType">Content Type</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.bbox">Bbox</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.createDate">Create Date</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.createBy">Create By</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.updateDate">Update Date</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.updateBy">Update By</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.document">Document</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.contentField.signatory">Signatory</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contentFieldList.map((contentField, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/content-field/${contentField.id}`} color="link" size="sm">
                      {contentField.id}
                    </Button>
                  </td>
                  <td>{contentField.contentType}</td>
                  <td>{contentField.bbox}</td>
                  <td>
                    {contentField.createDate ? <TextFormat type="date" value={contentField.createDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{contentField.createBy}</td>
                  <td>
                    {contentField.updateDate ? <TextFormat type="date" value={contentField.updateDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{contentField.updateBy}</td>
                  <td>
                    {contentField.document ? <Link to={`/document/${contentField.document.id}`}>{contentField.document.title}</Link> : ''}
                  </td>
                  <td>{contentField.signatory ? contentField.signatory.email : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/content-field/${contentField.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/content-field/${contentField.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/content-field/${contentField.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="esigningApp.contentField.home.notFound">No Content Fields found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ContentField;
