import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAuditTrail } from 'app/shared/model/audit-trail.model';
import { getEntities } from './audit-trail.reducer';

export const AuditTrail = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const auditTrailList = useAppSelector(state => state.auditTrail.entities);
  const loading = useAppSelector(state => state.auditTrail.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="audit-trail-heading" data-cy="AuditTrailHeading">
        <Translate contentKey="esigningApp.auditTrail.home.title">Audit Trails</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="esigningApp.auditTrail.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/audit-trail/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="esigningApp.auditTrail.home.createLabel">Create new Audit Trail</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {auditTrailList && auditTrailList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.activity">Activity</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.ipaddress">Ipaddress</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.time">Time</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.document">Document</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.auditTrail.document">Document</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {auditTrailList.map((auditTrail, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/audit-trail/${auditTrail.id}`} color="link" size="sm">
                      {auditTrail.id}
                    </Button>
                  </td>
                  <td>{auditTrail.activity}</td>
                  <td>{auditTrail.description}</td>
                  <td>{auditTrail.ipaddress}</td>
                  <td>{auditTrail.time ? <TextFormat type="date" value={auditTrail.time} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{auditTrail.document ? <Link to={`/document/${auditTrail.document.id}`}>{auditTrail.document.title}</Link> : ''}</td>
                  <td>{auditTrail.user ? auditTrail.user.email : ''}</td>
                  <td>{auditTrail.document ? <Link to={`/document/${auditTrail.document.id}`}>{auditTrail.document.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/audit-trail/${auditTrail.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/audit-trail/${auditTrail.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/audit-trail/${auditTrail.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="esigningApp.auditTrail.home.notFound">No Audit Trails found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AuditTrail;
