import Cookies from 'js-cookie';
const state = {
  positionIsShow:false,
  token: Cookies.get('token'),
  user:{}
}
const mutations = {
  setUser(state, data) {
    state.user = data;
  },
  setToken(state, data) {
    state.token = data;
  },
  setPositionIsShow(state, data) {
    state.positionIsShow = data;
  }
}
const actions = {
  setArtile(context, data) {
    context.commit('setArtile', data);
  }
}
const getters = {
  //获文章取条获件分页列表
  getArticleReplyPage(state) {
    return function (pageNo, pageSize) {
      let list = 0;
      return list;
    }
  }
}
export default {
  state,
  actions,
  mutations,
  getters
}
