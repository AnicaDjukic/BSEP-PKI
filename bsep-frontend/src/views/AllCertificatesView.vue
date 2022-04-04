<template>
  <ul>
    <li><a class="active" href="/all-certificates-page">Certificates</a></li>
    <li><a href="/create-certificate-page">Create certificate</a></li>
  </ul>
  <table  v-for="cert in certificates"
            :key="cert.serialNumber" style="border: 1px solid gray; width: 50%; margin-left:25%; margin-top: 5%" class="table">
    <thead style="border: 1px solid gray">
      <tr>
        <th></th>
        <th>Certificate</th>
        <th></th>
        <th>Subject</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <th>Certificate type:</th>
        <td>{{cert.certificateType}}</td>
        <th>Username:</th>

        <td>{{cert.username}}</td> <!--Subject data-->
      </tr>
      <tr>
        <th>Serial number:</th>
        <td>{{cert.serialNumber}}</td>
        <th>Country:</th>

        <td>{{cert.countryCode}}</td>
      </tr>
      <tr>
        <th>Start date:</th>
        <td>12/12/2022</td>
        <th>Organization unit:</th>

        <td>{{cert.organization}}</td>
      </tr>
      <tr>
        <th>Expiration date:</th>
        <td>{{cert.endDate}}</td>
        <th>Organization name:</th>

        <td>{{cert.organizationalUnitName}}</td>
      </tr>
      <tr>
        <th>Certificate status:</th>
        <td>{{cert.status}}</td>
        <th>Issuer common name</th>

        <td>{{cert.issuerCommonName}}</td>
      </tr>
      <tr >
        <td></td>
        <td></td>
        <td></td>
        <td>
        <button  type="button" class="btn btn-primary">Issue certificate</button>
        </td>
      </tr>
    </tbody>
  </table>
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
