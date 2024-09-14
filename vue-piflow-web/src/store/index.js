import Vue from 'vue'
import Vuex from 'vuex'
import variable from '../modules/variable'
import graphConf from './graphConf'

Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    variable,
    graphConf
  }
});
export default store;
