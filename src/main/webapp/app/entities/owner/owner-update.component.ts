import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';
import { email } from '@vuelidate/validators';

import OwnerService from './owner.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import { type IOwner, Owner } from '@/shared/model/owner.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'OwnerUpdate',
  setup() {
    const ownerService = inject('ownerService', () => new OwnerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const owner: Ref<IOwner> = ref(new Owner());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveOwner = async ownerId => {
      try {
        const res = await ownerService().find(ownerId);
        owner.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.ownerId) {
      retrieveOwner(route.params.ownerId);
    }

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      firstname: {},
      lastname: {},
      email: {
        email,
      },
      users: {},
    };
    const v$ = useVuelidate(validationRules, owner as any);
    v$.value.$validate();

    return {
      ownerService,
      alertService,
      owner,
      previousState,
      isSaving,
      currentLanguage,
      users,
      v$,
    };
  },
  created(): void {
    this.owner.users = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.owner.id) {
        this.ownerService()
          .update(this.owner)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Owner is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      } else {
        this.ownerService()
          .create(this.owner)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Owner is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      }
    },

    getSelected(selectedVals, option): any {
      if (selectedVals) {
        return selectedVals.find(value => option.id === value.id) ?? option;
      }
      return option;
    },
  },
});
