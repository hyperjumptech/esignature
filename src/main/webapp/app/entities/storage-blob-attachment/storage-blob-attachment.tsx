import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStorageBlobAttachment } from 'app/shared/model/storage-blob-attachment.model';
import { getEntities } from './storage-blob-attachment.reducer';

export const StorageBlobAttachment = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const storageBlobAttachmentList = useAppSelector(state => state.storageBlobAttachment.entities);
  const loading = useAppSelector(state => state.storageBlobAttachment.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="storage-blob-attachment-heading" data-cy="StorageBlobAttachmentHeading">
        <Translate contentKey="esigningApp.storageBlobAttachment.home.title">Storage Blob Attachments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.storageBlobAttachment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/storage-blob-attachment/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.storageBlobAttachment.home.createLabel">Create new Storage Blob Attachment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {storageBlobAttachmentList && storageBlobAttachmentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.storageBlobAttachment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlobAttachment.attachmentName">Attachment Name</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlobAttachment.recordType">Record Type</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlobAttachment.recordId">Record Id</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlobAttachment.sblob">Sblob</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlobAttachment.storageBlob">Storage Blob</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {storageBlobAttachmentList.map((storageBlobAttachment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/storage-blob-attachment/${storageBlobAttachment.id}`} color="link" size="sm">
                      {storageBlobAttachment.id}
                    </Button>
                  </td>
                  <td>{storageBlobAttachment.attachmentName}</td>
                  <td>{storageBlobAttachment.recordType}</td>
                  <td>{storageBlobAttachment.recordId}</td>
                  <td>
                    {storageBlobAttachment.sblob ? (
                      <Link to={`/storage-blob/${storageBlobAttachment.sblob.id}`}>{storageBlobAttachment.sblob.filename}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {storageBlobAttachment.storageBlob ? (
                      <Link to={`/storage-blob/${storageBlobAttachment.storageBlob.id}`}>{storageBlobAttachment.storageBlob.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/storage-blob-attachment/${storageBlobAttachment.id}`}
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
                        to={`/storage-blob-attachment/${storageBlobAttachment.id}/edit`}
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
                        to={`/storage-blob-attachment/${storageBlobAttachment.id}/delete`}
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
              <Translate contentKey="esigningApp.storageBlobAttachment.home.notFound">No Storage Blob Attachments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default StorageBlobAttachment;
