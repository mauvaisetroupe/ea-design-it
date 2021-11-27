import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';
import ReportingService from '@/eadesignit/reporting.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
@Component({
  computed: {
    filteredRows() {
      return this.flowInterfaces.filter(row => {
        const id = row.id.toString().toLowerCase();
        const description = row.description ? row.description.toString().toLowerCase() : '';
        const alias = row.alias.toString().toLowerCase();
        const source = row.source.name.toString().toLowerCase();
        const target = row.target.name.toString().toLowerCase();
        const proto = row.protocol ? row.protocol.name.toString().toLowerCase() : '';

        const searchTerm = this.filter.toLowerCase();

        return (
          id.includes(searchTerm) ||
          description.includes(searchTerm) ||
          alias.includes(searchTerm) ||
          source.includes(searchTerm) ||
          target.includes(searchTerm) ||
          proto.includes(searchTerm)
        );
      });
    },
  },
})
export default class FlowInterface extends Vue {
  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('reportingService') private reportingService: () => ReportingService;

  private removeId: number = null;
  private interfaceToMerge: IFlowInterface = null;

  public flowInterfaces: IFlowInterface[] = [];

  public isFetching = false;

  public filter = '';

  public mounted(): void {
    this.retrieveAllFlowInterfaces();
  }

  public clear(): void {
    this.retrieveAllFlowInterfaces();
  }

  public retrieveAllFlowInterfaces(): void {
    this.isFetching = true;
    this.reportingService()
      .retrieveInterfaces()
      .then(
        res => {
          this.flowInterfaces = res.data;
          this.isFetching = false;

          var mycolor = 'mycolor';
          var previousTuple = '';
          var mergeList = new Array();
          this.flowInterfaces.forEach(element => {
            if (previousTuple !== element.source.name + element.target.name + element.protocol.id) {
              mergeList = new Array();
              if (mycolor === 'mycolor') {
                mycolor = '';
              } else {
                mycolor = 'mycolor';
              }
            }
            mergeList.push(element.alias);
            previousTuple = element.source.name + element.target.name + element.protocol.id;
            (element as any).colored = mycolor;
            (element as any).mergeList = mergeList;
          });
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

  public prepareMerge(instance: IFlowInterface): void {
    this.interfaceToMerge = instance;
    if (<any>this.$refs.mergeEntity) {
      (<any>this.$refs.mergeEntity).show();
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

  public mergeFlowInterface(): void {
    this.reportingService()
      .mergeInterfaces(this.interfaceToMerge)
      .then(() => {
        const message =
          'FlowInterfaces ' + (this.interfaceToMerge as any).mergeList + ' have been merged and replaced by ' + this.interfaceToMerge.alias;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.interfaceToMerge = null;
        this.retrieveAllFlowInterfaces();
        this.closeMergeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }
  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public closeMergeDialog(): void {
    (<any>this.$refs.mergeEntity).hide();
  }
}
