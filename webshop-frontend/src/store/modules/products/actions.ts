const productsController = "http://localhost:8081/api/products/";
import { Product } from './index'
import { ProductFormDTO } from './index'

export default {
  saveProductForm(ctx: any, payload: Product) {
    ctx.commit("saveProductForm", payload);
  },
  async getProducts(ctx: any, param: Record<string, string>) {
    const params = new URLSearchParams(param);
    const paramsString = params.toString();
    const url = new URL(productsController + "getProducts?" + paramsString);
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
  async postProduct(ctx: any, payload: ProductFormDTO) {
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
