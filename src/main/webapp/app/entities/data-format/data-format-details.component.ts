import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDataFormat } from '@/shared/model/data-format.model';
import DataFormatService from './data-format.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component
export default class DataFormatDetails extends Vue {
  @Inject('dataFormatService') private dataFormatService: () => DataFormatService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  public dataFormat: IDataFormat = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFormatId) {
        vm.retrieveDataFormat(to.params.dataFormatId);
      }
    });
  }

  public retrieveDataFormat(dataFormatId) {
    this.dataFormatService()
      .find(dataFormatId)
      .then(res => {
        this.dataFormat = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
