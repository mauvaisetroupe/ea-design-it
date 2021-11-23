import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
import { IDataFlow } from '@/shared/model/data-flow.model';

import { IDataFlowItem, DataFlowItem } from '@/shared/model/data-flow-item.model';
import DataFlowItemService from './data-flow-item.service';

const validations: any = {
  dataFlowItem: {
    resourceName: {},
    description: {
      maxLength: maxLength(1000),
    },
    contractURL: {
      maxLength: maxLength(500),
    },
    documentationURL: {
      maxLength: maxLength(500),
    },
    startDate: {},
    endDate: {},
  },
};

@Component({
  validations,
})
export default class DataFlowItemUpdate extends Vue {
  @Inject('dataFlowItemService') private dataFlowItemService: () => DataFlowItemService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlowItem: IDataFlowItem = new DataFlowItem();

  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;

  public dataFlows: IDataFlow[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFlowItemId) {
        vm.retrieveDataFlowItem(to.params.dataFlowItemId);
      }
      vm.initRelationships();
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
    if (this.dataFlowItem.id) {
      this.dataFlowItemService()
        .update(this.dataFlowItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFlowItem is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
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
      this.dataFlowItemService()
        .create(this.dataFlowItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFlowItem is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
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

  public retrieveDataFlowItem(dataFlowItemId): void {
    this.dataFlowItemService()
      .find(dataFlowItemId)
      .then(res => {
        this.dataFlowItem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.dataFlowService()
      .retrieve()
      .then(res => {
        this.dataFlows = res.data;
      });
  }
}
