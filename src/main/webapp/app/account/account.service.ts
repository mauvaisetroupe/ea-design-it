import axios from 'axios';
import { Store } from 'vuex';
import VueRouter from 'vue-router';

export default class AccountService {
  private hasAdminAuthorityValue = false;
  private hasUserAuthorityValue = false;
  private hasWriteAuthorityValue = false;
  private hasDeleteAuthorityValue = false;
  private hasContributorAuthorityValue = false;

  public anonymousReadAllowed = true;

  constructor(private store: Store<any>, private router: VueRouter) {
    this.init();
  }

  public init(): void {
    this.retrieveProfiles();
  }

  public retrieveProfiles(): Promise<boolean> {
    return new Promise(resolve => {
      axios
        .get<any>('management/info')
        .then(res => {
          if (res.data && res.data.activeProfiles) {
            this.store.commit('setRibbonOnProfiles', res.data['display-ribbon-on-profiles']);
            this.store.commit('setActiveProfiles', res.data['activeProfiles']);
          }
          resolve(true);
        })
        .catch(() => resolve(false));
    });
  }

  public retrieveAccount(): Promise<boolean> {
    return new Promise(resolve => {
      axios
        .get<any>('api/account')
        .then(response => {
          this.store.commit('authenticate');
          const account = response.data;
          if (account) {
            this.store.commit('authenticated', account);
            if (sessionStorage.getItem('requested-url')) {
              this.router.replace(sessionStorage.getItem('requested-url'));
              sessionStorage.removeItem('requested-url');
            }
            this.hasUserAuthority();
            this.hasWriteAuthority();
            this.hasDeleteAuthority();
            this.hasContributorAuthority();
          } else {
            this.store.commit('logout');
            this.router.push('/');
            sessionStorage.removeItem('requested-url');
          }
          resolve(true);
        })
        .catch(() => {
          this.store.commit('logout');
          resolve(false);
        });
    });
  }

  public hasAnyAuthorityAndCheckAuth(authorities: any): Promise<boolean> {
    if (typeof authorities === 'string') {
      authorities = [authorities];
    }

    if (!this.authenticated || !this.userAuthorities) {
      const token = localStorage.getItem('jhi-authenticationToken') || sessionStorage.getItem('jhi-authenticationToken');
      if (!this.store.getters.account && !this.store.getters.logon && token) {
        this.retrieveAccount();
        return Promise.resolve(false);
      }
    }

    for (const authority of authorities) {
      if (this.userAuthorities.includes(authority)) {
        return Promise.resolve(true);
      }
    }

    return Promise.resolve(false);
  }

  public get authenticated(): boolean {
    return this.store.getters.authenticated;
  }

  public get userAuthorities(): any {
    return this.store.getters.account.authorities;
  }

  public hasUserAuthority() {
    this.hasAnyAuthorityAndCheckAuth('ROLE_USER').then(value => {
      this.hasUserAuthorityValue = value;
    });
  }

  public hasWriteAuthority() {
    this.hasAnyAuthorityAndCheckAuth('ROLE_WRITE').then(value => {
      this.hasWriteAuthorityValue = value;
    });
  }

  public hasDeleteAuthority() {
    this.hasAnyAuthorityAndCheckAuth('ROLE_HARD_DELETE').then(value => {
      this.hasDeleteAuthorityValue = value;
    });
  }

  public hasContributorAuthority() {
    this.hasAnyAuthorityAndCheckAuth('ROLE_CONTRIBUTOR').then(value => {
      this.hasContributorAuthorityValue = value;
    });
  }

  public get readAuthorities(): boolean {
    if (this.anonymousReadAllowed) {
      //anonymous read
      return true;
    } else {
      return this.hasUserAuthorityValue;
    }
  }

  public get writeAuthorities(): boolean {
    return this.store.getters.authenticated && this.hasWriteAuthorityValue;
  }

  public get deleteAuthorities(): boolean {
    return this.store.getters.authenticated && this.hasDeleteAuthorityValue;
  }

  public get contributorAuthorities(): boolean {
    return this.store.getters.authenticated && this.hasContributorAuthorityValue;
  }

  public get writeOrContributor(): boolean {
    return this.store.getters.authenticated && (this.hasWriteAuthorityValue || this.hasContributorAuthorityValue);
  }
}
