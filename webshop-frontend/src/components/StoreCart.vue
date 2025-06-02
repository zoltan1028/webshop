<template>
    <div>
        <p>Cart</p>
        <cart-item @onQtyChanged="qtyChanged" v-for="item in getCartContent" :key="item.id" :id="item.id"
            :name="item.name" :pieces="item.pieces"></cart-item>
        <base-button v-if="false" class="button-color-primary" @onClick="submitCart">Send Order</base-button>
        <base-button class="button-color-delete" @onClick="emptyCart">Empty Cart</base-button>
        <google-pay @payment-failed="onPaymentFailed" @successful-payment="submitCart"
            :paymentRequest="paymentRequestProp" v-if="isUserLoggedIn"></google-pay>
    </div>
</template>
<script>
import CartItem from './StoreCartItem.vue'
import GooglePay from './GooglePlayButton.vue'
export default {
    components: { CartItem, GooglePay },
    data() {
        return {
            localCartContent: null,
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
        }
    },
    created() {
        let cartContent = this.$store.getters['orders/getCartContent']
        let total = 0;
        cartContent.forEach(product => {
            total += Number(product.price) * Number(product.pieces);
        });
        this.paymentRequestProp.transactionInfo.totalPrice = String(total);
    },
    methods: {
        async submitCart(event) {
            this.$store.getters['orders/getCartContent'].forEach(item => {
                delete item.price
                delete item.name
            })

            try {
                await this.$store.dispatch('orders/submitOrder', {
                    "data": {
                        "cart": this.$store.getters['orders/getCartContent'],
                        "google_tokenData": event.detail
                    },
                    "token": this.$store.getters['authentication/getAuth'].token,
                })
            } catch (e) {
                console.log(e)
            }
            this.$router.push('/storecart/ordercomplete')
        },
        onPaymentFailed(event) {
            this.$router.push('/storehome')
        },
        emptyCart() {
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