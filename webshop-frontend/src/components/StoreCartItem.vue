<template>
    <div>
        <p>{{ name }} {{ pieces }}</p>
        <base-button class="button-color-primary" @onClick="this.$emit('onQtyChanged', [this.id, '+'])">+</base-button>
        <base-button class="button-color-delete" @onClick="this.$emit('onQtyChanged', [this.id, '-'])">-</base-button>
    </div>
</template>
<script>
export default {
    props: ['id', 'name', 'pieces'],
}
</script>