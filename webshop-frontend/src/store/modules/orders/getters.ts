import { CartItem } from "./index"

export default {
    getCartContent(state: any): CartItem[]  {
            return state.cart;
        }
};