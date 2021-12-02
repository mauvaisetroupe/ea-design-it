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

          let mycolor = 'mycolor';
          let previousTuple = '';
          let mergeList = [];
          this.flowInterfaces.forEach(element => {
            if (previousTuple !== element.source.name + element.target.name + element.protocol.id) {
              mergeList = [];
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
    const aliasToMerge = (this.interfaceToKeep as any).mergeList as string[];
    console.log(aliasToMerge);

    this.interfacesToMerge = [];
    this.dataFlowsToMerge = [];
    this.checkToMerge = [];

    this.flowInterfaces.forEach(inter => {
      if (aliasToMerge.indexOf(inter.alias) > -1) {
        this.interfacesToMerge.push(inter);
        if (inter.id != this.interfaceToKeep.id) {
          this.checkToMerge.push(inter.alias);
        }
        inter.dataFlows.forEach(df => {
          if (df.flowInterface == null) {
            df.flowInterface = {};
          }
          df.flowInterface.alias = inter.alias;

          if (df.flowInterface.functionalFlows == null) {
            df.flowInterface.functionalFlows = [];
          }
          inter.functionalFlows.forEach(element => {
            const funct = {
              alias: element.alias,
            };
            df.flowInterface.functionalFlows.push(funct);
          });

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
        this.dataFlowsToMerge = null;
        this.checkToMerge = [];
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
