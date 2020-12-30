export default {
    vaccinationDecisionState:
    {
        UNDETERMINED: 0,
        VACCINATION_PROCEED_YES: 1,
        VACCINATION_PROCEED_NO: 2   
    },	

    patientWorkflowState:
    {
        INITIAL: 0,
        RECORD_RETRIEVED: 1,
        ADMITTED: 2,
        VACCINATION_COMPLETE: 3,
        VACCINATION_CANCELED: 4,
        DISCHARGED: 5,
        DEMO: 6
    }
}