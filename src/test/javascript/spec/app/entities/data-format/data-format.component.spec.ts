/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import DataFormatComponent from '@/entities/data-format/data-format.vue';
import DataFormatClass from '@/entities/data-format/data-format.component';
import DataFormatService from '@/entities/data-format/data-format.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('DataFormat Management Component', () => {
    let wrapper: Wrapper<DataFormatClass>;
    let comp: DataFormatClass;
    let dataFormatServiceStub: SinonStubbedInstance<DataFormatService>;

    beforeEach(() => {
      dataFormatServiceStub = sinon.createStubInstance<DataFormatService>(DataFormatService);
      dataFormatServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DataFormatClass>(DataFormatComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          dataFormatService: () => dataFormatServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dataFormatServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDataFormats();
      await comp.$nextTick();

      // THEN
      expect(dataFormatServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dataFormats[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dataFormatServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeDataFormat();
      await comp.$nextTick();

      // THEN
      expect(dataFormatServiceStub.delete.called).toBeTruthy();
      expect(dataFormatServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
