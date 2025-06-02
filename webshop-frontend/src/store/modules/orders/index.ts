import actions from './actions.ts';
import mutations from './mutations.ts';
import getters from './getters.ts';

export default {
    namespaced: true,
    state() {
        return {
            cart: []
        }
    },
    actions,
    mutations,
    getters
}