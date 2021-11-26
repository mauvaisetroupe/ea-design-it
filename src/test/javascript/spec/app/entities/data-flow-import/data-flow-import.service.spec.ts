/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';

import DataFlowImportService from '@/entities/data-flow-import/data-flow-import.service';
import { DataFlowImport } from '@/shared/model/data-flow-import.model';
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
  describe('DataFlowImport Service', () => {
    let service: DataFlowImportService;
    let elemDefault;

    beforeEach(() => {
      service = new DataFlowImportService();
      elemDefault = new DataFlowImport(
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
        'AAAAAAA',
        ImportStatus.NEW,
        ImportStatus.NEW,
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

      it('should create a DataFlowImport', async () => {
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

      it('should not create a DataFlowImport', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a DataFlowImport', async () => {
        const returnedFromService = Object.assign(
          {
            dataId: 'BBBBBB',
            dataParentId: 'BBBBBB',
            dataParentName: 'BBBBBB',
            functionalFlowId: 'BBBBBB',
            flowInterfaceId: 'BBBBBB',
            dataType: 'BBBBBB',
            dataResourceName: 'BBBBBB',
            dataResourceType: 'BBBBBB',
            dataDescription: 'BBBBBB',
            dataFrequency: 'BBBBBB',
            dataFormat: 'BBBBBB',
            dataContractURL: 'BBBBBB',
            dataDocumentationURL: 'BBBBBB',
            source: 'BBBBBB',
            target: 'BBBBBB',
            importDataStatus: 'BBBBBB',
            importDataItemStatus: 'BBBBBB',
            importStatusMessage: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a DataFlowImport', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a DataFlowImport', async () => {
        const patchObject = Object.assign(
          {
            flowInterfaceId: 'BBBBBB',
            dataType: 'BBBBBB',
            dataResourceName: 'BBBBBB',
            dataDescription: 'BBBBBB',
            dataFrequency: 'BBBBBB',
            dataFormat: 'BBBBBB',
            dataDocumentationURL: 'BBBBBB',
            source: 'BBBBBB',
            target: 'BBBBBB',
            importDataStatus: 'BBBBBB',
            importStatusMessage: 'BBBBBB',
          },
          new DataFlowImport()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a DataFlowImport', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of DataFlowImport', async () => {
        const returnedFromService = Object.assign(
          {
            dataId: 'BBBBBB',
            dataParentId: 'BBBBBB',
            dataParentName: 'BBBBBB',
            functionalFlowId: 'BBBBBB',
            flowInterfaceId: 'BBBBBB',
            dataType: 'BBBBBB',
            dataResourceName: 'BBBBBB',
            dataResourceType: 'BBBBBB',
            dataDescription: 'BBBBBB',
            dataFrequency: 'BBBBBB',
            dataFormat: 'BBBBBB',
            dataContractURL: 'BBBBBB',
            dataDocumentationURL: 'BBBBBB',
            source: 'BBBBBB',
            target: 'BBBBBB',
            importDataStatus: 'BBBBBB',
            importDataItemStatus: 'BBBBBB',
            importStatusMessage: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of DataFlowImport', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a DataFlowImport', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a DataFlowImport', async () => {
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
