<template>
    <div v-for="product in getOrderedProducts" :key="product.id">
        {{ product }}
    </div>
</template>
<script>
export default {
    props: ['quantityList'],
    computed: {
        getOrderedProducts() {
            const orderId = this.$route.params.id
            const orders = this.$store.getters['orders/getOrders'].shopOrderList;
            const order = orders.find(order => order.id.toString() === orderId);
            return order.quantityList;
        }
    },
}
</script>