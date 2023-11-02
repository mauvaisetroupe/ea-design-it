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

const Technology = () => import('@/entities/technology/technology.vue');
const TechnologyUpdate = () => import('@/entities/technology/technology-update.vue');
const TechnologyDetails = () => import('@/entities/technology/technology-details.vue');

const Capability = () => import('@/entities/capability/capability.vue');
const CapabilityUpdate = () => import('@/entities/capability/capability-update.vue');
const CapabilityDetails = () => import('@/entities/capability/capability-details.vue');
const CapabilityNavigate = () => import('@/entities/capability/capability-navigate.vue');
const CapabilityRootNavigate = () => import('@/entities/capability/capability-navigate.vue');

const FunctionalFlowStep = () => import('@/entities/functional-flow-step/functional-flow-step.vue');
const FunctionalFlowStepUpdate = () => import('@/entities/functional-flow-step/functional-flow-step-update.vue');
const FunctionalFlowStepDetails = () => import('@/entities/functional-flow-step/functional-flow-step-details.vue');

// prettier-ignore
const FlowGroup = () => import('@/entities/flow-group/flow-group.vue');
const FlowGroupUpdate = () => import('@/entities/flow-group/flow-group-update.vue');
const FlowGroupDetails = () => import('@/entities/flow-group/flow-group-details.vue');

const ExternalReference = () => import('@/entities/external-reference/external-reference.vue');
const ExternalReferenceUpdate = () => import('@/entities/external-reference/external-reference-update.vue');
const ExternalReferenceDetails = () => import('@/entities/external-reference/external-reference-details.vue');

const ExternalSystem = () => import('@/entities/external-system/external-system.vue');
const ExternalSystemUpdate = () => import('@/entities/external-system/external-system-update.vue');
const ExternalSystemDetails = () => import('@/entities/external-system/external-system-details.vue');

const CapabilityApplicationMapping = () => import('@/entities/capability-application-mapping/capability-application-mapping.vue');
const CapabilityApplicationMappingUpdate = () =>
  import('@/entities/capability-application-mapping/capability-application-mapping-update.vue');
const CapabilityApplicationMappingDetails = () =>
  import('@/entities/capability-application-mapping/capability-application-mapping-details.vue');

const BusinessObject = () => import('@/entities/business-object/business-object.vue');
const BusinessObjectUpdate = () => import('@/entities/business-object/business-object-update.vue');
const BusinessObjectDetails = () => import('@/entities/business-object/business-object-details.vue');

