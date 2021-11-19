/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EventDataDetailComponent from '@/entities/event-data/event-data-details.vue';
import EventDataClass from '@/entities/event-data/event-data-details.component';
import EventDataService from '@/entities/event-data/event-data.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('EventData Management Detail Component', () => {
    let wrapper: Wrapper<EventDataClass>;
    let comp: EventDataClass;
    let eventDataServiceStub: SinonStubbedInstance<EventDataService>;

    beforeEach(() => {
      eventDataServiceStub = sinon.createStubInstance<EventDataService>(EventDataService);

      wrapper = shallowMount<EventDataClass>(EventDataDetailComponent, {
        store,
        localVue,
        router,
        provide: { eventDataService: () => eventDataServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEventData = { id: 123 };
        eventDataServiceStub.find.resolves(foundEventData);

        // WHEN
        comp.retrieveEventData(123);
        await comp.$nextTick();

        // THEN
        expect(comp.eventData).toBe(foundEventData);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEventData = { id: 123 };
        eventDataServiceStub.find.resolves(foundEventData);

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
