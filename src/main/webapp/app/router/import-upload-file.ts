import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

const ApplicationImportUploadFile = () => import('@/entities/application-import/application-import-upload-file.vue');
// prettier-ignore
const FlowImportUploadFile = () => import('@/entities/flow-import/flow-import-upload-file.vue');
// prettier-ignore
const EventImportUploadFile = () => import('@/entities/data-flow-import/data-flow-import-upload-file.vue');
// prettier-ignore

export default [
  {
    path: '/application-import-upload-file',
    name: 'ApplicationImportUploadFile',
    component: ApplicationImportUploadFile,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/flow-import-upload-file',
    name: 'FlowImportUploadFile',
    component: FlowImportUploadFile,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/event-import-upload-file',
    name: 'EventImportUploadFile',
    component: EventImportUploadFile,
    meta: { authorities: [Authority.USER] },
  },  
];
