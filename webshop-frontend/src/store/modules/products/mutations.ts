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
  async setProducts(state: any, payload: any) {
    if (payload.content.length === 0) {
        state.isProductsEmpty = true;
    } else {
        state.isProductsEmpty = false;
        state.products = payload;
    }
  },
};
