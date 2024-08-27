<template>
    <form @submit.prevent="submitForm">
        <input v-model="name" class="form-control form-control-md" type="text" placeholder="name" required>
        <input v-model="price" class="form-control form-control-md" type="text" placeholder="price" required>
        <input v-model="weight" class="form-control form-control-md" type="text" placeholder="weight" required>
        <input v-model="stock" class="form-control form-control-md" type="text" placeholder="stock" required>
        <input v-model="description" class="form-control form-control-md" type="text" placeholder="description"
            required>
        <base-button mode="input" class="primary_link button-color-primary_link" accept=".png"
            @change="setPicture">pic</base-button>
        <button>Register Product</button>
    </form>
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
        const form = this.$store.getters['products/getProductForm']
        this.name = form.name;
        this.price = form.price;
        this.weight = form.weight;
        this.stock = form.stock;
        this.description = form.description;
        this.base64Image = form.picture;
    },
    beforeUnmount() {
        const form = {
            name: this.name,
            price: this.price,
            weight: this.weight,
            stock: this.stock,
            description: this.description,
            picture: this.base64Image
        }
        this.$store.dispatch('products/saveProductForm', form)
    },
    data() {
        return {
            name: null,
            price: null,
            weight: null,
            stock: null,
            description: null,
            base64Image: null,
            error: null
        }
    },
    methods: {
        async submitForm() {
            const formData = {
                name: this.name,
                price: this.price,
                weight: this.weight,
                stock: this.stock,
                description: this.description,
                picture: this.base64Image
            }
            console.log('post')
            try {
                await this.$store.dispatch('products/postProduct', { "form": formData, "token": this.$store.getters['products/getToken'].token });
            } catch (error) {
                console.log("catch")
                this.error = error;
            }
        },
        setPicture(event) {
            const file = event.target.files[0];
            try {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    this.base64Image = reader.result.split(',')[1];
                };
            } catch {
                console.log("error pic")
            }
        },
        resetError() {
            this.error = null;
        }
    }
}
</script>