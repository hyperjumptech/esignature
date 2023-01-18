import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISignatureBlock } from 'app/shared/model/signature-block.model';
import { getEntities } from './signature-block.reducer';

export const SignatureBlock = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const signatureBlockList = useAppSelector(state => state.signatureBlock.entities);
  const loading = useAppSelector(state => state.signatureBlock.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="signature-block-heading" data-cy="SignatureBlockHeading">
        <Translate contentKey="esigningApp.signatureBlock.home.title">Signature Blocks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.signatureBlock.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/signature-block/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.signatureBlock.home.createLabel">Create new Signature Block</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {signatureBlockList && signatureBlockList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.signatureBlock.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.signatureBlock.styling">Styling</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.signatureBlock.pubKey">Pub Key</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.signatureBlock.pubKeyFingerprint">Pub Key Fingerprint</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.signatureBlock.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.signatureBlock.contentField">Content Field</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {signatureBlockList.map((signatureBlock, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/signature-block/${signatureBlock.id}`} color="link" size="sm">
                      {signatureBlock.id}
                    </Button>
                  </td>
                  <td>{signatureBlock.styling}</td>
                  <td>{signatureBlock.pubKey}</td>
                  <td>{signatureBlock.pubKeyFingerprint}</td>
                  <td>{signatureBlock.user ? signatureBlock.user.email : ''}</td>
                  <td>
                    {signatureBlock.contentField ? (
                      <Link to={`/content-field/${signatureBlock.contentField.id}`}>{signatureBlock.contentField.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/signature-block/${signatureBlock.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/signature-block/${signatureBlock.id}/edit`}
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
                        to={`/signature-block/${signatureBlock.id}/delete`}
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
              <Translate contentKey="esigningApp.signatureBlock.home.notFound">No Signature Blocks found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SignatureBlock;
