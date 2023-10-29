import { defineStore } from 'pinia';

export interface AccountStateStorable {
  logon: boolean;
  userIdentity: null | any;
  authenticated: boolean;
  profilesLoaded: boolean;
  ribbonOnProfiles: string;
  activeProfiles: string;
}

export const defaultAccountState: AccountStateStorable = {
  logon: null,
  userIdentity: null,
  authenticated: false,
  profilesLoaded: false,
  ribbonOnProfiles: '',
  activeProfiles: '',
};

export const useAccountStore = defineStore('main', {
  state: (): AccountStateStorable => ({ ...defaultAccountState }),
  getters: {
    account: state => state.userIdentity,
    //authenticated: state => state.authenticated,
    // activeProfiles: state => state.activeProfiles,
    // ribbonOnProfiles: state => state.ribbonOnProfiles,
    writeAuthority: state => hasAuthority(state, 'ROLE_WRITE'),
    userAuthority: state => hasAuthority(state, 'ROLE_USER'),
    deleteAuthority: state => hasAuthority(state, 'ROLE_HARD_DELETE'),
    contributorAuthority: state => hasAuthority(state, 'ROLE_CONTRIBUTOR'),
    adminAuthority: state => hasAuthority(state, 'ROLE_ADMIN'),
  },
  actions: {
    authenticate(promise) {
      this.logon = promise;
    },
    setAuthentication(identity) {
      this.userIdentity = identity;
      this.authenticated = true;
      this.logon = null;
    },
    logout() {
      this.userIdentity = null;
      this.authenticated = false;
      this.logon = null;
    },
    setProfilesLoaded() {
      this.profilesLoaded = true;
    },
    setActiveProfiles(profile) {
      this.activeProfiles = profile;
    },
    setRibbonOnProfiles(ribbon) {
      this.ribbonOnProfiles = ribbon;
    },
  },
});

function hasAuthority(state, role): boolean {
  console.log('performance issue?');
  return state.userIdentity?.authorities?.includes(role);
}
