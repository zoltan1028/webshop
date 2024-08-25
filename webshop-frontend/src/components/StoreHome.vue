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

        </div>
    </form>
    <div>
        <base-button class="button-color-primary" mode="link" to="/registration">Registration</base-button>
    </div>
    <base-button v-if="getTokenStatus.userRight === 'admin'" class="button-color-delete" mode="link"
        to="/manageproduct">Upload Stuff</base-button>

    <div v-if="!getProducts" class="d-flex justify-content-center align-items-center"><span class="spinner"></span>
    </div>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5">
        <store-item v-for="product in getProducts" :id="product.id" :name="product.name" :price="product.price"
            :weight="product.weight" :stock="product.stock" :description="product.description"
            :picture="product.picture" :key="product.id">
        </store-item>
    </div>
    <div class="d-flex justify-content-center">
        <nav aria-label="Page navigation example">
            <ul class="pagination">
                <li @click="updatePageNums('decrease')" class="page-item"><a class="page-link" href="#">Previous</a>
                </li>
                <li :id="pageX" @click="loadProducts(pageX)" class="page-item"><a class="page-link" href="#">{{ pageX
                        }}</a></li>
                <li :id="pageY" @click="loadProducts(pageY)" class="page-item"><a class="page-link" href="#">{{ pageY
                        }}</a></li>
                <li :id="pageZ" @click="loadProducts(pageZ)" class="page-item"><a class="page-link" href="#">{{ pageZ
                        }}</a></li>
                <li @click="updatePageNums('increase')" class="page-item"><a class="page-link" href="#">Next</a></li>
            </ul>
        </nav>
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
            loginButtonText: "Login",
            pageX: 0,
            pageY: 1,
            pageZ: 2
        }
    },
    computed: {
        getProducts() {
            const products = this.$store.getters['products/getProducts'];
            return products ? products.content : products;
        },
        getTokenStatus() {
            return this.$store.getters['products/getToken'];
        }
    },
    methods: {
        updatePageNums(operation) {
            if (operation === 'increase') {
                this.pageX += 1;
                this.pageY += 1;
                this.pageZ += 1;
                this.loadProducts(this.pageZ);

            } else {
                this.pageX -= 1;
                this.pageY -= 1;
                this.pageZ -= 1;
                this.loadProducts(this.pageX);
            }

        },
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
        async loadProducts(page = "0") {
            await this.$store.dispatch('products/getProducts', page);
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