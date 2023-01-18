export interface IDocumentManagement {
  id?: number;
  title?: string | null;
  name?: string | null;
  ref?: any;
  url?: any;
  size?: number;
  status?: string | null;
  updated?: string | null;
}

export const defaultValue: Readonly<IDocumentManagement> = {};
