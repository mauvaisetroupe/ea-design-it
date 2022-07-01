import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowImport } from '@/shared/model/flow-import.model';
import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';

import FlowImportService from './flow-import.service';
import AlertService from '@/shared/alert/alert.service';
import LandscapeViewService from '../landscape-view/landscape-view.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FlowImport extends Vue {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('flowImportService') private flowImportService: () => FlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  //public flowImports: IFlowImport[] = [];
  public excelFiles: File[] = null;
  public isFetching = false;
  public fileSubmited = false;
  public rowsLoaded = false;
  public excelFileNames = 'Browse File';
  public dtos = null;
  public notFilteredDtos = null;
  public existingLandscapes: ILandscapeView[] = null;
  public potentialLandscape: String[] = [];
  public selectedLandscape: String[] = [];

  public mounted(): void {
    this.retrieveAllLandscapeViews();
  }

  public retrieveAllLandscapeViews(): void {
    this.isFetching = true;
    this.landscapeViewService()
      .retrieve()
      .then(
        res => {
          this.existingLandscapes = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleFileUpload(event): void {
    console.log(event);
    this.selectedLandscape = [];
    this.excelFiles = Array.from(event.target.files);
    this.excelFileNames = this.excelFiles.map(f => f.name).join(', ');
    this.potentialLandscape = this.excelFiles.map(f => f.name.replace(/_/g, ' ').replace(/\..*/, ''));

    this.excelFiles.forEach((excel, i) => {
      this.existingLandscapes.forEach(landscape => {
        if (landscape.diagramName === this.potentialLandscape[i]) {
          this.selectedLandscape[i] = landscape.diagramName;
        }
      });
    });
  }

  public submitFile(): void {
    this.notFilteredDtos = [];
    this.dtos = [];
    this.isFetching = true;
    this.fileSubmited = true;
    let excelFile = this.excelFiles.shift();
    this.uploadOneFile(excelFile);
  }

  public uploadOneFile(excelFile) {
    this.flowImportService()
      .uploadFile([excelFile])
      .then(
        res => {
          this.dtos.push(...res.data);
          this.notFilteredDtos.push(...res.data);
          this.rowsLoaded = true;
          if (this.excelFiles.length > 0) {
            this.uploadOneFile(this.excelFiles.shift());
          } else {
            this.isFetching = false;
          }
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public filterErrors() {
    this.dtos = [];
    this.notFilteredDtos.forEach(dto => {
      let newdto = {};
      const newimport = [];
      dto.flowImports.forEach(elem => {
        const flowImport = elem as IFlowImport;
        if (
          flowImport.importFunctionalFlowStatus === 'ERROR' ||
          flowImport.importInterfaceStatus === 'ERROR' ||
          flowImport.importDataFlowStatus === 'ERROR'
        ) {
          newimport.push(flowImport);
        }
      });
      newdto = {
        excelFileName: dto.excelFileName,
        flowImports: newimport,
      };
      this.dtos.push(newdto);
    });
  }

  public getErrors() {
    const errors = [];
    this.notFilteredDtos.forEach(dto => {
      let line = 1;
      dto.flowImports.forEach(elem => {
        const flowImport = elem;
        if (
          flowImport.importFunctionalFlowStatus === 'ERROR' ||
          flowImport.importInterfaceStatus === 'ERROR' ||
          flowImport.importDataFlowStatus === 'ERROR'
        ) {
          flowImport.id = dto.excelFileName + '_' + line.toString();
          const errorRow = {
            ...flowImport,
          };
          errors.push(errorRow);
          console.log(errorRow);
        }
        line = line + 1;
      });
    });
    return errors;
  }

  public exportErrors() {
    const errors = this.getErrors();
    let csvContent = 'data:text/csv;charset=utf-8,';
    csvContent += [Object.keys(errors[0]).join(';'), ...errors.map(row => Object.values(row).join(';').replace(/\n/gm, ''))]
      .join('\n')
      .replace(/(^\[)|(\]$)/gm, '');
    const data = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', data);
    link.setAttribute('download', 'export.csv');
    link.click();
  }
}
