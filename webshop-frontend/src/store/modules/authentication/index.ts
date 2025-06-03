import actions from './actions.ts';
import mutations from './mutations.ts';
import getters from './getters.ts';


export interface AuthUserDTO {
    username: string
    passowrd: string
}
export interface UserRight {
    token: string
    userRight: string
}
export default {
    namespaced: true,
    state() {
        return {
            auth: {
                token: null,
                userRight: "",
            }
        }
    },
    actions,
    mutations,
    getters
}