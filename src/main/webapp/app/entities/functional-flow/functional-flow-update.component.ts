import { computed, defineComponent, inject, onMounted, ref, watch, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FunctionalFlowService from './functional-flow.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import { type IFunctionalFlow, FunctionalFlow } from '@/shared/model/functional-flow.model';
import type { IPlantumlFlowImport } from '@/shared/model/plantuml-flow-import.model';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import type { ILandscapeView } from '@/shared/model/landscape-view.model';
import ApplicationService from '../application/application.service';
import { nextTick } from 'process';
import type { bvToast } from 'bootstrap-vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlowUpdate',
  setup() {
    //@Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;
    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());
    //@Inject('alertService') private alertService: () => AlertService;
    const alertService = inject('alertService', () => useAlertService(), true);

    //public functionalFlow: IFunctionalFlow = new FunctionalFlow();
    const functionalFlow: Ref<IFunctionalFlow> = ref(new FunctionalFlow());

    //@Inject('ownerService') private ownerService: () => OwnerService;
    const ownerService = inject('ownerService', () => new OwnerService());

    //public owners: IOwner[] = [];
    const owners: Ref<IOwner[]> = ref([]);

    //@Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());

    //public allLandscapes: ILandscapeView[] = [];
    const allLandscapes: Ref<ILandscapeView[]> = ref([]);
    //public selectedLandscape: ILandscapeView = {};
    const selectedLandscape: Ref<ILandscapeView> = ref({});

    //@Inject('applicationService') private applicationService: () => ApplicationService;
    const applicationService = inject('applicationService', () => new ApplicationService());

    //public applications: string[] = [];
    const applications: Ref<string[]> = ref([]);

    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);
    const plantuml = ref('');
    const plantUMLImage = ref('');
    const isFetching = ref(false);
    const importError = ref('');
    const previewError = ref('');
    const functionalFlowImport: Ref<IPlantumlFlowImport> = ref({});
    const tabIndex: Ref<number> = ref(1);
    const landscapeGivenInParameter = ref(false);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    ////////////////////////////////////////////////
    // On load, retrieve
    // - FunctionFlow,
    // - plantUML source fron flowID
    // - plantUML image
    // - table with potential interfaces (should be the same than flow detail)
    /////////////////////////////////////////////////

    // STEP 1 - Retrieve FunctionalFlow
    const retrieveFunctionalFlow = async functionalFlowId => {
      try {
        const res = await functionalFlowService().find(functionalFlowId);
        functionalFlow.value = res;
        getPlantUMLSourceFromFlowId(functionalFlowId);
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.functionalFlowId) {
      retrieveFunctionalFlow(route.params.functionalFlowId);
    } else {
      // for creation, go on Information TAB
      // https://github.com/bootstrap-vue/bootstrap-vue/issues/2803
      nextTick(() => {
        tabIndex.value = 0;
      });
    }

    // STEP 2 - Retrieve plantuml source from flow ID
    function getPlantUMLSourceFromFlowId(functionalFlowId) {
      functionalFlowService()
        .getPlantUMLSource(functionalFlowId, true, true)
        .then(
          res => {
            plantuml.value = res.data;
            isFetching.value = false;
            getPlantUMLImageFromString();
          },
          err => {
            console.log(err);
          },
        );
    }

    // STEP 3 : Retrieve plantuml Image from plantuml source
    function getPlantUMLImageFromString() {
      functionalFlowService()
        .getPlantUMLFromString(plantuml.value)
        .then(
          res => {
            plantUMLImage.value = res.data;
            plantumlModified.value = false;
            isFetching.value = false;
            importPlantuml();
          },
          err => {
            console.log(err);
            plantUMLImage.value = '';
          },
        );
    }

    // STEP 3 : Retrieve interface list from plantuml Source
    function importPlantuml() {
      functionalFlowService()
        .importPlantuml(plantuml.value)
        .then(
          res => {
            functionalFlowImport.value = res.data;
            if (functionalFlowImport.value && functionalFlowImport.value.flowImportLines) {
              functionalFlowImport.value.flowImportLines.forEach(step => {
                if (step.selectedInterface) {
                  step.interfaceAlias = step.selectedInterface.alias;
                }
              });
            }
            isFetching.value = false;
            previewError.value = '';
          },
          err => {
            plantUMLImage.value = '';
            functionalFlowImport.value = { flowImportLines: [] };
            previewError.value = err;
            plantumlModified.value = false;
          },
        );
    }

    function changeInterface(flowimportLine) {
      if (!flowimportLine.selectedInterface) {
        flowimportLine.interfaceAlias = '';
      } else {
        flowimportLine.interfaceAlias = flowimportLine.selectedInterface.alias;
      }
    }

    function tabChanged() {
      if (plantumlModified.value) getPlantUMLImageFromString();
    }

    //////////////////////////////////////////////////
    // SAVE
    //////////////////////////////////////////////////

    const creation = computed(() => {
      return !functionalFlow.value.id;
    });

    const aliasesValid = computed(() => {
      if (!functionalFlowImport.value || !functionalFlowImport.value.flowImportLines) return true;
      return !functionalFlowImport.value.flowImportLines.some(step => !step.interfaceAlias || step.interfaceAlias === '');
    });

    function save(): void {
      isSaving.value = true;

      functionalFlowImport.value.id = functionalFlow.value.id;
      functionalFlowImport.value.alias = functionalFlow.value.alias;
      functionalFlowImport.value.description = functionalFlow.value.description;
      functionalFlowImport.value.comment = functionalFlow.value.comment;
      functionalFlowImport.value.status = functionalFlow.value.status;
      functionalFlowImport.value.documentationURL = functionalFlow.value.documentationURL;
      functionalFlowImport.value.documentationURL2 = functionalFlow.value.documentationURL;
      functionalFlowImport.value.startDate = functionalFlow.value.startDate;
      functionalFlowImport.value.endDate = functionalFlow.value.endDate;
      functionalFlowImport.value.owner = functionalFlow.value.owner;

      functionalFlowService()
        .saveImport(functionalFlowImport.value, selectedLandscape.value.id)
        .then(param => {
          isSaving.value = false;
          router.go(-1);
          let message = 'A FunctionalFlow is created with identifier ' + param.id;
          if (functionalFlow.value.id) {
            message = 'A FunctionalFlow is updated with identifier ' + param.id;
          }
          alertService.showInfo(message);
        })
        .catch(error => {
          isSaving.value = false;
          alertService.showAnyError(error);
        });
    }

    const initRelationships = () => {
      ownerService()
        .retrieve()
        .then(res => {
          owners.value = res.data;
        });
      landscapeViewService()
        .retrieve()
        .then(res => {
          allLandscapes.value = res.data;
          if (route.query.landscapeViewId) {
            landscapeGivenInParameter.value = true;
            allLandscapes.value.forEach(landscape => {
              if (landscape.id === parseInt(route.query.landscapeViewId as string)) {
                selectedLandscape.value = landscape;
              }
            });
          }
        });
    };

    initRelationships();

    ///////////////////////////////////////////////
    // appliction autocmplete
    ///////////////////////////////////////////////

    const plantumlModified = ref(false);

    const nbLines = computed(() => {
      return plantuml.value.split(/\r\n|\r|\n/).length;
    });

    watch(nbLines, () => {
      getPlantUMLImageFromString();
    });

    const lastLine = computed(() => {
      return plantuml.value.split(/\r\n|\r|\n/).slice(-1)[0];
    });

    watch(plantuml, () => {
      plantumlModified.value = true;
      //console.log(this.inputSplitted);
      selectedIndex.value = 0;
      wordIndex.value = inputSplitted.value.length - 1;
      focus();
    });

    const wordIndex = ref(0);
    const selectedIndex = ref(0);
    const searchMatch: Ref<string[]> = ref([]);
    const clickedChooseItem = ref(false);

    const listToSearch = computed(() => {
      const standardItems = applications.value;
      return standardItems;
    });

    const currentWord = computed(() => {
      return plantuml.value.replace(/(\r\n|\n|\r)/gm, ' ').split(' ')[wordIndex.value];
    });

    const inputSplitted = computed(() => {
      return plantuml.value.replace(/(\r\n|\n|\r)/gm, ' ').split(' ');
    });

    function highlightWord(word) {
      const regex = new RegExp('(' + currentWord.value + ')', 'g');
      return word.replace(regex, '<mark>$1</mark>');
    }
    function setWord(word) {
      const currentWords = plantuml.value.replace(/(\r\n|\n|\r)/gm, '__br__ ').split(' ');
      currentWords[wordIndex.value] = currentWords[wordIndex.value].replace(currentWord.value, '"' + word + '" ');
      wordIndex.value += 1;
      plantuml.value = currentWords.join(' ').replace(/__br__\s/g, '\n');
    }
    function moveDown() {
      if (selectedIndex.value < searchMatch.value.length - 1) {
        selectedIndex.value++;
      }
    }
    function moveUp() {
      if (selectedIndex.value !== -1) {
        selectedIndex.value--;
      }
    }
    function selectItem(index) {
      selectedIndex.value = index;
      chooseItem();
    }
    function chooseItem(e = null) {
      console.log(e);
      clickedChooseItem.value = true;

      if (selectedIndex.value !== -1 && searchMatch.value.length > 0) {
        if (e) {
          e.preventDefault();
        }
        setWord(searchMatch.value[selectedIndex.value]);
        selectedIndex.value = -1;
      }
    }
    function focusout(e) {
      setTimeout(() => {
        if (!clickedChooseItem.value) {
          searchMatch.value = [];
          selectedIndex.value = -1;
        }
        clickedChooseItem.value = false;
      }, 100);
    }
    function focus() {
      searchMatch.value = [];
      if (lastLine.value.includes(':')) {
        searchMatch.value = [];
      } else if (!currentWord.value || currentWord.value === '') {
        console.log(currentWord.value);
        searchMatch.value = [];
      } else if (currentWord.value.length > 2) {
        console.log(currentWord.value);
        searchMatch.value = listToSearch.value.filter(el => el.toLowerCase().indexOf(currentWord.value.toLowerCase()) >= 0);
      } else if (searchMatch.value.length === 1 && currentWord.value === searchMatch.value[0]) {
        searchMatch.value = [];
      }
    }

    const textareaNbLine = computed(() => {
      if (functionalFlow.value.steps) {
        let nb = functionalFlow.value.steps.length;
        const distinvtID: Set<number> = new Set(
          functionalFlow.value.steps
            .map(step => step.group)
            .filter(group => group) // to filter null values
            .map(group => group.id),
        );
        const nbGroups = distinvtID.size;
        nb = nb + 2 * nbGroups;
        const margin = 2;
        return nb + margin;
      }
      return 10;
    });

    onMounted(() => {
      applicationService()
        .retrieve()
        .then(res => {
          if (res.data) {
            applications.value = res.data.map(appli => appli.name);
          }
        });
    });

    const validations = useValidation();
    const validationRules = {
      alias: {},
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 1500 characters.', 1500),
      },
      comment: {
        maxLength: validations.maxLength('This field cannot be longer than 1000 characters.', 1000),
      },
      status: {},
      documentationURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      documentationURL2: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      startDate: {},
      endDate: {},
      steps: {},
      owner: {},
      landscapes: {},
      dataFlows: {},
    };
    const v$ = useVuelidate(validationRules, functionalFlow as any);
    v$.value.$validate();

    return {
      functionalFlowService,
      alertService,
      functionalFlow,
      previousState,
      isSaving,
      currentLanguage,
      owners,
      v$,
      tabChanged,
      tabIndex,
      textareaNbLine,
      plantuml,
      focusout,
      focus,
      chooseItem,
      moveDown,
      moveUp,
      searchMatch,
      selectedIndex,
      selectItem,
      highlightWord,
      getPlantUMLImageFromString,
      plantumlModified,
      plantUMLImage,
      previewError,
      functionalFlowImport,
      changeInterface,
      creation,
      selectedLandscape,
      allLandscapes,
      landscapeGivenInParameter,
      save,
      aliasesValid,
    };
  },
});
