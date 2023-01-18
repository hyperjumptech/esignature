import { IDocument } from './document.model';
import { IUser } from './user.model';

export interface IFileSigner {
  id?: number;
  file?: any;
  title?: string;
  isOwner?: boolean | null;
  document?: IDocument | null;
  user?: IUser | null;
}

export interface IFileSignerInput {
  documentId?: number;
  documentParticipantId?: number;
}

export const defaultValue: Readonly<IFileSigner> = {};
