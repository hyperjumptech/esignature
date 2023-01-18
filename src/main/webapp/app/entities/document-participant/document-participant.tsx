import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDocumentParticipant } from 'app/shared/model/document-participant.model';
import { getEntities } from './document-participant.reducer';

export const DocumentParticipant = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const documentParticipantList = useAppSelector(state => state.documentParticipant.entities);
  const loading = useAppSelector(state => state.documentParticipant.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="document-participant-heading" data-cy="DocumentParticipantHeading">
        <Translate contentKey="esigningApp.documentParticipant.home.title">Document Participants</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.documentParticipant.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/document-participant/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.documentParticipant.home.createLabel">Create new Document Participant</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {documentParticipantList && documentParticipantList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.documentParticipant.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.documentParticipant.isOwner">Is Owner</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.documentParticipant.document">Document</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.documentParticipant.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.documentParticipant.document">Document</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {documentParticipantList.map((documentParticipant, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/document-participant/${documentParticipant.id}`} color="link" size="sm">
                      {documentParticipant.id}
                    </Button>
                  </td>
                  <td>{documentParticipant.isOwner ? 'true' : 'false'}</td>
                  <td>
                    {documentParticipant.document ? (
                      <Link to={`/document/${documentParticipant.document.id}`}>{documentParticipant.document.title}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{documentParticipant.user ? documentParticipant.user.email : ''}</td>
                  <td>
                    {documentParticipant.document ? (
                      <Link to={`/document/${documentParticipant.document.id}`}>{documentParticipant.document.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/document-participant/${documentParticipant.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/document-participant/${documentParticipant.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/document-participant/${documentParticipant.id}/delete`}
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
              <Translate contentKey="esigningApp.documentParticipant.home.notFound">No Document Participants found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DocumentParticipant;
