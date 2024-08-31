const baseUrl = "http://localhost:8081/api/";
const productsController = "products/";
const ordersController = "orders/";
const shopUserController = "users/";

export default {
  saveProductForm(ctx: any, payload: object) {
    ctx.commit("saveProductForm", payload);
  },
  addProductToCart(ctx: any, payload: object) {
    ctx.commit("addProductToCart", payload);
  },
  emptyCart(ctx: any) {
    ctx.commit("emptyCart");
  },
  async register(ctx: any, payload: object) {
    const response = await fetch(baseUrl + shopUserController + "register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });
    if (!response.ok) {
      const errorMessage = await response.text();
      const error = new Error(errorMessage);
      throw error;
    }
  },
  async authLogin(ctx: any, payload: object) {
    const response = await fetch(baseUrl + shopUserController + "login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });
    if (!response.ok) {
      const errorMessage = await response.text();
      const error = new Error(errorMessage);
      throw error;
    }
    const responseData = await response.json();
    ctx.commit("setToken", responseData);
  },
  async authLogout(ctx: any, payload: string) {
    const response = await fetch(baseUrl + shopUserController + "logout", {
      method: "POST",
      headers: {
        "Content-Type": "text/plain",
      },
      body: payload,
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
    const responseData = await response.text();
    ctx.commit("removeToken");
  },
  removeToken(ctx: any) {
    ctx.commit("removeToken")
  },
  async submitOrder(ctx: any, payload: any) {
    console.log("payload:")
    console.log(payload)
    const response = await fetch(baseUrl + ordersController + "submitOrder", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Token": payload.token
      },
      body: JSON.stringify(payload.data),
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
  },
  async getProducts(ctx: any, param: string) {
    const params = {
      page: param,
      size: '5'
    }
    const url = new URL(baseUrl + productsController + "getProducts");
    url.search = new URLSearchParams(params).toString()

    const response = await fetch(url, {
      method: "GET",
    });
    console.log(response);
    if (!response.ok) {
      const error = new Error(
        response.statusText + "An error occurd fetching prods"
      );
      throw error;
    }
    const responseData = await response.json();
    ctx.commit("setProducts", responseData);
  },
  async postProduct(ctx: any, payload: any) {
    console.log("posting");
    console.log(payload);
    const response = await fetch(baseUrl + productsController + "newProduct", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "token": payload.token
      },
      body: JSON.stringify(payload.form),
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
  },
  async getTokenStatus(ctx: any, token: string) {
    const response = await fetch(baseUrl + shopUserController + "getTokenStatus", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "token": token
      },
    });
    if (!response.ok) {
      const error = new Error(
        response.statusText + "An error occurd while checking token status."
      );
      throw error;
    }
    const responseData = await response.text();
    ctx.commit("setTokenValidity", responseData);
  },
};
