<template>
    <div>
        <h1>Order Complete</h1>
        <base-button class="button-color-primary" mode="link" to="/">To Shop</base-button>
    </div>
</template>
<script>
export default {
    mounted() {
        setTimeout(() => {
            this.$router.push({ path: '/' });
        }, 10000);
    },
    created() {
        console.log("created complete order shit")
    }

}
</script>