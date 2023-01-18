import { IStorageBlob } from 'app/shared/model/storage-blob.model';

export interface IStorageBlobAttachment {
  id?: number;
  attachmentName?: string | null;
  recordType?: string | null;
  recordId?: number | null;
  sblob?: IStorageBlob | null;
  storageBlob?: IStorageBlob | null;
}

export const defaultValue: Readonly<IStorageBlobAttachment> = {};
