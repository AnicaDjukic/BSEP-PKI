<template>
  <link
    href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
    rel="stylesheet"
    id="bootstrap-css"
  />
  <div class="wrapper fadeInDown">
    <div id="formContent">
      <!-- Login Form -->
      <form>
        <input
          type="text"
          class="fadeIn second"
          name="login"
          placeholder="login"
          v-model="username"
        />
        <input
          type="password"
          class="fadeIn third"
          name="login"
          placeholder="password"
          v-model="password"
        />
        <input
          type="button"
          class="fadeIn fourth"
          value="Log In"
          v-on:click="login()"
        />
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'LoginView',
  data: function () {
    return {
      username: '',
      password: ''
    }
  },
  mounted: function () {},
  methods: {
    login: function () {
      const user = {
        username: this.username,
        password: this.password
      }
      axios
        .post('http://localhost:8080/api/v1/auth/login', user)
        .then((response) => {
          window.sessionStorage.setItem('jwt', response.data.jwt)
          this.$router.push('/admin-page')
        })
    }
  }
}
</script>
<style scoped src="@/css/Login.css"></style>
