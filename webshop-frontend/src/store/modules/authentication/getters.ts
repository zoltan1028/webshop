import { UserRight } from './index'

export default {
    getAuth(state: any): UserRight {
        return state.auth;
    },
};