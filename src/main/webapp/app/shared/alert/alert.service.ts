import type { BvToast } from 'bootstrap-vue';
import { getCurrentInstance } from 'vue';

export const useAlertService = () => {
  const bvToast = getCurrentInstance().root.proxy['_bv__toast'];
  if (!bvToast) {
    throw new Error('BootstrapVue toast component was not found');
  }
  return new AlertService({
    bvToast,
  });
};

export default class AlertService {
  private bvToast: BvToast;

  constructor({ bvToast }: { bvToast: BvToast }) {
    this.bvToast = bvToast;
  }

  public showInfo(toastMessage: string, toastOptions?: any) {
    this.bvToast.toast(toastMessage, {
      toaster: 'b-toaster-top-center',
      title: 'Info',
      variant: 'info',
      solid: true,
      autoHideDelay: 5000,
      ...toastOptions,
    });
  }

  public showSuccess(toastMessage: string) {
    this.bvToast.toast(toastMessage, {
      toaster: 'b-toaster-top-center',
      title: 'Success',
      variant: 'success',
      solid: true,
      autoHideDelay: 5000,
    });
  }

  public showError(toastMessage: string) {
    this.bvToast.toast(toastMessage, {
      toaster: 'b-toaster-top-center',
      title: 'Error',
      variant: 'danger',
      solid: true,
      autoHideDelay: 5000,
    });
  }

  public showHttpError(httpErrorResponse: any) {
    let errorMessage: string | null = null;
    switch (httpErrorResponse.status) {
      case 0:
        errorMessage = 'Server not reachable';
        break;

      case 400: {
        const arr = Object.keys(httpErrorResponse.headers);
        for (const entry of arr) {
          if (entry.toLowerCase().endsWith('app-error')) {
            errorMessage = httpErrorResponse.headers[entry];
          }
        }
        if (!errorMessage && httpErrorResponse.data?.fieldErrors) {
          errorMessage = 'Validation error';
        } else if (!errorMessage) {
          errorMessage = httpErrorResponse.data.message;
        }
        break;
      }

      case 404:
        errorMessage = 'The page does not exist.';
        break;

      default:
        errorMessage = httpErrorResponse.data.message;
    }
    this.showError(errorMessage);
  }
}
