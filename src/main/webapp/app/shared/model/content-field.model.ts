import dayjs from 'dayjs';
import { IDocument } from 'app/shared/model/document.model';
import { IUser } from 'app/shared/model/user.model';
import { ISignatureBlock } from 'app/shared/model/signature-block.model';
import { ISentinelBlock } from 'app/shared/model/sentinel-block.model';

export interface IContentField {
  id?: number;
  contentType?: string | null;
  bbox?: string | null;
  createDate?: string | null;
  createBy?: string | null;
  updateDate?: string | null;
  updateBy?: string | null;
  document?: IDocument | null;
  signatory?: IUser | null;
  signatureBlocks?: ISignatureBlock[] | null;
  sentinelBlocks?: ISentinelBlock[] | null;
}

export const defaultValue: Readonly<IContentField> = {};
