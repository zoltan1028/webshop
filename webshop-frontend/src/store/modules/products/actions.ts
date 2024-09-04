const productsController = "http://localhost:8081/api/products/";
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
  saveCart(ctx: any, payload: object) {
    ctx.commit("saveCart", payload);
  },
  async getProducts(ctx: any, param: string) {
    const params = {
      page: param,
      size: "5",
    };
    const url = new URL(productsController + "getProducts");
    url.search = new URLSearchParams(params).toString();

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
    const response = await fetch(productsController + "newProduct", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        token: payload.token,
      },
      body: JSON.stringify(payload.form),
    });
    if (!response.ok) {
      const error = new Error(response.statusText);
      throw error;
    }
  },
};
