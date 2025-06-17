import { CartItem } from './index'
export default {
  addProductToCart(state: any, payload: CartItem) {
    state.cart.push(payload);
  },
  emptyCart(state: any) {
    state.cart = [];
  },
  saveCart(state: any, payload: CartItem[]) {
    state.cart = payload;
  },
  setOrders(state: any, payload: any) {
    state.orders = payload
  }
}