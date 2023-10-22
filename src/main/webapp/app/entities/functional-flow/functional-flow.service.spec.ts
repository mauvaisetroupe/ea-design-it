/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import FunctionalFlowService from './functional-flow.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { FunctionalFlow } from '@/shared/model/functional-flow.model';

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
  describe('FunctionalFlow Service', () => {
    let service: FunctionalFlowService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new FunctionalFlowService();
      currentDate = new Date();
      elemDefault = new FunctionalFlow(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            startDate: dayjs(currentDate).format(DATE_FORMAT),
            endDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault,
        );
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

      it('should create a FunctionalFlow', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            startDate: dayjs(currentDate).format(DATE_FORMAT),
            endDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService,
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a FunctionalFlow', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a FunctionalFlow', async () => {
        const returnedFromService = Object.assign(
          {
            alias: 'BBBBBB',
            description: 'BBBBBB',
            comment: 'BBBBBB',
            status: 'BBBBBB',
            documentationURL: 'BBBBBB',
            documentationURL2: 'BBBBBB',
            startDate: dayjs(currentDate).format(DATE_FORMAT),
            endDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault,
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService,
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a FunctionalFlow', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a FunctionalFlow', async () => {
        const patchObject = Object.assign(
          {
            comment: 'BBBBBB',
            documentationURL: 'BBBBBB',
            documentationURL2: 'BBBBBB',
          },
          new FunctionalFlow(),
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService,
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a FunctionalFlow', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of FunctionalFlow', async () => {
        const returnedFromService = Object.assign(
          {
            alias: 'BBBBBB',
            description: 'BBBBBB',
            comment: 'BBBBBB',
            status: 'BBBBBB',
            documentationURL: 'BBBBBB',
            documentationURL2: 'BBBBBB',
            startDate: dayjs(currentDate).format(DATE_FORMAT),
            endDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService,
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of FunctionalFlow', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a FunctionalFlow', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123, false, false).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a FunctionalFlow', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123, false, false)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
