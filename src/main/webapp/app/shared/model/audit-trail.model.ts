import dayjs from 'dayjs';
import { IDocument } from 'app/shared/model/document.model';
import { IUser } from 'app/shared/model/user.model';

export interface IAuditTrail {
  id?: number;
  activity?: string | null;
  description?: string | null;
  ipaddress?: string | null;
  time?: string | null;
  document?: IDocument | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IAuditTrail> = {};
