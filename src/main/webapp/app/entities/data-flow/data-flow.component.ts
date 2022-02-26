import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDataFlow } from '@/shared/model/data-flow.model';

import DataFlowService from './data-flow.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
@Component({
  computed: {
    filteredRows() {
      return this.dataFlows.filter(row => {
        const data_id = row.id.toString().toLowerCase();
        const name = row.resourceName ? row.resourceName.toString().toLowerCase() : '';
        const description = row.description ? row.description.toString().toLowerCase() : '';
        const frequency = row.frequency ? row.frequency.toString().toLowerCase() : '';
        const format = row.format ? row.format.name.toString().toLowerCase() : '';
        const flowInterface = row.flowInterface ? row.flowInterface.alias.toString().toLowerCase() : '';
        const protocol =
          row.flowInterface != null && row.flowInterface.protocol != null ? row.flowInterface.protocol.name.toString().toLowerCase() : '';

        const searchTerm = this.filter.toLowerCase();

        return (
          data_id.includes(searchTerm) ||
          name.includes(searchTerm) ||
          description.includes(searchTerm) ||
          frequency.includes(searchTerm) ||
          format.includes(searchTerm) ||
          flowInterface.includes(searchTerm) ||
          protocol.includes(searchTerm)
        );
      });
    },
  },
})
export default class DataFlow extends Vue {
  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  private removeId: number = null;

  public dataFlows: IDataFlow[] = [];

  public isFetching = false;

  public filter = '';

  public mounted(): void {
    this.retrieveAllDataFlows();
  }

  public clear(): void {
    this.retrieveAllDataFlows();
  }

  public retrieveAllDataFlows(): void {
    this.isFetching = true;
    this.dataFlowService()
      .retrieve()
      .then(
        res => {
          this.dataFlows = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IDataFlow): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDataFlow(): void {
    this.dataFlowService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A DataFlow is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDataFlows();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
