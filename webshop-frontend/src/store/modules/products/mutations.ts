export default {
  saveProductForm(state: any, payload: object) {
    state.productForm = payload;
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
