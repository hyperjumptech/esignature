import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITextBlock } from 'app/shared/model/text-block.model';
import { getEntities } from './text-block.reducer';

export const TextBlock = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const textBlockList = useAppSelector(state => state.textBlock.entities);
  const loading = useAppSelector(state => state.textBlock.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="text-block-heading" data-cy="TextBlockHeading">
        <Translate contentKey="esigningApp.textBlock.home.title">Text Blocks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.textBlock.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/text-block/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.textBlock.home.createLabel">Create new Text Block</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {textBlockList && textBlockList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.textBlock.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.textBlock.textType">Text Type</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.textBlock.body">Body</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.textBlock.styling">Styling</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.textBlock.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {textBlockList.map((textBlock, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/text-block/${textBlock.id}`} color="link" size="sm">
                      {textBlock.id}
                    </Button>
                  </td>
                  <td>{textBlock.textType}</td>
                  <td>{textBlock.body}</td>
                  <td>{textBlock.styling}</td>
                  <td>{textBlock.user ? textBlock.user.email : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/text-block/${textBlock.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/text-block/${textBlock.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/text-block/${textBlock.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="esigningApp.textBlock.home.notFound">No Text Blocks found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TextBlock;
