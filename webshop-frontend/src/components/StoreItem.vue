<template>
    <div>
        <base-card>
            <img :src="`data:image/png;base64,${picture}`" />
            <p class="text-primary">név : {{ name }}</p>
            <p class="text-primary">ár : {{ price }}</p>
            <p class="text-primary">készleten : {{ stock }}</p>
            <p class="text-primary">kategória : {{ category.toLowerCase() }}</p>
            <p class="text-primary">leírás : {{ description }}</p>
            <base-button class="button-color-primary" mode="button" @onClick="addToCart">Add To Cart</base-button>
        </base-card>
    </div>
</template>
<script>
export default {
    props: ['id', 'name', 'price', 'weight', 'stock', 'description', 'picture', 'category'],
    data() {
        return {
            pieces: 1
        }
    },
    methods: {
        addToCart() {
            const existingItem = this.$store.getters['orders/getCartContent'].find(item => item.id === this.id);
            if (existingItem) {
                existingItem.pieces += this.pieces;
            } else {
                this.$store.dispatch('orders/addProductToCart', {
                'id': this.id,
                'pieces': this.pieces,
                'name': this.name,
                'price': this.price
                });
            }
        }
    }
}
</script>
<style scoped>
img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    /* Use 'cover' to fill the container while maintaining aspect ratio */
}
</style>