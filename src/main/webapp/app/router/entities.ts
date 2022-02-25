import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const LandscapeView = () => import('@/entities/landscape-view/landscape-view.vue');
const LandscapeViewUpdate = () => import('@/entities/landscape-view/landscape-view-update.vue');
const LandscapeViewDetails = () => import('@/entities/landscape-view/landscape-view-details.vue');

const Owner = () => import('@/entities/owner/owner.vue');
const OwnerUpdate = () => import('@/entities/owner/owner-update.vue');
const OwnerDetails = () => import('@/entities/owner/owner-details.vue');

const FunctionalFlow = () => import('@/entities/functional-flow/functional-flow.vue');
const FunctionalFlowUpdate = () => import('@/entities/functional-flow/functional-flow-update.vue');
const FunctionalFlowDetails = () => import('@/entities/functional-flow/functional-flow-details.vue');

const FlowInterface = () => import('@/entities/flow-interface/flow-interface.vue');
const FlowInterfaceUpdate = () => import('@/entities/flow-interface/flow-interface-update.vue');
const FlowInterfaceDetails = () => import('@/entities/flow-interface/flow-interface-details.vue');

const Application = () => import('@/entities/application/application.vue');
const ApplicationUpdate = () => import('@/entities/application/application-update.vue');
const ApplicationDetails = () => import('@/entities/application/application-details.vue');

const DataFlow = () => import('@/entities/data-flow/data-flow.vue');
const DataFlowUpdate = () => import('@/entities/data-flow/data-flow-update.vue');
const DataFlowDetails = () => import('@/entities/data-flow/data-flow-details.vue');

const ApplicationComponent = () => import('@/entities/application-component/application-component.vue');
const ApplicationComponentUpdate = () => import('@/entities/application-component/application-component-update.vue');
const ApplicationComponentDetails = () => import('@/entities/application-component/application-component-details.vue');

const ApplicationImport = () => import('@/entities/application-import/application-import.vue');
const ApplicationImportUpdate = () => import('@/entities/application-import/application-import-update.vue');
const ApplicationImportDetails = () => import('@/entities/application-import/application-import-details.vue');

const FlowImport = () => import('@/entities/flow-import/flow-import.vue');
const FlowImportUpdate = () => import('@/entities/flow-import/flow-import-update.vue');
const FlowImportDetails = () => import('@/entities/flow-import/flow-import-details.vue');

const Protocol = () => import('@/entities/protocol/protocol.vue');
const ProtocolUpdate = () => import('@/entities/protocol/protocol-update.vue');
const ProtocolDetails = () => import('@/entities/protocol/protocol-details.vue');

const DataFlowItem = () => import('@/entities/data-flow-item/data-flow-item.vue');
const DataFlowItemUpdate = () => import('@/entities/data-flow-item/data-flow-item-update.vue');
const DataFlowItemDetails = () => import('@/entities/data-flow-item/data-flow-item-details.vue');

const DataFormat = () => import('@/entities/data-format/data-format.vue');
const DataFormatUpdate = () => import('@/entities/data-format/data-format-update.vue');
const DataFormatDetails = () => import('@/entities/data-format/data-format-details.vue');

const ApplicationCategory = () => import('@/entities/application-category/application-category.vue');
const ApplicationCategoryUpdate = () => import('@/entities/application-category/application-category-update.vue');
const ApplicationCategoryDetails = () => import('@/entities/application-category/application-category-details.vue');

const DataFlowImport = () => import('@/entities/data-flow-import/data-flow-import.vue');
const DataFlowImportUpdate = () => import('@/entities/data-flow-import/data-flow-import-update.vue');
const DataFlowImportDetails = () => import('@/entities/data-flow-import/data-flow-import-details.vue');

const Technology = () => import('@/entities/technology/technology.vue');
const TechnologyUpdate = () => import('@/entities/technology/technology-update.vue');
const TechnologyDetails = () => import('@/entities/technology/technology-details.vue');

const Capability = () => import('@/entities/capability/capability.vue');
const CapabilityUpdate = () => import('@/entities/capability/capability-update.vue');
const CapabilityDetails = () => import('@/entities/capability/capability-details.vue');

