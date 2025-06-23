<template>
  <div class="mx-5">
    <nav>
      <router-link to="/">Home</router-link>
      <base-button v-if="!isHomePage" @click="previousPage" class="button-color-primary">Back</base-button>
    </nav>
    <router-view />
  </div>
</template>
<script>
export default {
  props: ['quantityList'],
  computed: {
    isHomePage() {
      const currentPath = this.$route.path
      let isHomePage = true
      if (currentPath === "/storehome") {
        isHomePage = true
      } else {
        isHomePage = false
      }
      return isHomePage
    }
  },
  methods: {
    previousPage() {
      this.$router.back()
    }
  }
}
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
