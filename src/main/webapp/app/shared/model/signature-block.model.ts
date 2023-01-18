import { IUser } from 'app/shared/model/user.model';
import { IContentField } from 'app/shared/model/content-field.model';

export interface ISignatureBlock {
  id?: number;
  styling?: string | null;
  pubKey?: string;
  pubKeyFingerprint?: string;
  user?: IUser | null;
  contentField?: IContentField | null;
}

export const defaultValue: Readonly<ISignatureBlock> = {};
