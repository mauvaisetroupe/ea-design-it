/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import EventDataUpdateComponent from '@/entities/event-data/event-data-update.vue';
import EventDataClass from '@/entities/event-data/event-data-update.component';
import EventDataService from '@/entities/event-data/event-data.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
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
  describe('EventData Management Update Component', () => {
    let wrapper: Wrapper<EventDataClass>;
    let comp: EventDataClass;
    let eventDataServiceStub: SinonStubbedInstance<EventDataService>;

    beforeEach(() => {
      eventDataServiceStub = sinon.createStubInstance<EventDataService>(EventDataService);

      wrapper = shallowMount<EventDataClass>(EventDataUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          eventDataService: () => eventDataServiceStub,
          alertService: () => new AlertService(),

          dataFlowService: () => new DataFlowService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.eventData = entity;
        eventDataServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(eventDataServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.eventData = entity;
        eventDataServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(eventDataServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEventData = { id: 123 };
        eventDataServiceStub.find.resolves(foundEventData);
        eventDataServiceStub.retrieve.resolves([foundEventData]);

        // WHEN
        comp.beforeRouteEnter({ params: { eventDataId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.eventData).toBe(foundEventData);
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
