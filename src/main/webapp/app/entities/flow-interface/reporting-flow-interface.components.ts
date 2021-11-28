import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';
import ReportingService from '@/eadesignit/reporting.service';
import { DataFlow } from '@/shared/model/data-flow.model';

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

  private interfaceToKeep: IFlowInterface = null;
  private interfacesToMerge: IFlowInterface[] = null;
  private dataFlowsToMerge: DataFlow[] = null;
  private checkToMerge = [];

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

  public prepareMerge(instance: IFlowInterface): void {
    this.interfaceToKeep = instance;
    var aliasToMerge = (this.interfaceToKeep as any).mergeList as String[];
    console.log(aliasToMerge);

    this.interfacesToMerge = [];
    this.dataFlowsToMerge = [];
    this.checkToMerge = [];

    this.flowInterfaces.forEach(element => {
      if (aliasToMerge.indexOf(element.alias) > -1) {
        this.interfacesToMerge.push(element);
        if (element.id != this.interfaceToKeep.id) {
          this.checkToMerge.push(element.alias);
        }
        element.dataFlows.forEach(df => {
          if (df.flowInterface == null) {
            df.flowInterface = {};
          }
          df.flowInterface.alias = element.alias;
          this.dataFlowsToMerge.push(df);
        });
      }
    });

    if (<any>this.$refs.mergeEntity) {
      (<any>this.$refs.mergeEntity).show();
    }
  }

  public mergeFlowInterface(): void {
    this.reportingService()
      .mergeInterfaces(this.interfaceToKeep, this.checkToMerge)
      .then(() => {
        const message = 'FlowInterfaces ' + this.checkToMerge + ' have been merged and replaced by ' + this.interfaceToKeep.alias;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.interfaceToKeep = null;
        this.interfacesToMerge = null;
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
