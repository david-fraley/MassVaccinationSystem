export default {
  state: {
    answers: {
      screeningQ1: "",
      screeningQ2: "",
      screeningQ2b: "",
      screeningQ3a: "",
      screeningQ3b: "",
      screeningQ3c: "",
      screeningQ4: "",
      screeningQ5: "",
      screeningQ6: "",
      screeningQ7: "",
      screeningQ8: "",
    }
  },
  getters: {},
  mutations: {
    setQuestionAnswer(state, { qname, answer }) {
      state.answers[qname] = answer
    }
  },
  actions: {},
}