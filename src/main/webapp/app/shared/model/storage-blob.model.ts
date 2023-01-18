import dayjs from 'dayjs';
import { IStorageBlobAttachment } from 'app/shared/model/storage-blob-attachment.model';
import { IDocument } from 'app/shared/model/document.model';

export interface IStorageBlob {
  id?: number;
  key?: string | null;
  path?: string | null;
  filename?: string | null;
  contentType?: string | null;
  metadata?: string | null;
  byteSize?: number | null;
  checksum?: string | null;
  createDate?: string | null;
  createBy?: string | null;
  updateDate?: string | null;
  updateBy?: string | null;
  attachments?: IStorageBlobAttachment[] | null;
  document?: IDocument | null;
}

export const defaultValue: Readonly<IStorageBlob> = {};
