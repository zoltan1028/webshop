const productsController = "http://localhost:8081/api/products/";
export default {
  saveProductForm(ctx: any, payload: object) {
    ctx.commit("saveProductForm", payload);
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
