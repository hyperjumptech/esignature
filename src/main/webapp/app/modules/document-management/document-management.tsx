import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { Link } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getEntities, useDownloadFile } from './document-management.reducer';

const DocumentManagement = () => {
  const dispatch = useAppDispatch();
  const [isMenuOpen, setMenuopen] = useState<boolean>(false);
  const loading = useAppSelector(state => state.documentManagement.loading);
  const document = useAppSelector(state => state.documentManagement.entity);
  const documentManagementList = useAppSelector(state => state.documentManagement.entities);

  const toggle = () => {
    const value = !isMenuOpen ? true : false;
    setMenuopen(value);
  };

  const handleDownload = (filename: string) => {
    dispatch(useDownloadFile(filename));
  };

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  return (
    <div>
      <div id="document-management-header" className="my-3">
        <div className="fs-2 fw-bold">
          <Translate contentKey="esigningApp.documentManagement.title" />
        </div>
      </div>
      <div id="document-management-body">
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="esigningApp.documentManagement.table.name">Document Name</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.documentManagement.table.size">File Size</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.documentManagement.table.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="esigningApp.documentManagement.table.update">Update</Translate>
                </th>
              </tr>
            </thead>
            <tbody>
              {documentManagementList && documentManagementList.length > 0
                ? documentManagementList.map((documentManagement, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>
                        <div className="d-flex flex-row">
                          <div className="mx-2">
                            {/* <img src="../../../../content/images/file-pdf-solid.svg" className="img-thumbnail" alt="file" /> */}
                          </div>
                          <div>{documentManagement.title}</div>
                        </div>
                      </td>
                      <td>{documentManagement.size}</td>
                      <td>{documentManagement.status}</td>
                      <td>{documentManagement.updated}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`/document/${documentManagement.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                            <FontAwesomeIcon icon="eye" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="esigningApp.documentManagement.dotsmenu.view">View Info</Translate>
                            </span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/document/${documentManagement.id}/edit`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            {/* <FontAwesomeIcon icon="mail-forward" />{' '} */}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="esigningApp.documentManagement.dotsmenu.send">Send Copy of Document</Translate>
                            </span>
                          </Button>
                          <a href={document.url} download={document.name} className="hidden" ref={document.ref} />
                          <Button
                            onClick={() => handleDownload(documentManagement.name)}
                            color="success"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            {/* <FontAwesomeIcon icon="download" />{' '} */}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="esigningApp.documentManagement.dotsmenu.download">Download PDF</Translate>
                            </span>
                          </Button>
                        </div>
                      </td>
                      {/* <div className="btn-group flex-btn-group-container">
                          <Dropdown isOpen={isMenuOpen} toggle={toggle}>
                            <DropdownToggle>
                              <img src="../../../../content/images/threedots.svg" alt="menu" />
                            </DropdownToggle>

                            <DropdownMenu variant="dark">
                              <DropdownItem href="#">
                                <Translate contentKey="esigningApp.documentManagement.dotsmenu.download">Download PDF</Translate>
                              </DropdownItem>
                              <DropdownItem href="#">
                                <Translate contentKey="esigningApp.documentManagement.dotsmenu.send">Send Copy of Document</Translate>
                              </DropdownItem>
                              <DropdownItem href="#">
                                <Translate contentKey="esigningApp.documentManagement.dotsmenu.view">View Info</Translate>
                              </DropdownItem>
                            </DropdownMenu>
                          </Dropdown>
                        </div> */}
                    </tr>
                  ))
                : !loading && (
                    <tr>
                      <td className="d-flex justify-content-center">
                        <div className="d-flex flex-column align-items-center justify-content-center">
                          <img src="../../../../content/images/empty-folder.svg" alt="Logo" />
                          <span>There are no files uploaded</span>
                          <span>uploaded file will be shown here</span>
                        </div>
                      </td>
                    </tr>
                  )}
            </tbody>
          </Table>
        </div>
      </div>
    </div>
  );
};

export default DocumentManagement;
