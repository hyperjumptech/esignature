import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStorageBlob } from 'app/shared/model/storage-blob.model';
import { getEntities } from './storage-blob.reducer';

export const StorageBlob = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const storageBlobList = useAppSelector(state => state.storageBlob.entities);
  const loading = useAppSelector(state => state.storageBlob.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="storage-blob-heading" data-cy="StorageBlobHeading">
        <Translate contentKey="esigningApp.storageBlob.home.title">Storage Blobs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.storageBlob.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/storage-blob/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.storageBlob.home.createLabel">Create new Storage Blob</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {storageBlobList && storageBlobList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.key">Key</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.path">Path</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.filename">Filename</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.contentType">Content Type</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.metadata">Metadata</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.byteSize">Byte Size</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.checksum">Checksum</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.createDate">Create Date</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.createBy">Create By</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.updateDate">Update Date</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.updateBy">Update By</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.storageBlob.document">Document</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {storageBlobList.map((storageBlob, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/storage-blob/${storageBlob.id}`} color="link" size="sm">
                      {storageBlob.id}
                    </Button>
                  </td>
                  <td>{storageBlob.key}</td>
                  <td>{storageBlob.path}</td>
                  <td>{storageBlob.filename}</td>
                  <td>{storageBlob.contentType}</td>
                  <td>{storageBlob.metadata}</td>
                  <td>{storageBlob.byteSize}</td>
                  <td>{storageBlob.checksum}</td>
                  <td>
                    {storageBlob.createDate ? <TextFormat type="date" value={storageBlob.createDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{storageBlob.createBy}</td>
                  <td>
                    {storageBlob.updateDate ? <TextFormat type="date" value={storageBlob.updateDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{storageBlob.updateBy}</td>
                  <td>
                    {storageBlob.document ? <Link to={`/document/${storageBlob.document.id}`}>{storageBlob.document.title}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/storage-blob/${storageBlob.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/storage-blob/${storageBlob.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/storage-blob/${storageBlob.id}/delete`}
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
              <Translate contentKey="esigningApp.storageBlob.home.notFound">No Storage Blobs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default StorageBlob;
