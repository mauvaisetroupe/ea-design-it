import axios from 'axios';

import { type AccountStore } from '@/store';
import { Authority } from '@/shared/security/authority';

export default class AccountService {
  //constructor(private store: AccountStore, private router: VueRouter) {
  constructor(private store: AccountStore) {
    this.init();
  }

  public async update(): Promise<void> {
    if (!this.store.profilesLoaded) {
      await this.retrieveProfiles();
      this.store.setProfilesLoaded();
    }
    await this.loadAccount();
  }

  public async retrieveProfiles(): Promise<boolean> {
    try {
      const res = await axios.get<any>('/management/info');
      if (res.data && res.data.activeProfiles) {
        this.store.setRibbonOnProfiles(res.data['display-ribbon-on-profiles']);
        this.store.setActiveProfiles(res.data['activeProfiles']);
      }
      return true;
    } catch (error) {
      return false;
    }
  }

  public async retrieveAccount(): Promise<boolean> {
    try {
      const response = await axios.get<any>('api/account');
      if (response.status === 200 && response.data) {
        const account = response.data;
        this.store.setAuthentication(account);
        return true;
      }
    } catch (error) {
      // Ignore error
    }

    this.store.logout();
    return false;
  }

  public async loadAccount() {
    if (this.store.logon) {
      return this.store.logon;
    }
    const token = localStorage.getItem('jhi-authenticationToken') || sessionStorage.getItem('jhi-authenticationToken');
    if (this.authenticated && this.userAuthorities && token) {
      return;
    }

    const promise = this.retrieveAccount();
    this.store.authenticate(promise);
    promise.then(() => this.store.authenticate(null));
    await promise;
  }

  public async hasAnyAuthorityAndCheckAuth(authorities: any): Promise<boolean> {
    if (typeof authorities === 'string') {
      authorities = [authorities];
    }

    return this.checkAuthorities(authorities);
  }

  public get authenticated(): boolean {
    return this.store.authenticated;
  }

  public get userAuthorities(): string[] {
    return this.store.account?.authorities;
  }

  public get writeAuthorities(): boolean {
    return this.store.writeAuthority;
  }

  public get deleteAuthorities(): boolean {
    return this.store.deleteAuthority;
  }

  public get contributorAuthorities(): boolean {
    return this.store.contributorAuthority;
  }

  public get writeOrContributor(): boolean {
    return this.writeAuthorities || this.contributorAuthorities;
  }

  private checkAuthorities(authorities: string[]): boolean {
    if (authorities.includes(Authority.ANONYMOUS_ALLOWED) && this.anonymousReadAllowed) {
      return true;
    }
    if (this.userAuthorities) {
      for (const authority of authorities) {
        if (this.userAuthorities.includes(authority)) {
          return true;
        }
      }
    }
    return false;
  }

  // Anonymous reader
  public anonymousReadAllowed = true;
  public initialized = false;

  public init(): void {
    this.retrieveProfiles();
    this.retrieveAnonymousProperty();
    // remember me...
    const token = localStorage.getItem('jhi-authenticationToken') || sessionStorage.getItem('jhi-authenticationToken');
    if (!this.store.account && !this.store.logon && token) {
      this.retrieveAccount();
    }
  }

  public retrieveAnonymousProperty(): Promise<boolean> {
    if (this.initialized) {
      console.log('retrieveAnonymousProperty already initialized');
      return Promise.resolve(this.anonymousReadAllowed);
    }
    console.log('About to call api/account/anoymous-reader');
    return new Promise(resolve => {
      axios
        .get<any>('api/account/anoymous-reader')
        .then(res => {
          this.anonymousReadAllowed = res.data;
          this.initialized = true;
          console.log('api/account/anoymous-reader : ' + this.anonymousReadAllowed);
          resolve(this.anonymousReadAllowed);
        })
        .catch(() => {
          this.initialized = true;
          resolve(this.anonymousReadAllowed);
        });
    });
  }

  public get readAuthorities(): boolean {
    if (this.anonymousReadAllowed) {
      //anonymous read
      return true;
    } else {
      return this.store.userAuthority;
    }
  }
}

// THIS WAS A BUG, naybe corrected in jhipster ?
// public retrieveAccount(): Promise<boolean> {
//   return new Promise(resolve => {
//     axios
//       .get<any>('api/account')
//       .then(response => {
//         this.store.commit('authenticate');
//         const account = response.data;
//         if (account) {
//           this.store.commit('authenticated', account);
//           if (sessionStorage.getItem('requested-url')) {
//             this.router.replace(sessionStorage.getItem('requested-url'));
//             sessionStorage.removeItem('requested-url');
//           }
//         } else {
//           this.store.commit('logout');
//           if (this.router.currentRoute.path !== '/') {
//             this.router.push('/');
//           }
//           sessionStorage.removeItem('requested-url');
//         }
//         resolve(true);
//       })
//       .catch(() => {
//         this.store.commit('logout');
//         resolve(false);
//       });
//   });
// }

// public hasAnyAuthorityAndCheckAuth(authorities: any): Promise<boolean> {
//   if (typeof authorities === 'string') {
//     authorities = [authorities];
//     }

//   this.store.logout();
//   return false;
// }
// public retrieveProfiles(): Promise<boolean> {
//   return new Promise(resolve => {
//     axios
//       .get<any>('management/info')
//       .then(res => {
//         if (res.data && res.data.activeProfiles) {
//           this.store.commit('setRibbonOnProfiles', res.data['display-ribbon-on-profiles']);
//           this.store.commit('setActiveProfiles', res.data['activeProfiles']);
//         }
//         resolve(true);
//       })
//       .catch(() => resolve(false));
//   });
// }
