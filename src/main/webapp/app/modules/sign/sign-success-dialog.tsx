import React, { useState } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Nav, NavLink, NavItem } from 'reactstrap';

export const SignSuccessDialog = args => {
  const isOpen = args.isOpen ? args.isOpen : false;
  const [modal, setModal] = useState(isOpen);
  const toggle = () => {
    setModal(!modal);
    args.onClose();
  };

  return (
    <Modal isOpen={modal} size="md" centered="true" toggle={toggle} {...args}>
      <ModalBody>
        <div className="py-3" style={{ maxWidth: '300px', margin: 'auto' }}>
          <div className="d-flex justify-content-center pb-3">
            <svg width="48" height="48" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect width="48" height="48" rx="24" fill="#D1FAE5" />
              <path d="M17 25L21 29L31 19" stroke="#059669" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </div>
          <h4 className="text-center">Sign Successful!</h4>
          <p className="text-center h6 pt-3 pb-4">
            <small className="muted">Thank you for submitting your document. You'll receive a copy in your inbox shortly.</small>
          </p>

          <Button
            className="w-100"
            color="primary"
            onClick={() => {
              toggle();
            }}
          >
            Close
          </Button>
        </div>
      </ModalBody>
    </Modal>
  );
};

export default SignSuccessDialog;
