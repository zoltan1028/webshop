export default {
    getToken(state: any) {
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