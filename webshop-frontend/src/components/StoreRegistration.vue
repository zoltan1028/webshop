<template>
    <div class="d-flex justify-content-center">
        <form @submit.prevent="register" class="card col-5">
            <div class="mb-3">
                <label for="exampleInputEmail1" class="form-label">Email address</label>
                <input v-model="username" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required>
                <div v-if="registrationMessage" id="emailHelp" class="form-text">{{ registrationMessage }}</div>
                <div v-else id="emailHelp" class="form-text">We'll never share your email with anyone else. :)))</div>
            </div>
            <div class="mb-3">
                <label for="exampleInputPassword1" class="form-label">Password</label>
                <input v-model="password" type="password" class="form-control" id="exampleInputPassword1" required>
            </div>
            <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="exampleCheck1" required>
                <label class="form-check-label" for="exampleCheck1">Check me out</label>
            </div>
            <button type="submit" class="btn btn-primary">Register</button>
        </form>
    </div>

</template>
<script>
export default {
    data() {
        return {
            username: "",
            password: "",
            registrationMessage: null
        }
    },
    methods: {
        async register() {
            const newRegister = {
                username: this.username,
                password: this.password
            }
            try {
                console.log("reg")
                await this.$store.dispatch('authentication/register', newRegister)
                this.registrationMessage = "Registration was successful."
            } catch (error) {
                console.log(error)
                this.registrationMessage = error
            }
        }
    }
}
</script>