import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

const FlowImportUploadFile = () => import('@/entities/flow-import/flow-import-upload-file.vue');
// prettier-ignore

export default [
  {
    path: '/flow-import-upload-file',
    name: 'FlowImportUploadFile',
    component: FlowImportUploadFile,
    meta: { authorities: [Authority.USER] },
  },
];
