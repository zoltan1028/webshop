<template>
    <p>Cart</p>
    <cart-item @onQtyChanged="qtyChanged" v-for="item in getCartContent" :key="item.id" :id="item.id" :name="item.name"
        :pieces="item.pieces"></cart-item>
    <base-button class="button-color-primary" @onClick="submitCart">Send Order</base-button>
    <base-button class="button-color-delete" @onClick="emptyCart">Empty Cart</base-button>
</template>
<script>
import CartItem from './StoreCartItem.vue'
export default {
    components: { CartItem },
    data() {
        return {
            localCartContent: null,
        }
    },
    computed: {
        getCartContent() {
            return this.$store.getters['products/getCartContent']
        }
    },
    methods: {
        submitCart() {
            console.log("order sent")
            this.$store.dispatch('products/submitOrder', {
                "cart": this.$store.getters['products/getCartContent'], 
                "token":this.$store.getters['products/getToken'].token
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