/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';

import ApplicationImportService from '@/entities/application-import/application-import.service';
import { ApplicationImport } from '@/shared/model/application-import.model';
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
  describe('ApplicationImport Service', () => {
    let service: ApplicationImportService;
    let elemDefault;

    beforeEach(() => {
      service = new ApplicationImportService();
      elemDefault = new ApplicationImport(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
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

      it('should create a ApplicationImport', async () => {
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

      it('should not create a ApplicationImport', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ApplicationImport', async () => {
        const returnedFromService = Object.assign(
          {
            importId: 'BBBBBB',
            excelFileName: 'BBBBBB',
            idFromExcel: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            technology: 'BBBBBB',
            comment: 'BBBBBB',
            importStatus: 'BBBBBB',
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

      it('should not update a ApplicationImport', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ApplicationImport', async () => {
        const patchObject = Object.assign(
          {
            idFromExcel: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            technology: 'BBBBBB',
          },
          new ApplicationImport()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ApplicationImport', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ApplicationImport', async () => {
        const returnedFromService = Object.assign(
          {
            importId: 'BBBBBB',
            excelFileName: 'BBBBBB',
            idFromExcel: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            technology: 'BBBBBB',
            comment: 'BBBBBB',
            importStatus: 'BBBBBB',
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

      it('should not return a list of ApplicationImport', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ApplicationImport', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ApplicationImport', async () => {
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
