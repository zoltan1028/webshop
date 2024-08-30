export default {
    getProductsIsEmpty(state: any) {
        return state.isProductsEmpty;
    },
    getAuth(state: any) {
        return state.auth;
    },
    getProductForm(state: any) {
        return state.productForm;
    },
    getProducts(state: any) {
        return state.products;
    },
    getCartContent(state: any) {
        return state.cart;
    }
};