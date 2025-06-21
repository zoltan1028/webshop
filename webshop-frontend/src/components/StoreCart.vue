<template>
    <div>
        <p>Cart</p>
        <cart-item @onQtyChanged="qtyChanged" v-for="item in getCartContent" :key="item.id" :id="item.id"
            :name="item.name" :pieces="item.pieces"></cart-item>
        <base-button class="button-color-delete" @onClick="emptyCart">Empty Cart</base-button>
        <div v-if="isUserLoggedIn && getOrdersOfUser.length != 0">
            <div v-for="order in getOrdersOfUser" :key="order.id">{{ order.id }} {{ order.orderReceivedAt }}</div>
            <legend class="font-semibold mb-2">Choose your order:</legend>
            <label v-for="id in getToppings" :key="id" class="block mb-1">
                <input type="radio" name="topping" :value="id" :checked="existingOrderId === id"
                    @click="radioOnClick(id)" readonly />
                {{ id }}
            </label>
        </div>
        <google-pay v-if="isUserLoggedIn && getCartContent != undefined && getCartContent.length != 0" @payment-failed="onPaymentFailed"
            @successful-payment="submitCart" :paymentRequest="paymentRequestProp"></google-pay>
    </div>
</template>
<script>
import CartItem from './StoreCartItem.vue'
import GooglePay from './GooglePlayButton.vue'
export default {
    components: { CartItem, GooglePay },
    data() {
        return {
            existingOrderId: null,
            toppings: [],
            paymentRequestProp: {
                apiVersion: 2,
                apiVersionMinor: 0,
                allowedPaymentMethods: [
                    {
                        type: 'CARD',
                        parameters: {
                            allowedAuthMethods: ['PAN_ONLY', 'CRYPTOGRAM_3DS'],
                            allowedCardNetworks: ['MASTERCARD', 'VISA'],
                        },
                        tokenizationSpecification: {
                            type: 'PAYMENT_GATEWAY',
                            parameters: {
                                gateway: 'example',
                                gatewayMerchantId: 'exampleGatewayMerchantId',
                            },
                        },
                    },
                ],
                merchantInfo: {
                    merchantId: '12345678901234567890',
                    merchantName: 'Demo Merchant',
                },
                transactionInfo: {
                    totalPriceStatus: 'FINAL',
                    totalPriceLabel: 'Total',
                    totalPrice: '100',
                    currencyCode: 'HUF',
                    countryCode: 'HU',
                },
                callbackIntents: ['PAYMENT_AUTHORIZATION'],
            }
        }
    },
    computed: {
        isUserLoggedIn() {
            const auth = this.$store.getters['authentication/getAuth'];
            return auth ? auth.token : auth;
        },
        getCartContent() {
            return this.$store.getters['orders/getCartContent']
        },
        getOrdersOfUser() {
            return this.$store.getters['orders/getOrdersOfUser'].shopOrderList
        },
        getToppings() {
            const userOrders = this.$store.getters['orders/getOrdersOfUser'].shopOrderList;
            //undefined until promise is fulfilled even if store variable is initialized as object 
            console.log(userOrders)
            return userOrders ? userOrders.map(order => order.id) : undefined;
        }
    },
    created() {
        const token = this.$store.getters['authentication/getAuth'].token

        if (token) {
            try {
                this.$store.dispatch('orders/getOrdersOfUser', token);
            } catch (e) {
                this.error = e
            }
        }
        let cartContent = this.$store.getters['orders/getCartContent']
        let total = 0;
        cartContent.forEach(product => {
            total += Number(product.price) * Number(product.pieces);
        });
        this.paymentRequestProp.transactionInfo.totalPrice = String(total);
    },
    methods: {
        radioOnClick(id) {
            this.existingOrderId = this.existingOrderId === id ? null : id;
        },
        async submitCart(event) {
            this.$store.getters['orders/getCartContent'].forEach(item => {
                delete item.price
                delete item.name
            })
            this.submitOrder()
            this.$store.dispatch('orders/emptyCart')
            this.$router.push('/storecart/ordercomplete')

        },
        async submitOrder() {
            try {
                if (this.existingOrderId === null) {
                    await this.$store.dispatch('orders/submitOrder', {
                        "data": {
                            "cart": this.$store.getters['orders/getCartContent'],
                            "google_tokenData": event.detail
                        },
                        "token": this.$store.getters['authentication/getAuth'].token,
                    })
                } else {
                    await this.$store.dispatch('orders/modifyOrder', {
                        "data": {
                            "idOfSelectedOrder": this.existingOrderId,
                            "cart": this.$store.getters['orders/getCartContent'],
                            "google_tokenData": event.detail
                        },
                        "token": this.$store.getters['authentication/getAuth'].token,
                    })
                }
            } catch (e) {
                console.log(e)
            }
        },
        onPaymentFailed(event) {
            this.$router.push('/storehome')
        },
        emptyCart() {
            console.log(this.selectedTopping)
            this.$store.dispatch('orders/emptyCart')
        },
        qtyChanged(data) {
            let cartContent = this.$store.getters['orders/getCartContent']
            let total = 0;
            cartContent.forEach(product => {
                if ((product.id === data[0]) && (data[1] === '+')) {
                    //changing value of state variable
                    product.pieces++;
                } else if ((product.id === data[0]) && (product.pieces > 1)) {
                    product.pieces--;
                } else if ((product.id === data[0]) && (product.pieces <= 1)) {
                    let filtered = cartContent.filter(item => item.id !== product.id)
                    //saving filtered cart to state variable
                    this.$store.dispatch('orders/saveCart', filtered);
                }
                total += Number(product.pieces) * Number(product.price);
            });
            this.paymentRequestProp.transactionInfo.totalPrice = String(total);
        }
    }
}
</script>
<style scoped></style>