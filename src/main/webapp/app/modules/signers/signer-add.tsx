import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IDocumentParticipant } from 'app/shared/model/document-participant.model';
import { getSession } from 'app/shared/reducers/authentication';
import React, { useEffect } from 'react';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getUsers } from '../administration/user-management/user-management.reducer';
import { StepLayout } from './signer-components';
import { addFileSigner, getEntities, getEntity, sendEmail } from './signer.reducer';

function AddSigner() {
  const dispatch = useAppDispatch();

  const loading = useAppSelector(state => state.documentParticipant.loading);
  const users = useAppSelector(state => state.userManagement.users);
  const signerEntity = useAppSelector(state => state.signer.entity);
  const documentParticipantList = useAppSelector(state => state.signer.entities);

  const handleValidSubmit = ({ user, document }) => {
    const documentParticipantEntity: IDocumentParticipant = {
      document: signerEntity.document,
      user: { id: user },
    };
    dispatch(addFileSigner(documentParticipantEntity));
  };

  const handleSendEmail = () => {
    console.log(`Sending email for document: ${signerEntity.document.title}`);
    dispatch(sendEmail(signerEntity.document.id));
  };

  useEffect(() => {
    dispatch(getUsers({}));
    dispatch(getSession());
    dispatch(getEntities(signerEntity?.document?.id));
    dispatch(getEntity(signerEntity.id));
  }, []);

  return (
    <>
      <StepLayout currentPage={2} />
      <div className="d-flex flex-column">
        <div className="mb-2">
          <span className="fs-5 fw-bold">Add Signer</span>
        </div>
        <div className="mb-4">
          <ValidatedForm onSubmit={handleValidSubmit}>
            <ValidatedField
              id="document-participant-document"
              name="document"
              data-cy="document"
              label={translate('esigningApp.documentParticipant.document')}
              type="text"
              key={signerEntity?.document?.id}
              disabled={true}
              value={signerEntity?.title}
            />{' '}
            <ValidatedField
              id="signer-user"
              name="user"
              data-cy="user"
              label={translate('esigningApp.documentParticipant.user')}
              type="select"
            >
              <option value="" key="0" />
              {users
                ? users.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.login}
                    </option>
                  ))
                : null}
            </ValidatedField>
            <div className="d-flex justify-content-end">
              <div className="mx-2">
                <Button
                  color="primary"
                  className="rounded"
                  id="save-entity"
                  data-cy="entityCreateSaveButton"
                  type="submit"
                  disabled={loading}
                >
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </div>
            </div>
          </ValidatedForm>
        </div>
        <div>
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
                        {documentParticipant?.document ? (
                          <Link to={`/document/${documentParticipant?.document?.id}`}>{documentParticipant?.document?.title}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td>{documentParticipant.user ? documentParticipant.user.email : ''}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button
                            tag={Link}
                            to={`/signer/${documentParticipant.id}/delete`}
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
                  <Translate contentKey="esigningApp.signer.emptysigner">No signer assigned yet</Translate>
                </div>
              )
            )}
          </div>
          <div className="d-flex justify-content-end">
            <Link
              to="/signer"
              state={{ document: signerEntity.document }}
              className="btn btn-primary mx-2"
              id="upload-signer"
              data-cy="uploadSignerButton"
            >
              back
            </Link>
            <Button className="me-2" color="info" onClick={handleSendEmail} disabled={loading}>
              <Translate contentKey="esigningApp.signer.sendmail">Send Email for Signing</Translate>
            </Button>
          </div>
        </div>
      </div>
    </>
  );
}

export default AddSigner;
