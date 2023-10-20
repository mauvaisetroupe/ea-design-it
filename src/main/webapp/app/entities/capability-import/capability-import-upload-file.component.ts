import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import CapabilityImportService from './capability-import.service';
import AlertService from '@/shared/alert/alert.service';
import { ICapabilityImport } from '@/shared/model/capability-import.model';
import { Action, ICapabilityActionDTO, ICapabilityImportAnalysisDTO } from '@/shared/model/capability-import-analysis.model';
import { ICapability } from '@/shared/model/capability.model';
import CapabilityTreeComponent from '@/entities/capability/component/capability-tree.vue';
 

@Component({
  mixins: [Vue2Filters.mixin],
  components: {
    CapabilityTreeComponent,
  },
})

export default class CapabilityImport extends Vue {
  @Inject('capabilityImportService') private capabilityImportService: () => CapabilityImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public capabilitiesImports: ICapabilityImport[] = [];
  public filteredCapabilitiesImports: ICapabilityImport[] = [];
  public excelFile: File = null;
  public isFetching = false;
  public fileSubmited = false;
  public rowsLoaded = false;
  public excelFileName = 'Browse File';
  public toAddOption = [{text: 'Import', value: Action.ADD}, {text: 'Ignore', value: Action.IGNORE}];
  public toDeleteOption = [{text: 'Delete', value: Action.DELETE}, {text: 'Ignore', value: Action.IGNORE}];
  public toDeleteWithMappingOption = [{text: 'Force Delete', value: Action.FORCE_DELETE}, {text: 'Ignore', value: Action.IGNORE}];
  public toDeleteWithChildMappingOption = [{text: 'Ignore', value: Action.IGNORE}];
  public analysisDone = false;
  public IGNORE = Action.IGNORE;
  public DELETE = Action.DELETE;
  public FORCE_DELETE = Action.FORCE_DELETE;
  public ADD = Action.ADD;

  public capabilitiesImportAnalysis: ICapabilityImportAnalysisDTO = {};

  public get somethingToImport() {
    return this.analysisDone && (
        this.capabilitiesImportAnalysis?.capabilitiesToAdd?.length >0 ||
        this.capabilitiesImportAnalysis?.capabilitiesToDelete?.length >0 ||
        this.capabilitiesImportAnalysis?.capabilitiesToDeleteWithMappings?.length >0 ||
        this.capabilitiesImportAnalysis?.ancestorsOfCapabilitiesWithMappings?.length >0
    );
  }

  public handleFileUpload(event): void {
    this.excelFile = event.target.files[0];
    this.excelFileName = this.excelFile.name;
  }

  public submitFileForAnalysis(): void {
    this.isFetching = true;
    this.fileSubmited = true;
    this.capabilitiesImports = [];
    this.filteredCapabilitiesImports = [];
    this.capabilityImportService()
      .uploadFileToAnalysis(this.excelFile)
      .then(
        res => {
          this.capabilitiesImportAnalysis = res;
          this.isFetching = false;
          this.analysisDone = true;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public confirmUploadedFile(): void {
    this.isFetching = true;
    this.fileSubmited = true;
    this.capabilitiesImports = [];
    this.filteredCapabilitiesImports = [];
    this.capabilityImportService()
      .confirmUploadedFile(this.capabilitiesImportAnalysis)
      .then(
        res => {
          this.analysisDone = false;
          this.capabilitiesImports = res.data;
          this.filteredCapabilitiesImports = res.data;
          this.isFetching = false;
          this.rowsLoaded = true;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }  

  // Error Handling

  public filterErrors() {
    this.filteredCapabilitiesImports = this.filteredCapabilitiesImports.filter(c => c.status === 'ERROR');
  }


  public toggleAll(capabilityActionss: ICapabilityActionDTO[], action: Action) {
    capabilityActionss.forEach(capaAction => {
        capaAction.action=action;
    });
  }
}
