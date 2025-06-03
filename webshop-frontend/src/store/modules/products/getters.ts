import { PageableProductsDTO } from './index'
import { Product } from './index'

export default {
    getProductsIsEmpty(state: any): boolean {
        return state.isProductsEmpty;
    },
    getProductForm(state: any): Product {
        return state.productForm;
    },
    getProducts(state: any): PageableProductsDTO  {
        return state.products;
    },
};