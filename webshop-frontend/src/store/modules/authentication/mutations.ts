export default {
    setToken(state: any, auth: object) {
        state.auth = auth;
        console.log("logtoken" + auth);
        console.log(auth);
      },
      removeToken(state: any) {
        state.auth.token = null;
        state.auth.userRight = null;
        
      },
      setTokenValidity(state: any, validity: string) {
        if (validity === "tokenIsInvalid") {
          console.log("invalid")
          state.auth.token = null;
          state.auth.userRight = null;
        }
      },
}