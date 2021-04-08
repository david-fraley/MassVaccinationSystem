import PatientLookupPage from '@/components/PatientLookupPage.vue';
import PatientCheckInPage from '@/components/PatientCheckInPage.vue';
import ConsentScreeningPage from '@/components/ConsentScreeningPage.vue';
import VaccinationEventPage from '@/components/VaccinationEventPage.vue';
import AdverseEventsPage from '@/components/AdverseEventsPage.vue';
import DischargePage from '@/components/DischargePage.vue';
import CreateLocationResourcePage from '@/components/CreateLocationResourcePage.vue';
import ApplicationInfoPage from '@/components/ApplicationInfoPage.vue';

export default [
  // Redirects to /route-one as the default route.
  {
    path: '/',
    redirect: '/RetrievePatientRecord',
  },
  {
    path: '/RetrievePatientRecord',
    component: PatientLookupPage,
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/CheckIn',
    component: PatientCheckInPage,
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/ConsentScreening',
    component: ConsentScreeningPage,
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/VaccinationEvent',
    component: VaccinationEventPage,
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/AdverseReaction',
    component: AdverseEventsPage,
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/Discharge',
    component: DischargePage,
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/Configuration',
    component: CreateLocationResourcePage,
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/Info',
    component: ApplicationInfoPage,
    meta: {
      requiresAuth: true,
    }
  }
];