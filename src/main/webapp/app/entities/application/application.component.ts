import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IApplication } from '@/shared/model/application.model';

import ApplicationService from './application.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';
import ApplicationImportService from '@/entities/application-import/application-import.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
@Component({
  computed: {
    filteredRows() {
      return this.applications.filter(row => {
        const name = row.name.toString().toLowerCase();
        const app_id = row.id.toString().toLowerCase();
        const technology = row.technology ? row.technology.toString().toLowerCase() : '';
        const description = row.description ? row.description.toString().toLowerCase() : '';
        const type = row.type ? row.type.toString().toLowerCase() : '';
        const comment = row.comment ? row.comment.toString().toLowerCase() : '';
        const owner = row.owner ? row.owner.toString().toLowerCase() : '';

        const searchTerm = this.filter.toLowerCase();

        return (
          name.includes(searchTerm) ||
          app_id.includes(searchTerm) ||
          technology.includes(searchTerm) ||
          description.includes(searchTerm) ||
          type.includes(searchTerm) ||
          comment.includes(searchTerm) ||
          owner.includes(searchTerm)
        );
      });
    },
  },
})
export default class Application extends Vue {
  @Inject('applicationService') private applicationService: () => ApplicationService;
  @Inject('applicationImportService') private applicationImportService: () => ApplicationImportService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  private removeId: number = null;

  public applications: IApplication[] = [];

  public isFetching = false;

  public filter = '';

  public selectedApplicationIds: number[] = [];

  public mounted(): void {
    this.retrieveAllApplications();
  }

  public clear(): void {
    sessionStorage.removeItem('applications');
    this.retrieveAllApplications();
  }

  public retrieveAllApplications(): void {
    this.isFetching = true;
    this.applicationService()
      .retrieve()
      .then(
        res => {
          this.applications = res.data;
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

  public prepareRemove(instance: IApplication): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeApplication(): void {
    this.applicationService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Application is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllApplications();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public isOwner(application: IApplication): Boolean {
    const username = this.$store.getters.account?.login ?? '';
    if (this.accountService().writeAuthorities) {
      return true;
    }
    if (application.owner && application.owner.users) {
      for (const user of application.owner.users) {
        if (user.login === username) {
          return true;
        }
      }
    }
    return false;
  }

  public exportExcel() {
    this.applicationImportService()
      .downloadFile()
      .then(response => {
        const url = URL.createObjectURL(
          new Blob([response.data], {
            type: 'application/vnd.ms-excel',
          })
        );
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'applications-export.xlsx');
        document.body.appendChild(link);
        link.click();
      });
  }
}
