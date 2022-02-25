/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProtocolDetailComponent from '@/entities/protocol/protocol-details.vue';
import ProtocolClass from '@/entities/protocol/protocol-details.component';
import ProtocolService from '@/entities/protocol/protocol.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Protocol Management Detail Component', () => {
    let wrapper: Wrapper<ProtocolClass>;
    let comp: ProtocolClass;
    let protocolServiceStub: SinonStubbedInstance<ProtocolService>;

    beforeEach(() => {
      protocolServiceStub = sinon.createStubInstance<ProtocolService>(ProtocolService);

      wrapper = shallowMount<ProtocolClass>(ProtocolDetailComponent, {
        store,
        localVue,
        router,
        provide: { protocolService: () => protocolServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProtocol = { id: 123 };
        protocolServiceStub.find.resolves(foundProtocol);

        // WHEN
        comp.retrieveProtocol(123);
        await comp.$nextTick();

        // THEN
        expect(comp.protocol).toBe(foundProtocol);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProtocol = { id: 123 };
        protocolServiceStub.find.resolves(foundProtocol);

        // WHEN
        comp.beforeRouteEnter({ params: { protocolId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.protocol).toBe(foundProtocol);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
