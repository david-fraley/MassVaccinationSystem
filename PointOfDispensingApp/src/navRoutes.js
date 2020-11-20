import UserLoginPage from './components/UserLoginPage';
import PatientLookupPage from './components/PatientLookupPage.vue';
import PatientCheckInPage from './components/PatientCheckInPage.vue';
import PatientHistoryPage from './components/PatientHistoryPage.vue';
import VaccinationEventPage from './components/VaccinationEventPage.vue';
import AdverseEventsPage from './components/AdverseEventsPage.vue';
import DischargePage from './components/DischargePage.vue';

export default [
  // Redirects to /route-one as the default route.
  {
    path: '/',
    redirect: '/UserLogin'
  },
  {
    path: '/UserLogin',
    component: UserLoginPage,
  },
  {
    path: '/RetrievePatientRecord',
    component: PatientLookupPage,
  },
  {
    path: '/CheckIn',
    component: PatientCheckInPage,
  },
  {
    path: '/PatientHistory',
    component: PatientHistoryPage,
  },
  {
    path: '/VaccinationEvent',
    component: VaccinationEventPage,
  },
  {
    path: '/AdverseReaction',
    component: AdverseEventsPage,
  }
  ,
  {
    path: '/Discharge',
    component: DischargePage,
  }
];