<template>
    <h2>Orders</h2>
    <base-card>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Total</th>
                </tr>
            </thead>
            <order-list-item v-for="order in getOrders" @onOrderDetailsEvent="toOrderDetails(order.id)"
                @onDeleteOrder="deleteOrder(order.id)" :key="order.id" :id="order.id" :status="order.status"
                :orderReceivedAt="order.orderReceivedAt" :orderTotal="order.orderTotal"></order-list-item>
        </table>
    </base-card>
    <base-modal :show="showModal" title="Are you sure you want to delete your order?">
        <template #actions>
            <base-button @onClick="cancelDelete" class="button-color-primary">Cancel</base-button>
            <base-button @onClick="confirmDelete" class="button-color-delete">Delete</base-button>
        </template>
    </base-modal>

</template>
<script>
import OrderListItem from './OrderListItem.vue'
export default {
    components: { OrderListItem },
    data() {
        return {
            orders: [],
            error: "",
            showModal: false,
            orderId: null
        }
    },
    created() {
        try {
            const token = this.$store.getters['authentication/getAuth'].token
            this.$store.dispatch('orders/getOrdersOfUser', token);
        } catch (e) {
            this.error = e
        }
    },
    computed: {
        getOrders() {
            return this.$store.getters['orders/getOrdersOfUser'].shopOrderList
        }
    },
    methods: {
        toOrderDetails(orderId) {
            this.$router.push({ name: 'OrderDetails', params: { id: orderId } });
        },
        cancelDelete() {
            this.showModal = false
        },
        async confirmDelete() {
            this.showModal = false
            const authentication = this.$store.getters['authentication/getAuth']
            try {
                await this.$store.dispatch('orders/deleteOrder', { token: authentication.token, orderId: this.orderId })
                await this.$store.dispatch('orders/getOrdersOfUser', authentication.token)
            } catch (error) {
                this.error = error.message;
            }
        },
        deleteOrder(orderId) {
            this.showModal = true
            this.orderId = orderId
        }
    }
}
</script>
<style>
table {
    margin-left: auto;
    margin-right: auto;
    border-collapse: collapse;
    width: 100%;
}
thead {
    text-align: center;
}
</style>