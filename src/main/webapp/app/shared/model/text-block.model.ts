import { IUser } from 'app/shared/model/user.model';

export interface ITextBlock {
  id?: number;
  textType?: string | null;
  body?: string | null;
  styling?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITextBlock> = {};
