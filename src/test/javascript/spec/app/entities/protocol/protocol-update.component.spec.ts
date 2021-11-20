/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ProtocolUpdateComponent from '@/entities/protocol/protocol-update.vue';
import ProtocolClass from '@/entities/protocol/protocol-update.component';
import ProtocolService from '@/entities/protocol/protocol.service';

import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Protocol Management Update Component', () => {
    let wrapper: Wrapper<ProtocolClass>;
    let comp: ProtocolClass;
    let protocolServiceStub: SinonStubbedInstance<ProtocolService>;

    beforeEach(() => {
      protocolServiceStub = sinon.createStubInstance<ProtocolService>(ProtocolService);

      wrapper = shallowMount<ProtocolClass>(ProtocolUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          protocolService: () => protocolServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.protocol = entity;
        protocolServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(protocolServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.protocol = entity;
        protocolServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(protocolServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProtocol = { id: 123 };
        protocolServiceStub.find.resolves(foundProtocol);
        protocolServiceStub.retrieve.resolves([foundProtocol]);

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
