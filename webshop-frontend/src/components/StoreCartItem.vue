<template>
    <div>
        <p>{{ name }} {{ pieces }}</p>
        <base-button class="button-color-primary" @onClick="this.$emit('onQtyChanged', {productId: this.id, arthimeticOperator: '+'})">+</base-button>
        <base-button class="button-color-delete" @onClick="this.$emit('onQtyChanged', {productId: this.id, arthimeticOperator: '-'})">-</base-button>
    </div>
</template>
<script>
export default {
    props: ['id', 'name', 'pieces'],
}
</script>