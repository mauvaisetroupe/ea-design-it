import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IDataFormat, DataFormat } from '@/shared/model/data-format.model';
import DataFormatService from './data-format.service';

const validations: any = {
  dataFormat: {
    name: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class DataFormatUpdate extends Vue {
  @Inject('dataFormatService') private dataFormatService: () => DataFormatService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFormat: IDataFormat = new DataFormat();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFormatId) {
        vm.retrieveDataFormat(to.params.dataFormatId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.dataFormat.id) {
      this.dataFormatService()
        .update(this.dataFormat)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFormat is updated with identifier ' + param.id;
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.dataFormatService()
        .create(this.dataFormat)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFormat is created with identifier ' + param.id;
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveDataFormat(dataFormatId): void {
    this.dataFormatService()
      .find(dataFormatId)
      .then(res => {
        this.dataFormat = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
