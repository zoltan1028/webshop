<template>
    <base-card>
        <table>
            <thead>
                <th>Product</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Total Cost</th>
            </thead>
            <tbody>
                <tr v-for="orderItem in getOrderedProducts" :key="orderItem.id">
                    <td>{{ orderItem.product.name }}</td>
                    <td>{{ orderItem.product.price }}</td>
                    <td>{{ orderItem.quantity }}</td>
                    <td>{{ orderItem.sum }}</td>     
                </tr>
            </tbody>
        </table>
    </base-card>
</template>
<script>
export default {
    props: ['quantityList'],
    computed: {
        getOrderedProducts() {
            const orderId = this.$route.params.id
            const orders = this.$store.getters['orders/getOrdersOfUser'].shopOrderList;
            const order = orders.find(order => order.id.toString() === orderId);
            return order.quantityList;
        }
    },
}
</script>