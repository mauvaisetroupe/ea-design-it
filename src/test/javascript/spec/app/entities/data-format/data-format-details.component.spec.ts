/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DataFormatDetailComponent from '@/entities/data-format/data-format-details.vue';
import DataFormatClass from '@/entities/data-format/data-format-details.component';
import DataFormatService from '@/entities/data-format/data-format.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DataFormat Management Detail Component', () => {
    let wrapper: Wrapper<DataFormatClass>;
    let comp: DataFormatClass;
    let dataFormatServiceStub: SinonStubbedInstance<DataFormatService>;

    beforeEach(() => {
      dataFormatServiceStub = sinon.createStubInstance<DataFormatService>(DataFormatService);

      wrapper = shallowMount<DataFormatClass>(DataFormatDetailComponent, {
        store,
        localVue,
        router,
        provide: { dataFormatService: () => dataFormatServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDataFormat = { id: 123 };
        dataFormatServiceStub.find.resolves(foundDataFormat);

        // WHEN
        comp.retrieveDataFormat(123);
        await comp.$nextTick();

        // THEN
        expect(comp.dataFormat).toBe(foundDataFormat);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFormat = { id: 123 };
        dataFormatServiceStub.find.resolves(foundDataFormat);

        // WHEN
        comp.beforeRouteEnter({ params: { dataFormatId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.dataFormat).toBe(foundDataFormat);
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
