<template>
    <div @click="this.$emit('onOrderDetailsEvent')">
        {{ id }}
    </div>
    <div>
        <base-button class="button-color-delete" mode="link" @click="this.$emit('onDeleteOrder')"
        to="/orders">Delete Order</base-button>
    </div>
</template>
<script>
export default {
    props: ['id', 'status'],
}
</script>