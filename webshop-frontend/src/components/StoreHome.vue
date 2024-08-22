<template>
    <h2>Store</h2>
    <base-button mode="link" class="button-color-primary" to="/storecart">Cart</base-button>

    <form @submit.prevent="submitLogin">
        <div class="row">
            <div class="col-2">
                <label for="username" class="form-label">Username</label>
                <input v-model="username" type="text" id="username" class="form-control" required>
            </div>
            <div class="col-2">
                <label for="password" class="form-label">Password</label>
                <input v-model="password" type="password" id="password" class="form-control" required>
            </div>
            <div class="col-2">
                <base-button class="button-color-delete">Login</base-button>
            </div>
        </div>
    </form>
    <base-button class="button-color-delete" mode="link" to="/manageproduct">Upload Stuff</base-button>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5">
        <store-item v-for="product in getProducts" :id="product.id" :name="product.name" :price="product.price"
            :weight="product.weight" :stock="product.stock" :description="product.description"
            :picture="product.picture" :key="product.id">
        </store-item>
    </div>
    <base-modal :show="error" title="An error occured">
        <h5>{{ error }}</h5>
        <template #actions>
            <base-button @onClick="resetError" class="button-color-primary">Ok</base-button>
        </template>
    </base-modal>

</template>
<script>
export default {
    created() {
        this.loadProducts();
    },
    data() {
        return {
            username: "",
            password: "",
            error: null
        }
    },
    computed: {
        getProducts() {
            console.log("call")
            return this.$store.getters['products/getProducts'];
        }
    },
    methods: {
        resetError() {
            this.error = null;
        },
        async loadProducts() {
            await this.$store.dispatch('products/getProducts');
        },
        async submitLogin() {
            console.log(this.username + this.password)
            const loginForm = {
                username: this.username,
                password: this.password
            }
            try {
                await this.$store.dispatch('products/authLogin', loginForm)
            } catch (error) {
                this.error = error.message;
            }
            //console.log(submitEvent.target.elements.username.value)
        }
    }
}
</script>

<style scoped></style>