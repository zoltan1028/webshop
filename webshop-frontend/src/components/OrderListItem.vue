<template>
    <tr>
        <td>{{ id }}</td>
        <td>{{ status }}</td>
        <td>{{ orderReceivedAt }}</td>
        <td>{{ orderTotal }}</td>
        <td>
            <base-button class="button-color-secondary" mode="button" @click="this.$emit('onOrderDetailsEvent')">Order
                Details</base-button>
        </td>
        <td>
            <base-button class="button-color-delete" mode="link" @click="this.$emit('onDeleteOrder')"
                to="/orders">Delete
                Order</base-button>
        </td>
    </tr>
</template>
<script>
export default {
    props: ['id', 'status', 'orderReceivedAt', 'orderTotal'],
}
</script>
<style>
td {
    padding: 12px 22px;
}
tr {
  border-bottom: 1px solid #ccc;
}
</style>
