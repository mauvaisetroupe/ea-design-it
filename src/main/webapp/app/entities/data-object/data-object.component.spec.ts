/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import DataObject from './data-object.vue';
import DataObjectService from './data-object.service';
import AlertService from '@/shared/alert/alert.service';

type DataObjectComponentType = InstanceType<typeof DataObject>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('DataObject Management Component', () => {
    let dataObjectServiceStub: SinonStubbedInstance<DataObjectService>;
    let mountOptions: MountingOptions<DataObjectComponentType>['global'];

    beforeEach(() => {
      dataObjectServiceStub = sinon.createStubInstance<DataObjectService>(DataObjectService);
      dataObjectServiceStub.retrieve.resolves({ headers: {} });

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
          dataObjectService: () => dataObjectServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataObjectServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(DataObject, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(dataObjectServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.dataObjects[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: DataObjectComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(DataObject, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        dataObjectServiceStub.retrieve.reset();
        dataObjectServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        dataObjectServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeDataObject();
        await comp.$nextTick(); // clear components

        // THEN
        expect(dataObjectServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(dataObjectServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
