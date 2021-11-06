import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

const ApplicationImportUploadFile = () => import('@/entities/application-import/application-import-upload-file.vue');
// prettier-ignore

export default [
  {
    path: '/application-import-upload-file',
    name: 'ApplicationImportUploadFile',
    component: ApplicationImportUploadFile,
    meta: { authorities: [Authority.USER] },
  },
];
