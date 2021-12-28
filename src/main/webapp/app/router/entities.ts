import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const LandscapeView = () => import('@/entities/landscape-view/landscape-view.vue');
// prettier-ignore
const LandscapeViewUpdate = () => import('@/entities/landscape-view/landscape-view-update.vue');
// prettier-ignore
const LandscapeViewDetails = () => import('@/entities/landscape-view/landscape-view-details.vue');
// prettier-ignore
const Owner = () => import('@/entities/owner/owner.vue');
// prettier-ignore
const OwnerUpdate = () => import('@/entities/owner/owner-update.vue');
// prettier-ignore
const OwnerDetails = () => import('@/entities/owner/owner-details.vue');
// prettier-ignore
const FunctionalFlow = () => import('@/entities/functional-flow/functional-flow.vue');
// prettier-ignore
const FunctionalFlowUpdate = () => import('@/entities/functional-flow/functional-flow-update.vue');
// prettier-ignore
const FunctionalFlowDetails = () => import('@/entities/functional-flow/functional-flow-details.vue');
// prettier-ignore
const FlowInterface = () => import('@/entities/flow-interface/flow-interface.vue');
// prettier-ignore
const FlowInterfaceUpdate = () => import('@/entities/flow-interface/flow-interface-update.vue');
// prettier-ignore
const FlowInterfaceDetails = () => import('@/entities/flow-interface/flow-interface-details.vue');
// prettier-ignore
const Application = () => import('@/entities/application/application.vue');
// prettier-ignore
const ApplicationUpdate = () => import('@/entities/application/application-update.vue');
// prettier-ignore
const ApplicationDetails = () => import('@/entities/application/application-details.vue');
// prettier-ignore
const DataFlow = () => import('@/entities/data-flow/data-flow.vue');
// prettier-ignore
const DataFlowUpdate = () => import('@/entities/data-flow/data-flow-update.vue');
// prettier-ignore
const DataFlowDetails = () => import('@/entities/data-flow/data-flow-details.vue');
// prettier-ignore
const ApplicationComponent = () => import('@/entities/application-component/application-component.vue');
// prettier-ignore
const ApplicationComponentUpdate = () => import('@/entities/application-component/application-component-update.vue');
// prettier-ignore
const ApplicationComponentDetails = () => import('@/entities/application-component/application-component-details.vue');
// prettier-ignore
const Protocol = () => import('@/entities/protocol/protocol.vue');
// prettier-ignore
const ProtocolUpdate = () => import('@/entities/protocol/protocol-update.vue');
// prettier-ignore
const ProtocolDetails = () => import('@/entities/protocol/protocol-details.vue');
// prettier-ignore
const DataFlowItem = () => import('@/entities/data-flow-item/data-flow-item.vue');
// prettier-ignore
const DataFlowItemUpdate = () => import('@/entities/data-flow-item/data-flow-item-update.vue');
// prettier-ignore
const DataFlowItemDetails = () => import('@/entities/data-flow-item/data-flow-item-details.vue');
// prettier-ignore
const DataFormat = () => import('@/entities/data-format/data-format.vue');
// prettier-ignore
const DataFormatUpdate = () => import('@/entities/data-format/data-format-update.vue');
// prettier-ignore
const DataFormatDetails = () => import('@/entities/data-format/data-format-details.vue');
// prettier-ignore
const ApplicationCategory = () => import('@/entities/application-category/application-category.vue');
// prettier-ignore
const ApplicationCategoryUpdate = () => import('@/entities/application-category/application-category-update.vue');
// prettier-ignore
const ApplicationCategoryDetails = () => import('@/entities/application-category/application-category-details.vue');
// prettier-ignore
const Technology = () => import('@/entities/technology/technology.vue');
// prettier-ignore
const TechnologyUpdate = () => import('@/entities/technology/technology-update.vue');
// prettier-ignore
const TechnologyDetails = () => import('@/entities/technology/technology-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/landscape-view',
    name: 'LandscapeView',
    component: LandscapeView,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/landscape-view/new',
    name: 'LandscapeViewCreate',
    component: LandscapeViewUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/landscape-view/:landscapeViewId/edit',
    name: 'LandscapeViewEdit',
    component: LandscapeViewUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/landscape-view/:landscapeViewId/view',
    name: 'LandscapeViewView',
    component: LandscapeViewDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/owner',
    name: 'Owner',
    component: Owner,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/owner/new',
    name: 'OwnerCreate',
    component: OwnerUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/owner/:ownerId/edit',
    name: 'OwnerEdit',
    component: OwnerUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/owner/:ownerId/view',
    name: 'OwnerView',
    component: OwnerDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/functional-flow',
    name: 'FunctionalFlow',
    component: FunctionalFlow,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/functional-flow/new',
    name: 'FunctionalFlowCreate',
    component: FunctionalFlowUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/functional-flow/:functionalFlowId/edit',
    name: 'FunctionalFlowEdit',
    component: FunctionalFlowUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/functional-flow/:functionalFlowId/view',
    name: 'FunctionalFlowView',
    component: FunctionalFlowDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/flow-interface',
    name: 'FlowInterface',
    component: FlowInterface,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/flow-interface/new',
    name: 'FlowInterfaceCreate',
    component: FlowInterfaceUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/flow-interface/:flowInterfaceId/edit',
    name: 'FlowInterfaceEdit',
    component: FlowInterfaceUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/flow-interface/:flowInterfaceId/view',
    name: 'FlowInterfaceView',
    component: FlowInterfaceDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/application',
    name: 'Application',
    component: Application,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/application/new',
    name: 'ApplicationCreate',
    component: ApplicationUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/application/:applicationId/edit',
    name: 'ApplicationEdit',
    component: ApplicationUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/application/:applicationId/view',
    name: 'ApplicationView',
    component: ApplicationDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/data-flow',
    name: 'DataFlow',
    component: DataFlow,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/data-flow/new',
    name: 'DataFlowCreate',
    component: DataFlowUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/data-flow/:dataFlowId/edit',
    name: 'DataFlowEdit',
    component: DataFlowUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/data-flow/:dataFlowId/view',
    name: 'DataFlowView',
    component: DataFlowDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/application-component',
    name: 'ApplicationComponent',
    component: ApplicationComponent,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/application-component/new',
    name: 'ApplicationComponentCreate',
    component: ApplicationComponentUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/application-component/:applicationComponentId/edit',
    name: 'ApplicationComponentEdit',
    component: ApplicationComponentUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/application-component/:applicationComponentId/view',
    name: 'ApplicationComponentView',
    component: ApplicationComponentDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/protocol',
    name: 'Protocol',
    component: Protocol,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/protocol/new',
    name: 'ProtocolCreate',
    component: ProtocolUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/protocol/:protocolId/edit',
    name: 'ProtocolEdit',
    component: ProtocolUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/protocol/:protocolId/view',
    name: 'ProtocolView',
    component: ProtocolDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/data-flow-item',
    name: 'DataFlowItem',
    component: DataFlowItem,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/data-flow-item/new',
    name: 'DataFlowItemCreate',
    component: DataFlowItemUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/data-flow-item/:dataFlowItemId/edit',
    name: 'DataFlowItemEdit',
    component: DataFlowItemUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/data-flow-item/:dataFlowItemId/view',
    name: 'DataFlowItemView',
    component: DataFlowItemDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/data-format',
    name: 'DataFormat',
    component: DataFormat,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/data-format/new',
    name: 'DataFormatCreate',
    component: DataFormatUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/data-format/:dataFormatId/edit',
    name: 'DataFormatEdit',
    component: DataFormatUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/data-format/:dataFormatId/view',
    name: 'DataFormatView',
    component: DataFormatDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/application-category',
    name: 'ApplicationCategory',
    component: ApplicationCategory,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/application-category/new',
    name: 'ApplicationCategoryCreate',
    component: ApplicationCategoryUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/application-category/:applicationCategoryId/edit',
    name: 'ApplicationCategoryEdit',
    component: ApplicationCategoryUpdate,
    meta: { authorities: [Authority.WRITE] },
  },
  {
    path: '/application-category/:applicationCategoryId/view',
    name: 'ApplicationCategoryView',
    component: ApplicationCategoryDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/technology',
    name: 'Technology',
    component: Technology,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  {
    path: '/technology/new',
    name: 'TechnologyCreate',
    component: TechnologyUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/technology/:technologyId/edit',
    name: 'TechnologyEdit',
    component: TechnologyUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/technology/:technologyId/view',
    name: 'TechnologyView',
    component: TechnologyDetails,
    meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
