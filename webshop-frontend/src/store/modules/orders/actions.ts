const ordersController = "http://localhost:8081/api/orders/";
import { CartItemDTO } from './index'
import { OrderDTO } from './index'
import { CartItem } from './index'


export default {
    async submitOrder(ctx: any, payload: OrderDTO) {
        const response = await fetch(ordersController + "submitOrder", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Token": payload.token
          },
          body: JSON.stringify(payload.data),
        });
        if (!response.ok) {
          const error = new Error(response.statusText);
          console.log(error)
          throw error;
        }
      },
  addProductToCart(ctx: any, payload: CartItemDTO) {
    ctx.commit("addProductToCart", payload);
  },
  emptyCart(ctx: any) {
    ctx.commit("emptyCart");
  },
  saveCart(ctx: any, payload: CartItem[]) {
    ctx.commit("saveCart", payload);
  },
}