import actions from './actions.ts';
import mutations from './mutations.ts';
import getters from './getters.ts';
export interface CartItem {
    id: number,
    pieces: number,
    name: string,
    price: number
}
export interface CartItemDTO {
    id: number,
    pieces: number
}
export interface OrderDTO {
    token: string,
    data: {
        cart: CartItemDTO[]
        google_tokenData: object
    }
}
export interface OrderDeleteDTO {
    token: string
    orderId: number
}
export default {
    namespaced: true,
    state() {
        return {
            cart: [],
            orders: {}
        }
    },
    actions,
    mutations,
    getters
}