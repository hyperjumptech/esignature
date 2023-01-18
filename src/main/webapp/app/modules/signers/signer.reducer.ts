import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { IDocumentParticipant } from 'app/shared/model/document-participant.model';
import { IDocument } from 'app/shared/model/document.model';
import { IFile } from 'app/shared/model/file.model';
import { defaultValue, IFileSigner, IFileSignerInput } from 'app/shared/model/signer.model';
import { IStorageBlob } from 'app/shared/model/storage-blob.model';
import { IUser } from 'app/shared/model/user.model';
import { createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { cleanEntity } from 'app/shared/util/entity-utils';
import axios from 'axios';

const initialState: EntityState<IFileSigner> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const filesUrl = 'api/files';
const storageUrl = 'api/storage-blobs';
const documentUrl = 'api/documents';
const documentParticipantUrl = 'api/document-participants';
const mailingUrl = 'api/signatory-email-sender/send-invitation';

export const getEntities = createAsyncThunk('signer/fetch_entity_list', async (documentId: number) => {
  if (documentId) {
    const requestUrl = `${documentUrl}/${documentId}?cacheBuster=${new Date().getTime()}`;
    const docResult = await axios.get<IDocument>(requestUrl);

    await Promise.all(
      docResult.data.participants.map(parts => {
        console.log(`---- doc result: ${JSON.stringify(parts.id)}`);
      })
    );
  }
  const requestUrl = `${documentParticipantUrl}?cacheBuster=${new Date().getTime()}`;
  const result = await axios.get<IDocumentParticipant[]>(requestUrl);
  const filtered = result.data
    .map((docPart: IDocumentParticipant) => {
      if (docPart?.document?.id == documentId) {
        return docPart;
      }
    })
    .filter(el => el != null);

  return filtered;
});

export const getEntity = createAsyncThunk(
  'signer/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${documentParticipantUrl}/${id}`;
    return axios.get<IDocumentParticipant>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createSignImage = createAsyncThunk(
  'signer/create_sign',
  async (data: { signImage: String; user: IUser }, thunkAPI) => {
    const currentDate = new Date();
    const currentUser = data.user.login;
    return new Promise<IDocument>(async (resolve, reject) => {
      try {
        const saveSignatureUrl = `api/signature-image/save-signature`;
        const sign = await axios.post<String>(saveSignatureUrl, data.signImage, {
          headers: {
            'Content-Type': 'text/plain',
          },
        });

        console.log('ini sign', sign);

        const documentEntity: IDocument = {
          title: `sign-${currentUser}`,
          createBy: currentUser,
          createDate: currentDate.toISOString(),
          updateBy: currentUser,
          updateDate: currentDate.toISOString(),
        };
        const documentResult = await axios.post<IDocument>(documentUrl, cleanEntity(documentEntity));

        const storageEntity: IStorageBlob = {
          filename: sign.data.toString(),
          byteSize: 0,
          contentType: 'image/png',
          document: documentResult.data,
          createBy: currentUser,
          createDate: currentDate.toISOString(),
          updateBy: currentUser,
          updateDate: currentDate.toISOString(),
        };
        await axios.post<IStorageBlob>(storageUrl, cleanEntity(storageEntity));

        const documentParticipantEntity: IDocumentParticipant = {
          document: documentResult.data,
          user: data.user,
          isOwner: true,
        };
        const documentParticipantResult = await axios.post<IDocumentParticipant>(
          documentParticipantUrl,
          cleanEntity(documentParticipantEntity)
        );

        return resolve(documentParticipantResult.data);
      } catch (error) {
        return reject(error);
      }
    });
  },
  {
    serializeError: serializeAxiosError,
  }
);

export const createEntity = createAsyncThunk(
  'signer/create_entity',
  async (data: IFileSigner, thunkAPI) => {
    const currentDate = new Date();
    const currentUser = data.user.login;
    return new Promise<IDocument>(async (resolve, reject) => {
      try {
        const filesEntity: IFile = {
          file: data.file,
        };

        const fileUploadUrl = `${filesUrl}/upload`;
        await axios.post<IFile>(fileUploadUrl, cleanEntity(filesEntity), {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        const documentEntity: IDocument = {
          title: data.title,
          createBy: currentUser,
          createDate: currentDate.toISOString(),
          updateBy: currentUser,
          updateDate: currentDate.toISOString(),
        };
        const documentResult = await axios.post<IDocument>(documentUrl, cleanEntity(documentEntity));

        const fileExt = data.file.path.split('.').pop();
        const storageEntity: IStorageBlob = {
          filename: data.file.path,
          byteSize: data.file.size,
          contentType: fileExt,
          document: documentResult.data,
          createBy: currentUser,
          createDate: currentDate.toISOString(),
          updateBy: currentUser,
          updateDate: currentDate.toISOString(),
        };
        await axios.post<IStorageBlob>(storageUrl, cleanEntity(storageEntity));

        const documentParticipantEntity: IDocumentParticipant = {
          document: documentResult.data,
          user: data.user,
          isOwner: true,
        };
        const documentParticipantResult = await axios.post<IDocumentParticipant>(
          documentParticipantUrl,
          cleanEntity(documentParticipantEntity)
        );

        const result: IFileSigner = {
          id: documentParticipantResult.data.id,
          document: documentResult.data,
          file: data.file,
          isOwner: true,
          title: data.title,
          user: data.user,
        };

        return resolve(result);
      } catch (error) {
        return reject(error);
      }
    });
  },
  {
    serializeError: serializeAxiosError,
  }
);

export const addFileSigner = createAsyncThunk(
  'signer/add_signer',
  async (data: IDocumentParticipant, thunkAPI) => {
    const documentParticipantEntity: IDocumentParticipant = {
      document: data.document,
      user: data.user,
      isOwner: false,
    };
    const result = await axios.post<IDocumentParticipant>(documentParticipantUrl, cleanEntity(documentParticipantEntity));
    thunkAPI.dispatch(getEntities(result.data.document.id));

    return result;
  },
  {
    serializeError: serializeAxiosError,
  }
);

export const deleteEntity = createAsyncThunk(
  'signer/delete_entity',
  async (data: IFileSignerInput, thunkAPI) => {
    const requestUrl = `${documentParticipantUrl}/${data.documentParticipantId}`;
    const result = await axios.delete<IDocumentParticipant>(requestUrl);
    thunkAPI.dispatch(getEntities(data.documentId));
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

export const SignerSlice = createEntitySlice({
  name: 'signer',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntities.fulfilled, (state, action) => {
        const filesignerList: IFileSigner[] = [];
        if (action.payload) {
          action.payload.map((dp: IDocumentParticipant) => {
            filesignerList.push({
              id: dp.id,
              document: dp.document,
              user: dp.user,
              isOwner: dp.isOwner,
            });
          });
        }

        state.loading = false;
        state.entities = filesignerList;
      })
      .addCase(getEntity.fulfilled, (state, action) => {
        const filesigner: IFileSigner = {};
        if (action.payload) {
          filesigner.id = action.payload.data.id;
          filesigner.document = action.payload.data.document;
          filesigner.user = action.payload.data.user;
          filesigner.isOwner = action.payload.data.isOwner;
          filesigner.title = action.payload.data.document.title;
        }

        state.loading = false;
        state.entity = filesigner;
      })
      .addCase(createEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = { ...state.entity, ...action.payload };
      })
      .addCase(createEntity.pending, (state, action) => {
        state.updating = true;
        state.loading = true;
      })
      .addCase(sendEmail.fulfilled, (state, action) => {
        state.updating = false;
        state.loading = false;
      })
      .addMatcher(isPending(sendEmail), state => {
        state.updating = true;
        state.loading = true;
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload,
        };
      })
      .addMatcher(isPending(getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = SignerSlice.actions;

// Reducer
export default SignerSlice.reducer;
