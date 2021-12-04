import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { ILandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from './landscape-view.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class LandscapeViewDetails extends mixins(JhiDataUtils) {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscapeView: ILandscapeView = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeViewId) {
        vm.retrieveLandscapeView(to.params.landscapeViewId);
      }
    });
  }

  public retrieveLandscapeView(landscapeViewId) {
    this.landscapeViewService()
      .find(landscapeViewId)
      .then(res => {
        this.landscapeView = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
