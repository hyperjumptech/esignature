import { useAppDispatch } from 'app/config/store';
import React from 'react';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Button } from 'reactstrap';

export function DocumentManagementSearch() {
  const dispatch = useAppDispatch();
  const handleValidSubmit = () => {};

  const statuses = [
    { id: 1, title: 'Pending Signature' },
    { id: 2, title: 'Pending Your Signature' },
    { id: 3, title: 'Declined' },
    { id: 4, title: 'Signed' },
    { id: 5, title: 'Draft' },
  ];

  return (
    <>
      <div className="documents-search-group">
        <div id="search-autocomplete" className="form-outline">
          <ValidatedForm onSubmit={handleValidSubmit}>
            <ValidatedField
              id="document-participant-document"
              name="document"
              data-cy="document"
              label={translate('esigningApp.documentManagement.search.title')}
              type="text"
              disabled={true}
            />
            <ValidatedField
              id="document-management-status"
              name="status"
              data-cy="status"
              label={translate('esigningApp.documentManagement.status')}
              type="select"
            >
              <option value="" key="0" />
              {statuses
                ? statuses.map(status => (
                    <option value={status.id} key={status.id}>
                      {status.title}
                    </option>
                  ))
                : null}
            </ValidatedField>
            <Button color="primary" type="submit" data-cy="submit">
              <Translate contentKey="esigningApp.documentManagement.search.button">Sign in</Translate>
            </Button>
          </ValidatedForm>
        </div>
        <button type="button" className="btn btn-primary">
          <i className="fas fa-search"></i>
        </button>
      </div>
    </>
  );
}

export default DocumentManagementSearch;
