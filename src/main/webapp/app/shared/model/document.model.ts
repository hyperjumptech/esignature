import dayjs from 'dayjs';
import { IDocumentParticipant } from 'app/shared/model/document-participant.model';
import { IContentField } from 'app/shared/model/content-field.model';
import { IStorageBlob } from 'app/shared/model/storage-blob.model';
import { IAuditTrail } from 'app/shared/model/audit-trail.model';

export interface IDocument {
  id?: number;
  title?: string;
  description?: string | null;
  createDate?: string | null;
  createBy?: string | null;
  updateDate?: string | null;
  updateBy?: string | null;
  participants?: IDocumentParticipant[] | null;
  fields?: IContentField[] | null;
  storages?: IStorageBlob[] | null;
  audittrails?: IAuditTrail[] | null;
}

export const defaultValue: Readonly<IDocument> = {};
