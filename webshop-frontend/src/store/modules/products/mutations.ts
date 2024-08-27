export default {
  saveProductForm(state: any, payload: object) {
    state.productForm = payload;
  },
  addProductToCart(state: any, payload: object) {
    console.log("push to cart");
    state.cart.push(payload);
  },
  emptyCart(state: any) {
    state.cart = [];
  },
  setToken(state: any, auth: string) {
    state.auth = auth;
    console.log("logtoken" + auth);
  },
  removeToken(state: any, token: string) {
    state.auth.token = null;
    state.auth.userRight = null;
    console.log("logtoken" + token);
  },
  setTokenValidity(state: any, validity: string) {
    if (validity === "tokenIsInvalid") {
      state.auth.token = null;
      state.auth.userRight = null;
    }
  },
  async setProducts(state: any, payload: object) {
    state.products = payload;
  },
};
