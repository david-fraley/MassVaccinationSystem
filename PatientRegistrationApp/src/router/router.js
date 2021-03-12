import VueRouter from "vue-router";
import SinglePatientFlow from "@/pages/single/SinglePatientFlow";
import HouseholdFlow from "@/pages/household/HouseholdFlow";

let router = new VueRouter({
  mode: "history",
  routes: [
    {
      path: "/single-patient",
      component: SinglePatientFlow,
    },
    {
      path: "/household",
      component: HouseholdFlow,
    },
  ],
});

export default router;
