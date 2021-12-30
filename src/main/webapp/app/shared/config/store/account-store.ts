import { Module } from 'vuex';

export const accountStore: Module<any, any> = {
  state: {
    logon: false,
    userIdentity: null,
    authenticated: false,
    ribbonOnProfiles: '',
    activeProfiles: '',
  },
  getters: {
    logon: state => state.logon,
    account: state => state.userIdentity,
    authenticated: state => state.authenticated,
    activeProfiles: state => state.activeProfiles,
    ribbonOnProfiles: state => state.ribbonOnProfiles,
    writeAuthority: state => hasAuthority(state, 'ROLE_WRITE'),
    userAuthority: state => hasAuthority(state, 'ROLE_USER'),
    deleteAuthority: state => hasAuthority(state, 'ROLE_HARD_DELETE'),
    contributorAuthority: state => hasAuthority(state, 'ROLE_CONTRIBUTOR'),
    adminAuthority: state => hasAuthority(state, 'ROLE_ADMIN'),
  },
  mutations: {
    authenticate(state) {
      state.logon = true;
    },
    authenticated(state, identity) {
      state.userIdentity = identity;
      state.authenticated = true;
      state.logon = false;
    },
    logout(state) {
      state.userIdentity = null;
      state.authenticated = false;
      state.logon = false;
    },
    setActiveProfiles(state, profile) {
      state.activeProfiles = profile;
    },
    setRibbonOnProfiles(state, ribbon) {
      state.ribbonOnProfiles = ribbon;
    },
  },
};

function hasAuthority(state, role): boolean {
  console.log('performance issue?');
  return state.userIdentity?.authorities?.includes(role);
}
