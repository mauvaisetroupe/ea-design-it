import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
@Component({
  computed: {
    filteredRows() {
      return this.flowInterfaces.filter(row => {
        const searchTerm = this.filter.toLowerCase();
        const id = row.id.toString().toLowerCase();
        const description = row.description ? row.description.toString().toLowerCase() : '';
        const alias = row.alias.toString().toLowerCase();
        const source = row.source.name.toString().toLowerCase();
        const target = row.target.name.toString().toLowerCase();
        const proto = row.protocol ? row.protocol.name.toString().toLowerCase() : '';
        if (searchTerm == 'protocol::empty') {
          return proto === '';
        } else {
          return (
            id.includes(searchTerm) ||
            description.includes(searchTerm) ||
            alias.includes(searchTerm) ||
            source.includes(searchTerm) ||
            target.includes(searchTerm) ||
            proto.includes(searchTerm)
          );
        }
      });
    },
  },
})
export default class FlowInterface extends Vue {
  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  private removeId: number = null;

  public flowInterfaces: IFlowInterface[] = [];

  public isFetching = false;

  public filter: string = '';

  public mounted(): void {
    this.retrieveAllFlowInterfaces();
    if (this.$route.query.searchTerm) {
      this.filter = this.$route.query.searchTerm as string;
    }
  }

  public clear(): void {
    this.retrieveAllFlowInterfaces();
  }

  public retrieveAllFlowInterfaces(): void {
    this.isFetching = true;
    this.flowInterfaceService()
      .retrieve()
      .then(
        res => {
          this.flowInterfaces = res.data;
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

  public prepareRemove(instance: IFlowInterface): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFlowInterface(): void {
    this.flowInterfaceService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A FlowInterface is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFlowInterfaces();
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
