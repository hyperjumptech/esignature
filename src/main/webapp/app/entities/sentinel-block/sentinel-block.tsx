import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISentinelBlock } from 'app/shared/model/sentinel-block.model';
import { getEntities } from './sentinel-block.reducer';

export const SentinelBlock = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const sentinelBlockList = useAppSelector(state => state.sentinelBlock.entities);
  const loading = useAppSelector(state => state.sentinelBlock.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="sentinel-block-heading" data-cy="SentinelBlockHeading">
        <Translate contentKey="esigningApp.sentinelBlock.home.title">Sentinel Blocks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.sentinelBlock.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/sentinel-block/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.sentinelBlock.home.createLabel">Create new Sentinel Block</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {sentinelBlockList && sentinelBlockList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.sentinelBlock.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.sentinelBlock.blockType">Block Type</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.sentinelBlock.placeholder">Placeholder</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.sentinelBlock.contentField">Content Field</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sentinelBlockList.map((sentinelBlock, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/sentinel-block/${sentinelBlock.id}`} color="link" size="sm">
                      {sentinelBlock.id}
                    </Button>
                  </td>
                  <td>{sentinelBlock.blockType}</td>
                  <td>{sentinelBlock.placeholder}</td>
                  <td>
                    {sentinelBlock.contentField ? (
                      <Link to={`/content-field/${sentinelBlock.contentField.id}`}>{sentinelBlock.contentField.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/sentinel-block/${sentinelBlock.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/sentinel-block/${sentinelBlock.id}/edit`}
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
                        to={`/sentinel-block/${sentinelBlock.id}/delete`}
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
              <Translate contentKey="esigningApp.sentinelBlock.home.notFound">No Sentinel Blocks found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SentinelBlock;
