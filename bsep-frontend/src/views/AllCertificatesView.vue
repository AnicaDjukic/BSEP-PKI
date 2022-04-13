<template>
  <table
    v-for="cert in certificates"
    :key="cert.serialNumber"
    style="
      border: 1px solid gray;
      width: 50%;
      margin-left: 25%;
      margin-top: 5%;
      background: white;
      zoom: 1;
    "
    class="table table-striped"
  >
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
        <td>{{ cert.certificateType }}</td>
        <th>Username:</th>

        <td>{{ cert.username }}</td>
        <!--Subject data-->
      </tr>
      <tr>
        <th>Serial number:</th>
        <td>{{ cert.serialNumber }}</td>
        <th>Country:</th>

        <td>{{ cert.countryCode }}</td>
      </tr>
      <tr>
        <th>Start date:</th>
        <td>{{ cert.startDate }}</td>
        <th>Organization unit:</th>

        <td>{{ cert.organization }}</td>
      </tr>
      <tr>
        <th>Expiration date:</th>
        <td>{{ cert.endDate }}</td>
        <th>Organization name:</th>

        <td>{{ cert.organizationalUnitName }}</td>
      </tr>
      <tr v-bind:class = "(cert.status=='VALID')?'green':'red'">
        <th>Certificate status:</th>
        <td>{{ cert.status }}</td>
        <th>Issuer common name</th>

        <td>{{ cert.issuerCommonName }}</td>
      </tr>
      <tr>
        <td>
          <button
            type="button"
            class="btn btn-primary"
            style="
              background-color: rgb(3, 20, 50);
              border-color: rgb(3, 20, 50);
              width: 100%
            "
            v-on:click="isRevoked(cert.id)"
          >
            Is revoked
          </button>
        </td>
        <td>
          <button
            type="button"
            class="btn btn-primary"
            style="
              background-color: rgb(3, 20, 50);
              border-color: rgb(3, 20, 50);
              width: 70%
            "
            v-if="cert.status != 'REVOKED' && role === 'ROLE_ADMIN'"
            v-on:click="revokeCertificate(cert.id)"
          >
            Revoke certificate
          </button>
        </td>
        <td>
          <button
            type="button"
            class="btn btn-primary"
            style="
              background-color: rgb(3, 20, 50);
              border-color: rgb(3, 20, 50);
            "
            v-on:click="showModal(cert.serialNumber, cert.endDate)"
            v-if="cert.certificateType != 'END_ENTITY' && cert.status == 'VALID'"
          >
            Issue certificate
          </button>
        </td>
        <td>
          <button
            type="button"
            class="btn btn-primary"
            style="
              background-color: rgb(3, 20, 50);
              border-color: rgb(3, 20, 50);
              width: 110%;
              margin-left: -10%;
            "
            v-on:click="downloadCertificate(cert.id)"
          >
            Download certificate
          </button>
        </td>
      </tr>
    </tbody>
  </table>
  <IssueCertificateModal
    v-show="isModalVisible"
    @close="closeModal"
    v-bind:issuerCertificateSerialNumber="issuerCertificateSerialNumber"
    v-bind:issuerExpirationDate="issuerExpirationDate"
  />
</template>

<script>
import axios from 'axios'
import IssueCertificateModal from '@/components/IssueCertificateModal.vue'
export default {
  name: 'AllCertificatesView',
  components: { IssueCertificateModal },
  data: function () {
    return {
      isModalVisible: false,
      certificates: [],
      issuerCertificateSerialNumber: '',
      issuerExpirationDate: null,
      role: ''
    }
  },
  mounted: function () {
    const jwtToken = window.sessionStorage.getItem('jwt')
    if (jwtToken) {
      const tokenSplit = jwtToken.split('.')
      const decoded = decodeURIComponent(escape(window.atob(tokenSplit[1])))
      const obj = JSON.parse(decoded)
      console.log(obj.role)
      this.role = obj.role
    }

    if (this.role == null) this.role = ''
    axios.defaults.headers.common.Authorization =
      'Bearer ' + window.sessionStorage.getItem('jwt')
    axios.get('http://localhost:8080/api/v1/certificate').then((response) => {
      this.certificates = response.data
    })
  },
  methods: {
    formatDate: function (date) {
      const d = new Date(date)
      let month = '' + (d.getMonth() + 1)
      let day = '' + d.getDate()
      const year = d.getFullYear()

      if (month.length < 2) month = '0' + month
      if (day.length < 2) day = '0' + day

      return [year, month, day].join('-')
    },
    showModal (serialNumber, expirationDate) {
      this.issuerExpirationDate = expirationDate
      this.issuerCertificateSerialNumber = serialNumber
      this.isModalVisible = true
    },
    downloadCertificate (id) {
      axios.defaults.headers.common.Authorization =
        'Bearer ' + window.sessionStorage.getItem('jwt')
      axios
        .get('http://localhost:8080/api/v1/certificate/' + id + '/download')
        .then((response) => {
          const blob = new Blob([response.data], {
            type: 'application/x-x509-ca-cert'
          })
          const url = window.URL.createObjectURL(blob)
          window.open(url)
        })
    },
    closeModal () {
      this.isModalVisible = false
    },
    revokeCertificate (certificateId) {
      axios.defaults.headers.common.Authorization =
        'Bearer ' + window.sessionStorage.getItem('jwt')
      axios
        .put(
          'http://localhost:8080/api/v1/certificate/' +
            certificateId +
            '/revoke'
        )
        .then(() => {
          window.location.reload()
        })
    },
    isRevoked (certificateId) {
      axios.defaults.headers.common.Authorization =
        'Bearer ' + window.sessionStorage.getItem('jwt')
      axios
        .get(
          'http://localhost:8080/api/v1/certificate/' +
            certificateId +
            '/status'
        )
        .then((response) => {
          if (response.data) {
            alert('Certificate is revoked')
          } else {
            alert('Certifivate is not revoked')
          }
        })
    }
  }
}
</script>
<style scoped src="@/css/Admin.css"></style>
<style scoped src="@/css/Login.css"></style>
