/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';

import LandscapeViewService from './landscape-view.service';
import { LandscapeView } from '@/shared/model/landscape-view.model';

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
  describe('LandscapeView Service', () => {
    let service: LandscapeViewService;
    let elemDefault;

    beforeEach(() => {
      service = new LandscapeViewService();
      elemDefault = new LandscapeView(123, 'APPLICATION_LANDSCAPE', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
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

      it('should create a LandscapeView', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
          },
          elemDefault,
        );
        const expected = Object.assign({}, returnedFromService);

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a LandscapeView', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a LandscapeView', async () => {
        const returnedFromService = Object.assign(
          {
            viewpoint: 'BBBBBB',
            diagramName: 'BBBBBB',
            compressedDrawXML: 'BBBBBB',
            compressedDrawSVG: 'BBBBBB',
          },
          elemDefault,
        );

        const expected = Object.assign({}, returnedFromService);
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a LandscapeView', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a LandscapeView', async () => {
        const patchObject = Object.assign(
          {
            viewpoint: 'BBBBBB',
            compressedDrawXML: 'BBBBBB',
          },
          new LandscapeView(),
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a LandscapeView', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of LandscapeView', async () => {
        const returnedFromService = Object.assign(
          {
            viewpoint: 'BBBBBB',
            diagramName: 'BBBBBB',
            compressedDrawXML: 'BBBBBB',
            compressedDrawSVG: 'BBBBBB',
          },
          elemDefault,
        );
        const expected = Object.assign({}, returnedFromService);
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of LandscapeView', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a LandscapeView', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123, false, false, false).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a LandscapeView', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123, false, false, false)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
