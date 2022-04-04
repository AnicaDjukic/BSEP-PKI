<template>
  <ul>
    <li><a class="active" href="/all-certificates-page">Certificates</a></li>
    <li><a href="/create-certificate-page">Create certificate</a></li>
  </ul>
  <div v-for="c in certificates" :key="c" class="list-group">
  <a href="#" class="list-group-item list-group-item-action flex-column align-items-start active">
    <div class="d-flex w-100 justify-content-between">
      <h5 class="mb-1">List group item heading</h5>
      <small>3 days ago</small>
    </div>
    <p class="mb-1">Donec id elit non mi porta gravida at eget metus. Maecenas sed diam eget risus varius blandit.</p>
    <small>Donec id elit non mi porta.</small>
  </a>
</div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'AllCertificatesView',
  data: function () {
    return {
      certificates: []
    }
  },
  mounted: function () {
    axios
      .get('http://localhost:8080/api/v1/certificate')
      .then((response) => {
        this.certificates = response.data
      })
  },
  methods: {}
}
</script>
<style scoped src="@/css/Admin.css"></style>
<style scoped src="@/css/Login.css"></style>
