/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';

import FlowImportService from '@/entities/flow-import/flow-import.service';
import { FlowImport } from '@/shared/model/flow-import.model';
import { ImportStatus } from '@/shared/model/enumerations/import-status.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('FlowImport Service', () => {
    let service: FlowImportService;
    let elemDefault;

    beforeEach(() => {
      service = new FlowImportService();
      elemDefault = new FlowImport(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        ImportStatus.NEW,
        ImportStatus.NEW,
        ImportStatus.NEW,
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a FlowImport', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a FlowImport', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a FlowImport', async () => {
        const returnedFromService = Object.assign(
          {
            idFlowFromExcel: 'BBBBBB',
            flowAlias: 'BBBBBB',
            sourceElement: 'BBBBBB',
            targetElement: 'BBBBBB',
            description: 'BBBBBB',
            integrationPattern: 'BBBBBB',
            frequency: 'BBBBBB',
            format: 'BBBBBB',
            swagger: 'BBBBBB',
            blueprint: 'BBBBBB',
            blueprintStatus: 'BBBBBB',
            flowStatus: 'BBBBBB',
            comment: 'BBBBBB',
            documentName: 'BBBBBB',
            importInterfaceStatus: 'BBBBBB',
            importFunctionalFlowStatus: 'BBBBBB',
            importDataFlowStatus: 'BBBBBB',
            importStatusMessage: 'BBBBBB',
            existingApplicationID: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a FlowImport', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a FlowImport', async () => {
        const patchObject = Object.assign(
          {
            idFlowFromExcel: 'BBBBBB',
            flowAlias: 'BBBBBB',
            targetElement: 'BBBBBB',
            description: 'BBBBBB',
            integrationPattern: 'BBBBBB',
            format: 'BBBBBB',
            swagger: 'BBBBBB',
            blueprintStatus: 'BBBBBB',
            comment: 'BBBBBB',
            importFunctionalFlowStatus: 'BBBBBB',
            existingApplicationID: 'BBBBBB',
          },
          new FlowImport()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a FlowImport', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of FlowImport', async () => {
        const returnedFromService = Object.assign(
          {
            idFlowFromExcel: 'BBBBBB',
            flowAlias: 'BBBBBB',
            sourceElement: 'BBBBBB',
            targetElement: 'BBBBBB',
            description: 'BBBBBB',
            integrationPattern: 'BBBBBB',
            frequency: 'BBBBBB',
            format: 'BBBBBB',
            swagger: 'BBBBBB',
            blueprint: 'BBBBBB',
            blueprintStatus: 'BBBBBB',
            flowStatus: 'BBBBBB',
            comment: 'BBBBBB',
            documentName: 'BBBBBB',
            importInterfaceStatus: 'BBBBBB',
            importFunctionalFlowStatus: 'BBBBBB',
            importDataFlowStatus: 'BBBBBB',
            importStatusMessage: 'BBBBBB',
            existingApplicationID: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of FlowImport', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a FlowImport', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a FlowImport', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
