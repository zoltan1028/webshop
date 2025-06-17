import { CartItem } from "./index"

export default {
    getCartContent(state: any): CartItem[]  {
            return state.cart;
        },
    getOrders(state: any) : any {
        return state.orders
    }
};