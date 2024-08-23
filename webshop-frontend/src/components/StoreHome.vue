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
                <base-button class="button-color-delete">{{ loginButtonText }}</base-button>
            </div>
            <div>
                <base-button class="button-color-primary" mode="link" to="/registration">Registration</base-button>
            </div>
        </div>
    </form>
    <base-button v-if="getTokenStatus.userRight === 'admin'" class="button-color-delete" mode="link" to="/manageproduct">Upload Stuff</base-button>
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
        this.initUI();
    },
    data() {
        return {
            username: "",
            password: "",
            error: null,
            loginButtonText: "Login"
        }
    },
    computed: {
        getProducts() {
            console.log("call")
            return this.$store.getters['products/getProducts'];
        },
        getTokenStatus() {
            return this.$store.getters['products/getToken'];
        }
    },
    methods: {
        initUI() {
            const auth = this.$store.getters['products/getToken'];
            if (auth.token) {
                this.loginButtonText = "Logout"
            } else {
                this.loginButtonText = "Login"
            }
        },
        resetError() {
            this.error = null;
        },
        async loadProducts() {
            await this.$store.dispatch('products/getProducts');
        },
        async submitLogin() {
            const auth = this.$store.getters['products/getToken']
            console.log(auth)
            if (!auth.token) {
                const loginForm = {
                    username: this.username,
                    password: this.password
                }
                try {
                    await this.$store.dispatch('products/authLogin', loginForm)
                    this.loginButtonText = "Logout";
                } catch (error) {
                    this.error = error.message;
                }
            } else {
                console.log("logout")
                this.loginButtonText = "Login";
                try {
                    await this.$store.dispatch('products/authLogout', auth.token)
                } catch (e) {
                    this.error = e
                }
            }

            //console.log(submitEvent.target.elements.username.value)
        }
    }
}
</script>

<style scoped></style>