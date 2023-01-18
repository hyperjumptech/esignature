import { createAsyncThunk } from '@reduxjs/toolkit';
import { IStorageBlob } from 'app/shared/model/storage-blob.model';
import { createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import axios from 'axios';

const initialState: EntityState<Blob> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: null,
  updating: false,
  updateSuccess: false,
};

const storageUrl = 'api/storage-blobs';
const fileUrl = 'api/files';

export const getByDocument = createAsyncThunk('storageBlob/bydocument', async (id: string | number, thunkAPI) => {
  const requestUrl = `${storageUrl}/bydocument/${id}`;
  const docId = await axios.get<IStorageBlob>(requestUrl);
  const file = await axios.get<Blob>(fileUrl + '/' + docId.data.filename, {
    responseType: 'blob',
  });
  return file;
});

export const Sign = createEntitySlice({
  name: 'sign',
  initialState,
  extraReducers(builder) {
    builder.addCase(getByDocument.fulfilled, (state, action) => {
      let storageBlob: Blob;
      if (action.payload) {
        storageBlob = action.payload.data;
      }

      state.loading = false;
      state.entity = storageBlob;
    });
  },
});

export const { reset } = Sign.actions;

// Reducer
export default Sign.reducer;
