const shopUserController = "http://localhost:8081/api/users/";

export default {
  async authLogin(ctx: any, payload: object) {
    const response = await fetch(shopUserController + "login", {
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
    const response = await fetch(shopUserController + "logout", {
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
  async register(ctx: any, payload: object) {
    const response = await fetch(shopUserController + "register", {
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
  removeToken(ctx: any) {
    ctx.commit("removeToken");
  },
  async getTokenStatus(ctx: any, token: string) {
    const response = await fetch(
      shopUserController + "getTokenStatus",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          token: token,
        },
      }
    );
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
