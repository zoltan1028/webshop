import { UserRight } from './index'

export default {
    setToken(state: any, auth: UserRight) {
        state.auth = auth;
      },
      removeToken(state: any) {
        state.auth.token = null;
        state.auth.userRight = null;
      },
      setTokenValidity(state: any, isTokenValid: string) {
        if (isTokenValid === "tokenIsInvalid") {
          state.auth.token = null;
          state.auth.userRight = "";
        }
      },
}