import Component from 'vue-class-component';
import { Inject, Vue } from 'vue-property-decorator';
import LoginService from '@/account/login.service';
import AccountService from '@/account/account.service';
import ExportService from '@/eadesignit/export.service';

@Component
export default class Home extends Vue {
  @Inject('loginService')
  private loginService: () => LoginService;

  @Inject('accountService') public accountService: () => AccountService;

  @Inject('exportService') private exportService: () => ExportService;

  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }

  public exportExcel() {
    this.exportService()
      .downloadFile()
      .then(response => {
        const url = URL.createObjectURL(
          new Blob([response.data], {
            type: 'application/vnd.ms-excel',
          })
        );
        const link = document.createElement('a');
        link.href = url;
        var today = new Date().toISOString().split('T')[0];
        var time = new Date().toLocaleTimeString().replace(' ', '_');
        link.setAttribute('download', 'full-data-export-' + today + '-' + time + '.xlsx');
        document.body.appendChild(link);
        link.click();
      });
  }
}
