import { createAsyncThunk } from '@reduxjs/toolkit';
import { defaultValue, IDocumentManagement } from 'app/shared/model/document-management.model';
import { IDocument } from 'app/shared/model/document.model';
import { IStorageBlob } from 'app/shared/model/storage-blob.model';
import { createEntitySlice, EntityState, IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import axios from 'axios';

const initialState: EntityState<IDocumentManagement> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const storageUrl = 'api/storage-blobs';
const documentUrl = 'api/documents';
const documentParticipantUrl = 'api/document-participants';
const fileUrl = 'api/files';
const mailingUrl = 'api/signatory-email-sender/send-invitation';

export const getEntities = createAsyncThunk('documentManagement/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const documentManagementList: IDocumentManagement[] = [];

  const documentByUserUrl = `${documentUrl}/by_current_user?cacheBuster=${new Date().getTime()}`;
  const userDocuments = await axios.get<IDocument[]>(documentByUserUrl);

  await Promise.all(
    userDocuments.data.map(async doc => {
      const requestUrl = `${storageUrl}/bydocument/${doc.id}?cacheBuster=${new Date().getTime()}`;
      const storage = await axios.get<IStorageBlob>(requestUrl);

      documentManagementList.push({
        id: doc.id,
        title: doc.title,
        name: storage?.data?.filename,
        size: storage?.data?.byteSize,
        status: 'Pending Signature',
        updated: doc.updateDate,
      });
    })
  );

  return documentManagementList;
});

export const getEntity = createAsyncThunk(
  'documentManagement/fetch_entity',
  async (id: string | number) => {
    if (!id) {
      return;
    }
    return {};
  },
  { serializeError: serializeAxiosError }
);

export const useDownloadFile = createAsyncThunk(
  'documentManagement/download_file',
  async (name: string) => {
    if (!name) {
      return;
    }

    const downloadFilePath = `${fileUrl}/${name}?cacheBuster=${new Date().getTime()}`;
    const blobResult = await axios.get(downloadFilePath, { responseType: 'blob' });
    const url = URL.createObjectURL(new Blob([blobResult.data]));

    const result: IDocumentManagement = {
      name,
      url,
    };
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const sendEmail = createAsyncThunk(
  'signer/send_email',
  async (documentId: number, thunkAPI) => {
    const requestUrl = `${mailingUrl}/${documentId}`;
    const result = await axios.post<IDocument>(requestUrl);
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const DocumentManagementSlice = createEntitySlice({
  name: 'documentManagement',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntities.fulfilled, (state, action) => {
        state.loading = false;
        state.entities = action.payload;
      })
      .addCase(useDownloadFile.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload;
      })
      .addCase(useDownloadFile.pending, (state, action) => {
        state.loading = true;
        state.updating = true;
      })
      .addCase(sendEmail.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      });
  },
});

export const { reset } = DocumentManagementSlice.actions;

// Reducer
export default DocumentManagementSlice.reducer;
