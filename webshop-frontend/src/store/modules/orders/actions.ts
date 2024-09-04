const ordersController = "http://localhost:8081/api/orders/";
export default {
    async submitOrder(ctx: any, payload: any) {
        console.log("payload:")
        console.log(payload)
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
}