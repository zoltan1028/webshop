<template>
    <h2>Store</h2>
    <div>Number of pages: {{ getNumberOfPages }}</div>
    <div>Items found: {{ getProductsCount }}</div>
    <div class="row align-items-center justify-content-center my-3">
        <div class="col-2">
            <base-button v-if="getAuthentication.userRight === 'ADMIN'" class="button-color-delete" mode="link"
                to="/manageproduct">Upload Stuff</base-button>
        </div>
        <div class="col-2">
            <base-button v-if="getAuthentication.token !== null" class="button-color-delete" mode="link"
                to="/orders">Orders</base-button>
        </div>
        <div class="col-2">
            <base-button mode="link" class="button-color-primary" to="/storecart">Cart</base-button>
        </div>
    </div>

    <form @submit.prevent="submitLogin">
        <div class="row align-items-center justify-content-center">
            <div class="col-2">
                <label for="username" class="form-label">Username</label>
                <input v-model="username" type="text" id="username" class="form-control" required>
            </div>
            <div class="col-2">
                <label for="password" class="form-label">Password</label>
                <input v-model="password" type="password" id="password" class="form-control" required>
            </div>
            <div class="col-2">
                <base-button class="button-color-delete">{{ getAuthentication.token !== null ? 'Logout' :
                    'Login' }}</base-button>
            </div>
            <div class="col-2">
                <base-button class="button-color-primary" mode="link" to="/registration">Registration</base-button>
            </div>

        </div>
    </form>

    <div class="row align-items-center justify-content-end">
        <div class="col-2">
            <div class="dropdown">
                <base-button class="button-color-primary dropdown-toggle" mode="button" id="kategoria"
                    data-bs-toggle="dropdown">{{ orderBy }}</base-button>
                <ul class="dropdown-menu">
                    <li v-for="c in getCategories" :key="c" :value="c"><a class="dropdown-item" href="#"
                            @click.prevent="setOrderBy(c) == c">{{ c }}</a></li>
                </ul>
            </div>
        </div>
        <base-button class="button-color-delete" @onClick="invertSortDir">{{ buttonLabelSort }}</base-button>
    </div>

    <div v-if="!getProducts" class="d-flex justify-content-center align-items-center"><span class="spinner"></span>
    </div>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5">
        <store-item v-for="product in getProducts" :id="product.id" :name="product.name" :price="product.price"
            :weight="product.weight" :stock="product.stock" :description="product.description"
            :picture="product.picture" :category="product.category" :key="product.id">
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
import StoreItem from './StoreItem.vue'
export default {
    components: { StoreItem },
    created() {
        this.connectWebSocket();
        this.loadProducts();
    },
    data() {
        return {
            username: "",
            password: "",
            error: null,
            buttonLabelSort: "Asc",
            orderBy: "name",
            page: 1,
            query: {
                page: 1,
                size: 5,
                category: "FRUIT",
                sort: "name,asc"
            }
        }
    },
    computed: {
        getProducts() {
            const product = this.$store.getters['products/getProducts'];
            return product ? product.content : null;
        },
        getCategories() {
            const products = this.$store.getters['products/getProducts'];
            return products ? Object.keys(products.content[0]) : [];
        },
        getProductsCount() {
            const products = this.$store.getters['products/getProducts'];
            return products ? products.totalElements : 0;
        },
        getNumberOfPages() {
            const products = this.$store.getters['products/getProducts'];
            return products ? products.totalPages : 0;
        },
        getAuthentication() {
            const auth = this.$store.getters['authentication/getAuth'];
            return auth;
        }
    },
    methods: {
        setOrderBy(value) {
            this.orderBy = value;
            this.query.sort = `${value},desc`
            console.log(this.query)

            this.loadProducts(this.query.page);
        },
        invertSortDir() {
            this.buttonLabelSort = this.buttonLabelSort === 'asc' ? 'desc' : 'asc'
            this.query.sort = `${this.orderBy},${this.buttonLabelSort}`
            console.log(this.query)

            this.loadProducts(this.query.page)
        },
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
                this.$router.push('/')
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
        async loadProducts(page = 0) {
            if (page !== undefined) {
                this.query.page = page
            }
            console.log(this.query)
            await this.$store.dispatch('products/getProducts', this.query);
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
        }
    }
}
</script>

<style scoped></style>