const FunctionalFlowStep = () => import('@/entities/functional-flow-step/functional-flow-step.vue');
const FunctionalFlowStepUpdate = () => import('@/entities/functional-flow-step/functional-flow-step-update.vue');
const FunctionalFlowStepDetails = () => import('@/entities/functional-flow-step/functional-flow-step-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'landscape-view',
      name: 'LandscapeView',
      component: LandscapeView,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape-view/new',
      name: 'LandscapeViewCreate',
      component: LandscapeViewUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape-view/:landscapeViewId/edit',
      name: 'LandscapeViewEdit',
      component: LandscapeViewUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape-view/:landscapeViewId/view',
      name: 'LandscapeViewView',
      component: LandscapeViewDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'owner',
      name: 'Owner',
      component: Owner,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'owner/new',
      name: 'OwnerCreate',
      component: OwnerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'owner/:ownerId/edit',
      name: 'OwnerEdit',
      component: OwnerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'owner/:ownerId/view',
      name: 'OwnerView',
      component: OwnerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow',
      name: 'FunctionalFlow',
      component: FunctionalFlow,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow/new',
      name: 'FunctionalFlowCreate',
      component: FunctionalFlowUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow/:functionalFlowId/edit',
      name: 'FunctionalFlowEdit',
      component: FunctionalFlowUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow/:functionalFlowId/view',
      name: 'FunctionalFlowView',
      component: FunctionalFlowDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-interface',
      name: 'FlowInterface',
      component: FlowInterface,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-interface/new',
      name: 'FlowInterfaceCreate',
      component: FlowInterfaceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-interface/:flowInterfaceId/edit',
      name: 'FlowInterfaceEdit',
      component: FlowInterfaceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-interface/:flowInterfaceId/view',
      name: 'FlowInterfaceView',
      component: FlowInterfaceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application',
      name: 'Application',
      component: Application,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application/new',
      name: 'ApplicationCreate',
      component: ApplicationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application/:applicationId/edit',
      name: 'ApplicationEdit',
      component: ApplicationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application/:applicationId/view',
      name: 'ApplicationView',
      component: ApplicationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow',
      name: 'DataFlow',
      component: DataFlow,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow/new',
      name: 'DataFlowCreate',
      component: DataFlowUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow/:dataFlowId/edit',
      name: 'DataFlowEdit',
      component: DataFlowUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow/:dataFlowId/view',
      name: 'DataFlowView',
      component: DataFlowDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-component',
      name: 'ApplicationComponent',
      component: ApplicationComponent,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-component/new',
      name: 'ApplicationComponentCreate',
      component: ApplicationComponentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-component/:applicationComponentId/edit',
      name: 'ApplicationComponentEdit',
      component: ApplicationComponentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-component/:applicationComponentId/view',
      name: 'ApplicationComponentView',
      component: ApplicationComponentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-import',
      name: 'ApplicationImport',
      component: ApplicationImport,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-import/new',
      name: 'ApplicationImportCreate',
      component: ApplicationImportUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-import/:applicationImportId/edit',
      name: 'ApplicationImportEdit',
      component: ApplicationImportUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-import/:applicationImportId/view',
      name: 'ApplicationImportView',
      component: ApplicationImportDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-import',
      name: 'FlowImport',
      component: FlowImport,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-import/new',
      name: 'FlowImportCreate',
      component: FlowImportUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-import/:flowImportId/edit',
      name: 'FlowImportEdit',
      component: FlowImportUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-import/:flowImportId/view',
      name: 'FlowImportView',
      component: FlowImportDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'protocol',
      name: 'Protocol',
      component: Protocol,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'protocol/new',
      name: 'ProtocolCreate',
      component: ProtocolUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'protocol/:protocolId/edit',
      name: 'ProtocolEdit',
      component: ProtocolUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'protocol/:protocolId/view',
      name: 'ProtocolView',
      component: ProtocolDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-item',
      name: 'DataFlowItem',
      component: DataFlowItem,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-item/new',
      name: 'DataFlowItemCreate',
      component: DataFlowItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-item/:dataFlowItemId/edit',
      name: 'DataFlowItemEdit',
      component: DataFlowItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-item/:dataFlowItemId/view',
      name: 'DataFlowItemView',
      component: DataFlowItemDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-format',
      name: 'DataFormat',
      component: DataFormat,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-format/new',
      name: 'DataFormatCreate',
      component: DataFormatUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-format/:dataFormatId/edit',
      name: 'DataFormatEdit',
      component: DataFormatUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-format/:dataFormatId/view',
      name: 'DataFormatView',
      component: DataFormatDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-category',
      name: 'ApplicationCategory',
      component: ApplicationCategory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-category/new',
      name: 'ApplicationCategoryCreate',
      component: ApplicationCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-category/:applicationCategoryId/edit',
      name: 'ApplicationCategoryEdit',
      component: ApplicationCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-category/:applicationCategoryId/view',
      name: 'ApplicationCategoryView',
      component: ApplicationCategoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-import',
      name: 'DataFlowImport',
      component: DataFlowImport,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-import/new',
      name: 'DataFlowImportCreate',
      component: DataFlowImportUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-import/:dataFlowImportId/edit',
      name: 'DataFlowImportEdit',
      component: DataFlowImportUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-flow-import/:dataFlowImportId/view',
      name: 'DataFlowImportView',
      component: DataFlowImportDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'technology',
      name: 'Technology',
      component: Technology,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'technology/new',
      name: 'TechnologyCreate',
      component: TechnologyUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'technology/:technologyId/edit',
      name: 'TechnologyEdit',
      component: TechnologyUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'technology/:technologyId/view',
      name: 'TechnologyView',
      component: TechnologyDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capability',
      name: 'Capability',
      component: Capability,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capability/new',
      name: 'CapabilityCreate',
      component: CapabilityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capability/:capabilityId/edit',
      name: 'CapabilityEdit',
      component: CapabilityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capability/:capabilityId/view',
      name: 'CapabilityView',
      component: CapabilityDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow-step',
      name: 'FunctionalFlowStep',
      component: FunctionalFlowStep,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow-step/new',
      name: 'FunctionalFlowStepCreate',
      component: FunctionalFlowStepUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow-step/:functionalFlowStepId/edit',
      name: 'FunctionalFlowStepEdit',
      component: FunctionalFlowStepUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'functional-flow-step/:functionalFlowStepId/view',
      name: 'FunctionalFlowStepView',
      component: FunctionalFlowStepDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
