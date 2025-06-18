const ordersController = "http://localhost:8081/api/orders/";
import { CartItemDTO } from "./index";
import { OrderDTO } from "./index";
import { CartItem } from "./index";
import { OrderDeleteDTO } from "./index";

export default {
  async submitOrder(ctx: any, payload: OrderDTO) {
    const response = await fetch(ordersController + "submitOrder", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Token: payload.token,
      },
      body: JSON.stringify(payload.data),
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
  },
  async modifyOrder(ctx: any, payload: OrderDTO) {
    const response = await fetch(ordersController + "modifyOrder", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Token: payload.token,
      },
      body: JSON.stringify(payload.data),
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
  },
  async deleteOrder(ctx: any, payload: OrderDeleteDTO) {
    const response = await fetch(ordersController + "deleteOrder/" + payload.orderId, {
      method: "DELETE",
      headers: {
        Token: payload.token,
      },
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
  },
  async getOrdersOfUser(ctx: any, token: string) {
    const response = await fetch(ordersController + "getOrdersOfUser", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        token: token,
      },
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
    const responseData = await response.json();
    ctx.commit("setOrders", responseData);
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
};
