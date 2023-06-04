import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IApplication } from '@/shared/model/application.model';

import ApplicationService from './application.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';
import ApplicationImportService from '@/entities/application-import/application-import.service';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';

@Component({
  mixins: [Vue2Filters.mixin],
})
@Component({
  computed: {
    filteredApplications() {
      return this.applications.filter(app => {
        var appType: boolean = true;
        if (this.applicationTypeSelected) {
          appType = app.applicationType && app.applicationType.valueOf() == this.applicationTypeSelected;
        }
        var softwareType: boolean = true;
        if (this.softwareTypeSelected) {
          softwareType = app.softwareType && app.softwareType.valueOf() == this.softwareTypeSelected;
        }
        return appType && softwareType;
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

  public showAdvanced = false;

  public fields = [
    { key: 'CHECKBOX', sortable: false, label: '' },
    // { key: 'id', sortable: false },
    { key: 'alias', sortable: true },
    { key: 'name', sortable: true },
    { key: 'description', sortable: false, formatter: 'formatLongText' },
    // {key:'comment', sortable: false},
    // {key:'documentationURL', sortable: false},
    // {key:'startDate', sortable: false},
    // {key:'endDate', sortable: false},
    { key: 'applicationType', sortable: false },
    { key: 'softwareType', sortable: false },
    { key: 'nickname', sortable: false },
    { key: 'owner.name', sortable: false, label: 'Owner' },
    { key: 'itOwner.name', sortable: false, label: 'IT Owner' },
    { key: 'businessOwner.name', sortable: false, label: 'Business Owner' },
    { key: 'categories', sortable: false },
    { key: 'technologies', sortable: false },
    //{ key: 'capabilities', sortable: false },
    // {key:'externalIDS', sortable: false},
    // {key:'applicationsLists', sortable: false},
  ];
  public sortBy = 'name';
  public sortDesc = false;
  public filterOn = ['alias', 'name', 'description'];
  public formatLongText(value, key, item) {
    var max = 600;
    if (value && value.length > max) {
      return value.substring(0, max) + '...';
    } else {
      return value;
    }
  }
  public applicationTypeValues: string[] = Object.keys(ApplicationType);
  public softwareTypeValues: string[] = Object.keys(SoftwareType);

  public applicationTypeSelected = '';
  public softwareTypeSelected = '';

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
