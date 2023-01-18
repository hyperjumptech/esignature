import React from 'react';
import './signer.scss';

export interface IStepProps {
  stepNumber: string;
  stepName: string;
  filled: boolean;
}

export interface IStepLayoutProps {
  currentPage: number;
}

export function Steps(props: IStepProps) {
  if (props.filled) {
    return (
      <div className="d-flex flex-row align-items-center md-2 steps">
        <div className="d-flex circle bg-primary">{props.stepNumber}</div>
        <div className="text">
          <span>{props.stepName}</span>
        </div>
      </div>
    );
  }

  return (
    <div className="d-flex flex-row align-items-center md-2 steps">
      <div className="d-flex circle">{props.stepNumber}</div>
      <div className="text">
        <span>{props.stepName}</span>
      </div>
    </div>
  );
}

export function StepLayout(props: IStepLayoutProps) {
  return (
    <div className="signer-steps">
      <div className="row">
        <div className="col-sm">
          <Steps stepNumber="01" stepName="Select documents" filled={props.currentPage > 1} />
        </div>
        <div className="col-sm">
          <Steps stepNumber="02" stepName="Add Signers" filled={false} />
        </div>
      </div>
    </div>
  );
}
