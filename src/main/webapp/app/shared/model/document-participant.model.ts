import { IDocument } from 'app/shared/model/document.model';
import { IUser } from 'app/shared/model/user.model';

export interface IDocumentParticipant {
  id?: number;
  isOwner?: boolean | null;
  document?: IDocument | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IDocumentParticipant> = {
  isOwner: false,
};
