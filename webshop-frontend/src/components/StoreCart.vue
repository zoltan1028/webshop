<template>
    <div>
        <p>Cart</p>
        <cart-item @onQtyChanged="qtyChanged" v-for="item in getCartContent" :key="item.id" :id="item.id"
            :name="item.name" :pieces="item.pieces"></cart-item>
        <base-button v-if="false" class="button-color-primary" @onClick="submitCart">Send Order</base-button>
        <base-button class="button-color-delete" @onClick="emptyCart">Empty Cart</base-button>
        <google-pay v-if="isUserLoggedIn"></google-pay>
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
        }
    },
    computed: {
        isUserLoggedIn() {
            const auth = this.$store.getters['products/getAuth'];
            return auth ? auth.token : auth;
        },
        getCartContent() {
            return this.$store.getters['products/getCartContent']
        }
    },
    methods: {
        submitCart() {
            console.log("order sent")
            this.$store.dispatch('products/submitOrder', {
                "cart": this.$store.getters['products/getCartContent'],
                "token": this.$store.getters['products/getAuth'].token
            })
        },
        emptyCart() {
            this.$store.dispatch('products/emptyCart')
        },
        qtyChanged(data) {
            let cartContent = this.$store.getters['products/getCartContent']
            console.log(cartContent)
            cartContent.forEach(product => {
                if ((product.id === data[0]) && (data[1] === '+')) {
                    product.pieces++;
                } else if ((product.id === data[0]) && (product.pieces > 1)) {
                    product.pieces--;
                }
            });
        }
    }
}
</script>
<style scoped></style>