import React, { useEffect, useState } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Nav, NavLink, NavItem } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createSignImage } from '../signers/signer.reducer';
import { getSession } from 'app/shared/reducers/authentication';

export const SignatureDialog = args => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getSession());
  }, []);
  const currentUser = useAppSelector(state => state.authentication.account);

  const isOpen = args.isOpen ? args.isOpen : false;
  const [modal, setModal] = useState(isOpen);

  const toggle = () => {
    args.onClose(false);
    setModal(!modal);
  };
  const sendSuccessMessage = () => {
    args.onClose(true);
    setModal(!modal);
  };

  const CanvasDocument = () => (
    <div style={{ backgroundColor: '#F9FAFC' }}>
      <canvas id="signatureCanvas" style={{ display: 'block', margin: 'auto', width: '300px', height: '100px' }} />
    </div>
  );

  const drawingCanvas = (text: string) => {
    const canvas = document.getElementById('signatureCanvas') as HTMLCanvasElement;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.font = '38px Montez';
    ctx.textAlign = 'center';
    ctx.fillText(text, canvas.width / 2, canvas.height / 2);
  };

  const getCanvasImage = () => {
    const canvas = document.getElementById('signatureCanvas') as HTMLCanvasElement;
    return canvas.toDataURL('image/jpg');
  };

  return (
    <Modal isOpen={modal} toggle={toggle} {...args}>
      <ModalHeader toggle={toggle}>Signature</ModalHeader>
      <ModalBody>
        <Nav tabs>
          <NavItem href="#">
            <NavLink disabled>Saved</NavLink>
          </NavItem>
          <NavItem href="#">
            <NavLink disabled>Draw</NavLink>
          </NavItem>
          <NavItem href="#type">
            <NavLink active>Type</NavLink>
          </NavItem>
        </Nav>
        <div id="type">
          <CanvasDocument />
          <div className="form-group py-1">
            <input
              placeholder="Enter your name"
              className="form-control"
              type="text"
              onChange={evt => {
                drawingCanvas(evt.target.value);
              }}
            />
          </div>
        </div>
      </ModalBody>
      <ModalFooter>
        <div className="w-100">
          <label htmlFor="agree">I understand this is a legal representation of my signature</label>
        </div>
        <Button
          color="primary"
          onClick={async () => {
            sendSuccessMessage();

            const base64 = getCanvasImage();

            console.log(base64);
            console.log(currentUser);
            dispatch(
              createSignImage({
                signImage: base64,
                user: currentUser,
              })
            );
          }}
        >
          Insert
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SignatureDialog;
