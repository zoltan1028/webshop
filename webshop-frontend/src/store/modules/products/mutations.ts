import { Product } from './index'
import { PageableProductsDTO } from './index'

export default {
  saveProductForm(state: any, payload: Product) {
    state.productForm = payload;
  },
  async setProducts(state: any, payload: PageableProductsDTO) {
    if (payload.content.length === 0) {
        state.isProductsEmpty = true;
    } else {
        state.isProductsEmpty = false;
        state.products = payload;
    }
  },
};
