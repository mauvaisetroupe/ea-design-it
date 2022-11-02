// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.common with an alias.
import Vue from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import App from './app.vue';
import Vue2Filters from 'vue2-filters';
import { ToastPlugin } from 'bootstrap-vue';
import router from './router';
import * as config from './shared/config/config';
import * as bootstrapVueConfig from './shared/config/config-bootstrap-vue';
import JhiItemCountComponent from './shared/jhi-item-count.vue';
import JhiSortIndicatorComponent from './shared/sort/jhi-sort-indicator.vue';
import InfiniteLoading from 'vue-infinite-loading';
import HealthService from './admin/health/health.service';
import MetricsService from './admin/metrics/metrics.service';
import LogsService from './admin/logs/logs.service';
import ConfigurationService from '@/admin/configuration/configuration.service';
import ActivateService from './account/activate/activate.service';
import RegisterService from './account/register/register.service';
import UserManagementService from './admin/user-management/user-management.service';
import LoginService from './account/login.service';
import AccountService from './account/account.service';
import AlertService from './shared/alert/alert.service';

import '../content/scss/global.scss';
import '../content/scss/vendor.scss';
/* tslint:disable */

import ApplicationImportService from '@/entities/application-import/application-import.service';
import ComponentImportService from '@/entities/component-import/component-import.service';
import FlowImportService from '@/entities/flow-import/flow-import.service';
import DataFlowImportService from '@/entities/data-flow-import/data-flow-import.service';
import SequenceDiagramService from '@/eadesignit/sequence-diagram/import.service';
import ApplicationsDiagramService from '@/eadesignit/applications-diagram/applications-diagram.service';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import ApplicationService from '@/entities/application/application.service';

// jhipster-needle-add-entity-service-to-main-import - JHipster will import entities services here

import ReportingService from '@/eadesignit/reporting.service';
import { Authority } from './shared/security/authority';
import CapabilityImportService from '@/entities/capability-import/capability-import.service';

/* tslint:enable */
Vue.config.productionTip = false;
config.initVueApp(Vue);
config.initFortAwesome(Vue);
bootstrapVueConfig.initBootstrapVue(Vue);
Vue.use(Vue2Filters);
Vue.use(ToastPlugin);
Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.component('jhi-item-count', JhiItemCountComponent);
Vue.component('jhi-sort-indicator', JhiSortIndicatorComponent);
Vue.component('infinite-loading', InfiniteLoading);
const store = config.initVueXStore(Vue);

const loginService = new LoginService();
const accountService = new AccountService(store, router);
const applicationService = new ApplicationService();

router.beforeEach(async (to, from, next) => {
  // Check if anonymous read is allowed
  console.log('SHOULD LOAD ANONYMOUS READING PARAMETER');

  if (!to.matched.length) {
    next('/not-found');
  }

  if (to.meta && to.meta.authorities && to.meta.authorities.length > 0) {
    console.log('to.meta.authorities' + to.meta.authorities);

    if (to.meta.authorities.includes(Authority.ANONYMOUS_ALLOWED) && accountService.anonymousReadAllowed) {
      next();
    } else {
      accountService.hasAnyAuthorityAndCheckAuth(to.meta.authorities).then(value => {
        if (!value) {
          console.log(to);
          console.log('hasAnyAuthorityAndCheckAuth(' + to.meta.authorities + ') -> ' + value);
          sessionStorage.setItem('requested-url', to.fullPath);
          next('/forbidden');
        } else {
          next();
        }
      });
    }
  } else {
    // no authorities, so just proceed
    next();
  }
});

/* tslint:disable */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>',
  router,
  provide: {
    loginService: () => loginService,
    activateService: () => new ActivateService(),
    registerService: () => new RegisterService(),
    userManagementService: () => new UserManagementService(),
    healthService: () => new HealthService(),
    configurationService: () => new ConfigurationService(),
    logsService: () => new LogsService(),
    metricsService: () => new MetricsService(),

    applicationImportService: () => new ApplicationImportService(),
    componentImportService: () => new ComponentImportService(),
    flowImportService: () => new FlowImportService(),
    dataFlowImportService: () => new DataFlowImportService(),
    sequenceDiagramService: () => new SequenceDiagramService(),
    applicationsDiagramService: () => new ApplicationsDiagramService(),

    // jhipster-needle-add-entity-service-to-main - JHipster will import entities services here
    accountService: () => accountService,
    alertService: () => new AlertService(),

    reportingService: () => new ReportingService(),
    capabilityImportService: () => new CapabilityImportService(),
    landscapeViewService: () => new LandscapeViewService(),

    applicationService: () => new ApplicationService(),
  },
  store,
  created: function () {
    applicationService.retrieve().then(res => {
      console.log('Applications loaded');
    });
  },
});
