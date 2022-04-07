<template>
  <div class="background">
    <ul v-if="role !== ''">
      <li><a class="active" href="/all-certificates-page">Certificates</a></li>
      <li v-if="role === 'ROLE_ADMIN'">
        <a href="/create-certificate-page">Create root certificate</a>
      </li>
      <li style="float: right" v-if="role !== ''">
        <a v-on:click="logout" href="/">Logout</a>
      </li>
    </ul>
    <router-view />
  </div>
</template>
<script>
export default {
  name: 'App',
  mounted: function () {
    this.updateLinks()
  },
  data: function () {
    return {
      role: ''
    }
  },
  watch: {
    $route (to, from) {
      this.updateLinks()
    }
  },
  methods: {
    logout: function () {
      window.sessionStorage.clear()
      this.role = ''
    },
    updateLinks: function () {
      // const { base64decode } = require('nodejs-base64')
      const jwtToken = window.sessionStorage.getItem('jwt')
      if (jwtToken) {
        const tokenSplit = jwtToken.split('.')
        // const decoded = base64decode(tokenSplit[1])
        const decoded = decodeURIComponent(escape(window.atob(tokenSplit[1])))
        const obj = JSON.parse(decoded)
        console.log(obj.role)
        this.role = obj.role
      }

      if (this.role == null) this.role = ''
    }
  }
}
</script>

<style scoped src="@/css/Admin.css"></style>
<style scoped src="@/css/Login.css"></style>
