import { IContentField } from 'app/shared/model/content-field.model';

export interface ISentinelBlock {
  id?: number;
  blockType?: string | null;
  placeholder?: string | null;
  contentField?: IContentField | null;
}

export const defaultValue: Readonly<ISentinelBlock> = {};
