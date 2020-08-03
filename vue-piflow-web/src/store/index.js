import Vue from 'vue'
import Vuex from 'vuex'
import variable from '../modules/variable'

Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    variable,
  }
});
export default store;
