import { Authority } from '@/shared/security/authority';
/* tslint:disable */

// prettier-ignore
const ApplicationImportUploadFile = () => import('@/entities/application-import/application-import-upload-file.vue');
// prettier-ignore
const ComponentImportUploadFile = () => import('@/entities/component-import/component-import-upload-file.vue');
// prettier-ignore
const FlowImportUploadMultiFile = () => import('@/entities/flow-import/flow-import-upload-multifile.vue');
// prettier-ignore
const EventImportUploadFile = () => import('@/entities/data-flow-import/data-flow-import-upload-file.vue');
// prettier-ignore
const CapabilityImportUploadFile = () => import('@/entities/capability-import/capability-import-upload-file.vue');
// prettier-ignore
const ApplicationCapabilityImportUploadFile = () => import('@/entities/capability-import/application-capability-import-upload-file.vue');
// prettier-ignore
const FlowReporting = () => import('@/entities/flow-interface/reporting-flow-interface.vue');
// prettier-ignore
const OrphanFlowGroupReporting = () => import('@/entities/flow-group/reporting-orphan-flow-group.vue');
// prettier-ignore
const ApplicationsDiagram = () => import('@/eadesignit/applications-diagram/applications-diagram.vue');
// prettier-ignore
const ExternalSystemUploadFile = () => import('@/entities/external-system/external-system-upload-file.vue');
// prettier-ignore
const FullExport = () => import('@/eadesignit/full-export/full-export.vue');

export default [
  {
    path: '/application-import-upload-file',
    name: 'ApplicationImportUploadFile',
    component: ApplicationImportUploadFile,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/component-import-upload-file',
    name: 'ComponentImportUploadFile',
    component: ComponentImportUploadFile,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/flow-import-upload-multifile',
    name: 'FlowImportUploadMultiFile',
    component: FlowImportUploadMultiFile,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/event-import-upload-file',
    name: 'EventImportUploadFile',
    component: EventImportUploadFile,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/capability-import-upload-file',
    name: 'CapabilityImportUploadFile',
    component: CapabilityImportUploadFile,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/application-capability-import-upload-file',
    name: 'ApplicationCapabilityImportUploadFile',
    component: ApplicationCapabilityImportUploadFile,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/reporting/flow-interface',
    name: 'FlowReporting',
    component: FlowReporting,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/reporting/orphan-flow-group',
    name: 'OrphanFlowGroupReporting',
    component: OrphanFlowGroupReporting,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/applications-diagram',
    name: 'ApplicationsDiagram',
    component: ApplicationsDiagram,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/external-system-upload-file',
    name: 'ExternalSystemUploadFile',
    component: ExternalSystemUploadFile,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/full-export',
    name: 'FullExport',
    component: FullExport,
    meta: { authorities: [Authority.WRITE] },
  },
];
