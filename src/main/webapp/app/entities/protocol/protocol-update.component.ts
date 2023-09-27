import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IProtocol, Protocol } from '@/shared/model/protocol.model';
import ProtocolService from './protocol.service';
import { ProtocolType } from '@/shared/model/enumerations/protocol-type.model';

const validations: any = {
  protocol: {
    name: {
      required,
    },
    type: {
      required,
    },
    description: {
      maxLength: maxLength(1000),
    },
    scope: {},
  },
};

@Component({
  validations,
})
export default class ProtocolUpdate extends Vue {
  @Inject('protocolService') private protocolService: () => ProtocolService;
  @Inject('alertService') private alertService: () => AlertService;

  public protocol: IProtocol = new Protocol();
  public protocolTypeValues: string[] = Object.keys(ProtocolType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.protocolId) {
        vm.retrieveProtocol(to.params.protocolId);
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
    if (this.protocol.id) {
      this.protocolService()
        .update(this.protocol)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Protocol is updated with identifier ' + param.id;
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
      this.protocolService()
        .create(this.protocol)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Protocol is created with identifier ' + param.id;
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

  public retrieveProtocol(protocolId): void {
    this.protocolService()
      .find(protocolId)
      .then(res => {
        this.protocol = res;
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
