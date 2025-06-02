export default {
  addProductToCart(state: any, payload: object) {
    state.cart.push(payload);
  },
  emptyCart(state: any) {
    state.cart = [];
  },
  saveCart(state: any, payload: object) {
    state.cart = payload;
  },
}