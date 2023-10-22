/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import DataFormat from './data-format.vue';
import DataFormatService from './data-format.service';
import AlertService from '@/shared/alert/alert.service';

type DataFormatComponentType = InstanceType<typeof DataFormat>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('DataFormat Management Component', () => {
    let dataFormatServiceStub: SinonStubbedInstance<DataFormatService>;
    let mountOptions: MountingOptions<DataFormatComponentType>['global'];

    beforeEach(() => {
      dataFormatServiceStub = sinon.createStubInstance<DataFormatService>(DataFormatService);
      dataFormatServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          dataFormatService: () => dataFormatServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataFormatServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(DataFormat, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(dataFormatServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.dataFormats[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: DataFormatComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(DataFormat, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        dataFormatServiceStub.retrieve.reset();
        dataFormatServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        dataFormatServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeDataFormat();
        await comp.$nextTick(); // clear components

        // THEN
        expect(dataFormatServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(dataFormatServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
