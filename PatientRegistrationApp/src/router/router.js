import VueRouter from "vue-router";
import PatientInfoForm from "@/pages/patient/PatientInfoForm";
import Questionnaire from "@/pages/questionnaire/Questionnaire";
import Review from "@/pages/review/Review";
import FollowUp from "@/pages/followup/FollowUp";

let router = new VueRouter({
  mode: "history",
  routes: [
    {
      path: "/",
      redirect: "/patient-info"
    },
    {
      path: "/patient-info",
      component: PatientInfoForm,
    },
    {
      path: "/questions",
      component: Questionnaire,
    },
    {
      path: "/review",
      component: Review,
    },
    {
      path: "/followup",
      component: FollowUp,
    }
  ],
});

export default router;
