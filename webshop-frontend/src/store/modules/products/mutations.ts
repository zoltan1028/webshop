export default {
  saveProductForm(state: any, payload: object) {
    state.productForm = payload;
  },
  addProductToCart(state: any, payload: object) {
    state.cart.push(payload);
  },
  emptyCart(state: any) {
    state.cart = [];
  },
  saveCart(state: any, payload: object) {
    state.cart = payload;
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
