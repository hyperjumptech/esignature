import document from 'app/entities/document/document.reducer';
import documentParticipant from 'app/entities/document-participant/document-participant.reducer';
import signatureBlock from 'app/entities/signature-block/signature-block.reducer';
import sentinelBlock from 'app/entities/sentinel-block/sentinel-block.reducer';
import textBlock from 'app/entities/text-block/text-block.reducer';
import contentField from 'app/entities/content-field/content-field.reducer';
import storageBlob from 'app/entities/storage-blob/storage-blob.reducer';
import storageBlobAttachment from 'app/entities/storage-blob-attachment/storage-blob-attachment.reducer';
import auditTrail from 'app/entities/audit-trail/audit-trail.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  document,
  documentParticipant,
  signatureBlock,
  sentinelBlock,
  textBlock,
  contentField,
  storageBlob,
  storageBlobAttachment,
  auditTrail,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
