import { defineComponent, provide } from 'vue';

import LandscapeViewService from './landscape-view/landscape-view.service';
import OwnerService from './owner/owner.service';
import FunctionalFlowService from './functional-flow/functional-flow.service';
import FlowInterfaceService from './flow-interface/flow-interface.service';
import ApplicationService from './application/application.service';
import DataFlowService from './data-flow/data-flow.service';
import ApplicationComponentService from './application-component/application-component.service';
import ProtocolService from './protocol/protocol.service';
import DataFlowItemService from './data-flow-item/data-flow-item.service';
import DataFormatService from './data-format/data-format.service';
import ApplicationCategoryService from './application-category/application-category.service';
import TechnologyService from './technology/technology.service';
import CapabilityService from './capability/capability.service';
import FunctionalFlowStepService from './functional-flow-step/functional-flow-step.service';
import FlowGroupService from './flow-group/flow-group.service';
import ExternalReferenceService from './external-reference/external-reference.service';
import ExternalSystemService from './external-system/external-system.service';
import CapabilityApplicationMappingService from './capability-application-mapping/capability-application-mapping.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('landscapeViewService', () => new LandscapeViewService());
    provide('ownerService', () => new OwnerService());
    provide('functionalFlowService', () => new FunctionalFlowService());
    provide('flowInterfaceService', () => new FlowInterfaceService());
    provide('applicationService', () => new ApplicationService());
    provide('dataFlowService', () => new DataFlowService());
    provide('applicationComponentService', () => new ApplicationComponentService());
    provide('protocolService', () => new ProtocolService());
    provide('dataFlowItemService', () => new DataFlowItemService());
    provide('dataFormatService', () => new DataFormatService());
    provide('applicationCategoryService', () => new ApplicationCategoryService());
    provide('technologyService', () => new TechnologyService());
    provide('capabilityService', () => new CapabilityService());
    provide('functionalFlowStepService', () => new FunctionalFlowStepService());
    provide('flowGroupService', () => new FlowGroupService());
    provide('externalReferenceService', () => new ExternalReferenceService());
    provide('externalSystemService', () => new ExternalSystemService());
    provide('capabilityApplicationMappingService', () => new CapabilityApplicationMappingService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
