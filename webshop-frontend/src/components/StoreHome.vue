<template>
    <h2>Store</h2>
    <div>Number of pages: {{ getNumberOfPages }}</div>
    <div>Items found: {{ getProductsCount }}</div>
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
                <base-button class="button-color-delete">{{ initWithTokenStatus.token !== null ? 'Logout' :
        'Login' }}</base-button>
            </div>

        </div>
    </form>
    <div>
        <base-button class="button-color-primary" mode="link" to="/registration">Registration</base-button>
    </div>
    <base-button v-if="initWithTokenStatus.userRight === 'ADMIN'" class="button-color-delete" mode="link"
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
                <li @click="loadProducts(page - 1)" class="page-item"><a class="page-link" href="#">{{ page - 1
                        }}</a></li>
                <li @click="loadProducts(page)" class="page-item"><a class="page-link" href="#">{{ page
                        }}</a></li>
                <li @click="loadProducts(page + 1)" class="page-item"><a class="page-link" href="#">{{ page + 1
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
        this.connectWebSocket();
        this.loadProducts();
    },
    data() {
        return {
            username: "",
            password: "",
            error: null,
            page: 1,
        }
    },
    computed: {
        getProducts() {
            const products = this.$store.getters['products/getProducts'];
            return products ? products.content : null;
        },
        getProductsCount() {
            const products = this.$store.getters['products/getProducts'];
            return products ? products.totalElements : 0;
        },
        getNumberOfPages() {
            const products = this.$store.getters['products/getProducts'];
            return products ? products.totalPages : 0;
        },
        initWithTokenStatus() {
            const auth = this.$store.getters['authentication/getAuth'];
            return auth;
        }
    },
    methods: {
        connectWebSocket() {
            this.socket = new WebSocket('ws://localhost:8080/notify');
            this.socket.onopen = () => {
                this.isConnected = true;
                console.log('WebSocket connection opened.');
            };

            this.socket.onmessage = (event) => {
                //on expired token message removes token -- auth check for UI login/out --
                this.lastNotification = event.data;
                console.log('Received notification:', event.data);
                this.$store.dispatch('authentication/removeToken');
                console.log("tokenremoved:")
                console.log(this.$store.getters['authentication/getAuth'].token)
            };
            this.socket.onclose = () => {
                this.isConnected = false;
                console.log('WebSocket connection closed.');
                // Optionally, attempt to reconnect
                setTimeout(this.connectWebSocket, 1000); // Try reconnecting after 5 seconds
            };

            this.socket.onerror = (error) => {
                console.error('WebSocket error:', error);
            };
        },
        async updatePageNums(operation) {
            if (operation === "increase") {
                await this.loadProducts(this.page + 2);
                if (!this.$store.getters['products/getProductsIsEmpty']) {
                    this.page += 1;
                }
            }
            else if (operation === 'decrease' && this.page > 1) {
                this.page -= 1;
                this.loadProducts(this.page - 1);
            }
        },
        resetError() {
            this.error = null;
        },
        async loadProducts(page = "0") {
            await this.$store.dispatch('products/getProducts', page);
        },
        async submitLogin() {
            const auth = this.$store.getters['authentication/getAuth']
            if (!auth.token) {
                console.log(auth.token)
                const loginForm = {
                    username: this.username,
                    password: this.password,
                }
                try {
                    await this.$store.dispatch('authentication/authLogin', loginForm)
                } catch (error) {
                    this.error = error.message;
                }
            } else {
                try {
                    await this.$store.dispatch('authentication/authLogout', auth.token)
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