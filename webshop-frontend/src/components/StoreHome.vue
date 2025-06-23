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
        <div class="row align-items-center justify-content-end">
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
        <div class="col-2">
            <base-button class="button-color-delete" @onClick="invertSortDir">{{ buttonLabelSort }}</base-button>

        </div>
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
                <li @click="onButtonPress('previous')" class="page-item"><a class="page-link" href="#">Previous</a></li>
                <li @click="onButtonPress('left')" class="page-item"><a class="page-link" href="#">{{ buttonLabel }}</a>
                </li>
                <li @click="onButtonPress('middle')" class="page-item"><a class="page-link" href="#">{{ buttonLabel + 1
                }}</a></li>
                <li @click="onButtonPress('right')" class="page-item"><a class="page-link" href="#">{{ buttonLabel + 2
                }}</a></li>
                <li @click="onButtonPress('next')" class="page-item"><a class="page-link" href="#">Next</a></li>
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
            buttonLabelSort: "asc",
            orderBy: "name",
            buttonLabel: 0,
            previousButton: "left",
            query: {
                page: 0,
                size: 5,
                category: "FRUIT",
                sort: "name,asc"
            },
            buttonLookup: {
                left: { left: 0, middle: -1, right: -2 },
                middle: { left: 1, middle: 0, right: -1 },
                right: { left: 2, middle: 1, right: 0 },
                next: { left: 3, middle: 2, right: 1 },
                previous: { left: -1, middle: -2, right: -3 }
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
            this.query.sort = `${this.orderBy},${this.buttonLabelSort}`
            this.loadProducts();
        },
        invertSortDir() {
            this.buttonLabelSort = this.buttonLabelSort === 'asc' ? 'desc' : 'asc'
            this.query.sort = `${this.orderBy},${this.buttonLabelSort}`
            this.loadProducts()
        },
        connectWebSocket() {
            this.socket = new WebSocket('ws://localhost:8080/notify');
            this.socket.onopen = () => {
                this.isConnected = true;
            };
            this.socket.onmessage = (event) => {
                //on expired token message removes token -- auth check for UI login/out --
                this.lastNotification = event.data;
                this.$store.dispatch('authentication/removeToken');
                this.$router.push('/')
            };
            this.socket.onclose = () => {
                this.isConnected = false;
                setTimeout(this.connectWebSocket, 1000); // Try reconnecting after 5 seconds
            };

            this.socket.onerror = (error) => {
                console.error('WebSocket error:', error);
            };
        },
        resetError() {
            this.error = null;
        },
        async onButtonPress(button) {
            if (button === this.previousButton) return;
            const step = this.buttonLookup?.[button]?.[this.previousButton] ?? 0;
            if (step < 0 && this.query.page === 0) return;
            this.query.page += this.buttonLookup[button][this.previousButton];
            await this.loadProducts();
            const isPageEmpty = this.$store.getters['products/getProductsIsEmpty']
            if (isPageEmpty) {this.query.page -= 1;return;}
            this.updateButtonLabels(button);
        },
        updateButtonLabels(button) {
            if (button === "next") {
                this.previousButton = "right";
                this.buttonLabel += 1;
            } else if (button === "previous") {
                this.previousButton = "left";
                this.buttonLabel -= 1;
            } else {
                this.previousButton = button;
            }
        },
        async loadProducts() {
            console.log(this.query)
            await this.$store.dispatch('products/getProducts', this.query);
        },
        async submitLogin() {
            const auth = this.$store.getters['authentication/getAuth']
            if (!auth.token) {
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