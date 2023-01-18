import './signer.scss';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IFileSigner } from 'app/shared/model/signer.model';
import { getSession } from 'app/shared/reducers/authentication';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { useDropzone } from 'react-dropzone';
import { Link } from 'react-router-dom';
import { Col, Row } from 'reactstrap';
import { StepLayout } from './signer-components';
import { createEntity } from './signer.reducer';

function SignerUpload({ open }: any) {
  const [files, setFiles] = useState([]);
  const titleField = useRef<HTMLInputElement | undefined>();

  const currentUser = useAppSelector(state => state.authentication.account);
  const signer = useAppSelector(state => state.signer.entity);

  const onDrop = useCallback(acceptedFiles => {
    const newFiles = [];

    acceptedFiles.map((file: any) => {
      const uploadData: IFileSigner = {
        file,
        title: titleField.current.value,
        user: currentUser,
      };

      dispatch(createEntity(uploadData));

      newFiles.push(
        <li key={file.lastModified}>
          {titleField.current.value} - {file.path} - {file.size} bytes
        </li>
      );
    });

    setFiles(newFiles);
  }, []);

  const { getRootProps, getInputProps } = useDropzone({ onDrop });
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getSession());
  }, []);

  return (
    <>
      <StepLayout currentPage={1} />
      <Row className="align-items-center mb-2">
        <Col>
          <div className="mb-2">
            <span className="fs-5 fw-bold">Upload Document</span>
          </div>
          <div className="mb-2 d-flex flex-column">
            <label className="me-2">Document title</label>
            <input id="documentTitle" name="documentTitle" ref={titleField} />
          </div>
          <div {...getRootProps({ className: 'dropzone' })} className="dropzone-body">
            <input className="input-zone" {...getInputProps()} />
            <div className="flex text-center">
              <p className="dropzone-content">Drag and drop some files here, or click to select files</p>
              <button type="button" onClick={open} className="btn btn-sm btn-upload">
                Upload
              </button>
            </div>
          </div>
        </Col>
        <Col>
          {files.length > 0 ? (
            <aside>
              <ul>{files}</ul>
            </aside>
          ) : (
            <div className="d-flex flex-column align-items-center">
              <img src="../../../../content/images/empty-folder.svg" alt="Logo" />
              <span>There are no files uploaded</span>
              <span>uploaded file will be shown here</span>
            </div>
          )}
        </Col>
      </Row>
      <Row>
        <div className="d-flex justify-content-end">
          <Link
            to={`/signer/add`}
            state={{ document: signer }}
            className="btn btn-primary add-signer"
            id="add-signer"
            data-cy="addSignerButton"
          >
            Next
          </Link>
        </div>
      </Row>
    </>
  );
}

export default SignerUpload;
