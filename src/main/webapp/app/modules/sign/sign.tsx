import { getByDocument } from './sign.reducer';
import React, { useEffect, useState } from 'react';
import { Document, Page } from 'react-pdf/dist/esm/entry.webpack5';
import { useParams } from 'react-router-dom';
import SignatureDialog from './signature-dialog';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import SignSuccessDialog from './sign-success-dialog';

export default function Sign() {
  const dispatch = useAppDispatch();

  const [visibleSignature, setVisibleSignature] = useState(false);
  const [successSign, setSuccessSign] = useState(false);

  const [url, setUrl] = useState(null);
  const [numPages, setNumPages] = useState(null);
  const [pageNumber, setPageNumber] = useState(1);

  const storageBlob = useAppSelector(state => state.sign.entity);
  useEffect(() => {
    if (storageBlob) {
      const file = window.URL.createObjectURL(storageBlob);
      setUrl(file);
    }
  }, [storageBlob]);

  const { id } = useParams<'id'>();
  useEffect(() => {
    dispatch(getByDocument(id));
  }, []);

  function onDocumentLoadSuccess({ numPages }) {
    setNumPages(numPages);
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <p>
          Page {pageNumber} of {numPages}
        </p>
        <div>
          <label
            style={{ cursor: 'pointer' }}
            onClick={() => {
              if (pageNumber - 1 > 0) {
                setPageNumber(pageNumber - 1);
              }
            }}
          >
            Prev
          </label>
          <span style={{ margin: '0 15px' }}>|</span>
          <label
            style={{ cursor: 'pointer' }}
            onClick={() => {
              if (pageNumber + 1 < numPages) {
                setPageNumber(pageNumber + 1);
              }
            }}
          >
            Next
          </label>
        </div>
        <div>
          <a className="btn btn-outline-primary btn-sm" href={url} download={id + '.pdf'}>
            Download
          </a>
          <span className="px-1"></span>
          <button type="button" className="btn btn-primary btn-sm" onClick={() => setVisibleSignature(true)}>
            Continue
          </button>
        </div>
      </div>

      <Document file={url} onLoadSuccess={onDocumentLoadSuccess}>
        <Page
          className="d-flex justify-content-center"
          renderMode="canvas"
          renderAnnotationLayer={false}
          renderTextLayer={false}
          pageNumber={pageNumber}
        />
      </Document>

      <SignatureDialog
        isOpen={visibleSignature}
        onClose={isSuccess => {
          setVisibleSignature(false);
          if (isSuccess) {
            setSuccessSign(true);
          }
        }}
      />

      <SignSuccessDialog
        isOpen={successSign}
        onClose={() => {
          setSuccessSign(false);
        }}
      />
    </div>
  );
}
