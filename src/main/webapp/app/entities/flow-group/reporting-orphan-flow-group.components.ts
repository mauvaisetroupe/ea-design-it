import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowGroup } from '@/shared/model/flow-group.model';

import ReportingService from '@/eadesignit/reporting.service';
import AlertService from '@/shared/alert/alert.service';
import FlowGroupService from './flow-group.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FlowGroup extends Vue {
  @Inject('reportingService') private reportingService: () => ReportingService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('flowGroupService') private flowGroupService: () => FlowGroupService;

  private removeId: number = null;

  public flowGroups: IFlowGroup[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllFlowGroups();
  }

  public clear(): void {
    this.retrieveAllFlowGroups();
  }

  public retrieveAllFlowGroups(): void {
    this.isFetching = true;
    this.reportingService()
      .retrieveOrphanFlowGroup()
      .then(
        res => {
          this.flowGroups = res.data;
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
}