const DataObject = () => import('@/entities/data-object/data-object.vue');
const DataObjectUpdate = () => import('@/entities/data-object/data-object-update.vue');
const DataObjectDetails = () => import('@/entities/data-object/data-object-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'landscape-view',
      name: 'LandscapeView',
      component: LandscapeView,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'landscape-view/new',
      name: 'LandscapeViewCreate',
      component: LandscapeViewUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'landscape-view/:landscapeViewId/edit',
      name: 'LandscapeViewEdit',
      component: LandscapeViewUpdate,
      meta: { authorities: [Authority.WRITE, Authority.CONTRIBUTOR] },
    },
    {
      path: 'landscape-view/:landscapeViewId/view',
      name: 'LandscapeViewView',
      component: LandscapeViewDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'owner',
      name: 'Owner',
      component: Owner,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'owner/new',
      name: 'OwnerCreate',
      component: OwnerUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'owner/:ownerId/edit',
      name: 'OwnerEdit',
      component: OwnerUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'owner/:ownerId/view',
      name: 'OwnerView',
      component: OwnerDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'functional-flow',
      name: 'FunctionalFlow',
      component: FunctionalFlow,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'functional-flow/new',
      name: 'FunctionalFlowCreate',
      component: FunctionalFlowUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'functional-flow/:functionalFlowId/edit',
      name: 'FunctionalFlowEdit',
      component: FunctionalFlowUpdate,
      meta: { authorities: [Authority.WRITE, Authority.CONTRIBUTOR] },
    },
    {
      path: 'functional-flow/:functionalFlowId/view',
      name: 'FunctionalFlowView',
      component: FunctionalFlowDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'flow-interface',
      name: 'FlowInterface',
      component: FlowInterface,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'flow-interface/new',
      name: 'FlowInterfaceCreate',
      component: FlowInterfaceUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'flow-interface/:flowInterfaceId/edit',
      name: 'FlowInterfaceEdit',
      component: FlowInterfaceUpdate,
      meta: { authorities: [Authority.WRITE, Authority.CONTRIBUTOR] },
    },
    {
      path: 'flow-interface/:flowInterfaceId/view',
      name: 'FlowInterfaceView',
      component: FlowInterfaceDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'application',
      name: 'Application',
      component: Application,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'application/new',
      name: 'ApplicationCreate',
      component: ApplicationUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'application/:applicationId/edit',
      name: 'ApplicationEdit',
      component: ApplicationUpdate,
      meta: { authorities: [Authority.WRITE, Authority.CONTRIBUTOR] },
    },
    {
      path: 'application/:applicationId/view',
      name: 'ApplicationView',
      component: ApplicationDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-flow',
      name: 'DataFlow',
      component: DataFlow,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-flow/new',
      name: 'DataFlowCreate',
      component: DataFlowUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'data-flow/:dataFlowId/edit',
      name: 'DataFlowEdit',
      component: DataFlowUpdate,
      meta: { authorities: [Authority.WRITE, Authority.CONTRIBUTOR] },
    },
    {
      path: 'data-flow/:dataFlowId/view',
      name: 'DataFlowView',
      component: DataFlowDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'application-component',
      name: 'ApplicationComponent',
      component: ApplicationComponent,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'application-component/new',
      name: 'ApplicationComponentCreate',
      component: ApplicationComponentUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'application-component/:applicationComponentId/edit',
      name: 'ApplicationComponentEdit',
      component: ApplicationComponentUpdate,
      meta: { authorities: [Authority.WRITE, Authority.CONTRIBUTOR] },
    },
    {
      path: 'application-component/:applicationComponentId/view',
      name: 'ApplicationComponentView',
      component: ApplicationComponentDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'protocol',
      name: 'Protocol',
      component: Protocol,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'protocol/new',
      name: 'ProtocolCreate',
      component: ProtocolUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'protocol/:protocolId/edit',
      name: 'ProtocolEdit',
      component: ProtocolUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'protocol/:protocolId/view',
      name: 'ProtocolView',
      component: ProtocolDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-flow-item',
      name: 'DataFlowItem',
      component: DataFlowItem,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-flow-item/new',
      name: 'DataFlowItemCreate',
      component: DataFlowItemUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'data-flow-item/:dataFlowItemId/edit',
      name: 'DataFlowItemEdit',
      component: DataFlowItemUpdate,
      meta: { authorities: [Authority.WRITE, Authority.CONTRIBUTOR] },
    },
    {
      path: 'data-flow-item/:dataFlowItemId/view',
      name: 'DataFlowItemView',
      component: DataFlowItemDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-format',
      name: 'DataFormat',
      component: DataFormat,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-format/new',
      name: 'DataFormatCreate',
      component: DataFormatUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'data-format/:dataFormatId/edit',
      name: 'DataFormatEdit',
      component: DataFormatUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'data-format/:dataFormatId/view',
      name: 'DataFormatView',
      component: DataFormatDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'application-category',
      name: 'ApplicationCategory',
      component: ApplicationCategory,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'application-category/new',
      name: 'ApplicationCategoryCreate',
      component: ApplicationCategoryUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'application-category/:applicationCategoryId/edit',
      name: 'ApplicationCategoryEdit',
      component: ApplicationCategoryUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'application-category/:applicationCategoryId/view',
      name: 'ApplicationCategoryView',
      component: ApplicationCategoryDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'technology',
      name: 'Technology',
      component: Technology,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'technology/new',
      name: 'TechnologyCreate',
      component: TechnologyUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'technology/:technologyId/edit',
      name: 'TechnologyEdit',
      component: TechnologyUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'technology/:technologyId/view',
      name: 'TechnologyView',
      component: TechnologyDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'capability',
      name: 'Capability',
      component: Capability,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'capability/new',
      name: 'CapabilityCreate',
      component: CapabilityUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'capability/:capabilityId/edit',
      name: 'CapabilityEdit',
      component: CapabilityUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'capability/:capabilityId/view',
      name: 'CapabilityView',
      component: CapabilityDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'capability/:capabilityId/navigate',
      name: 'CapabilityNavigate',
      component: CapabilityNavigate,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'capability/navigate',
      name: 'CapabilityRootNavigate',
      component: CapabilityRootNavigate,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'functional-flow-step',
      name: 'FunctionalFlowStep',
      component: FunctionalFlowStep,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'functional-flow-step/new',
      name: 'FunctionalFlowStepCreate',
      component: FunctionalFlowStepUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'functional-flow-step/:functionalFlowStepId/edit',
      name: 'FunctionalFlowStepEdit',
      component: FunctionalFlowStepUpdate,
      meta: { authorities: [Authority.WRITE] },
    },
    {
      path: 'functional-flow-step/:functionalFlowStepId/view',
      name: 'FunctionalFlowStepView',
      component: FunctionalFlowStepDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'flow-group',
      name: 'FlowGroup',
      component: FlowGroup,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'flow-group/new',
      name: 'FlowGroupCreate',
      component: FlowGroupUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-group/:flowGroupId/edit',
      name: 'FlowGroupEdit',
      component: FlowGroupUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flow-group/:flowGroupId/view',
      name: 'FlowGroupView',
      component: FlowGroupDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'external-reference',
      name: 'ExternalReference',
      component: ExternalReference,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'external-reference/new',
      name: 'ExternalReferenceCreate',
      component: ExternalReferenceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'external-reference/:externalReferenceId/edit',
      name: 'ExternalReferenceEdit',
      component: ExternalReferenceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'external-reference/:externalReferenceId/view',
      name: 'ExternalReferenceView',
      component: ExternalReferenceDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'external-system',
      name: 'ExternalSystem',
      component: ExternalSystem,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'external-system/new',
      name: 'ExternalSystemCreate',
      component: ExternalSystemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'external-system/:externalSystemId/edit',
      name: 'ExternalSystemEdit',
      component: ExternalSystemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'external-system/:externalSystemId/view',
      name: 'ExternalSystemView',
      component: ExternalSystemDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'capability-application-mapping',
      name: 'CapabilityApplicationMapping',
      component: CapabilityApplicationMapping,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'capability-application-mapping/new',
      name: 'CapabilityApplicationMappingCreate',
      component: CapabilityApplicationMappingUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capability-application-mapping/:capabilityApplicationMappingId/edit',
      name: 'CapabilityApplicationMappingEdit',
      component: CapabilityApplicationMappingUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'capability-application-mapping/:capabilityApplicationMappingId/view',
      name: 'CapabilityApplicationMappingView',
      component: CapabilityApplicationMappingDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'business-object',
      name: 'BusinessObject',
      component: BusinessObject,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'business-object/new',
      name: 'BusinessObjectCreate',
      component: BusinessObjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'business-object/:businessObjectId/edit',
      name: 'BusinessObjectEdit',
      component: BusinessObjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'business-object/:businessObjectId/view',
      name: 'BusinessObjectView',
      component: BusinessObjectDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-object',
      name: 'DataObject',
      component: DataObject,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    {
      path: 'data-object/new',
      name: 'DataObjectCreate',
      component: DataObjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-object/:dataObjectId/edit',
      name: 'DataObjectEdit',
      component: DataObjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'data-object/:dataObjectId/view',
      name: 'DataObjectView',
      component: DataObjectDetails,
      meta: { authorities: [Authority.USER, Authority.ANONYMOUS_ALLOWED] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
