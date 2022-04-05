<template>
  <table
    v-for="cert in certificates"
    :key="cert.serialNumber"
    style="border: 1px solid gray; width: 50%; margin-left: 25%; margin-top: 5%"
    class="table"
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
        <td>12/12/2022</td>
        <th>Organization unit:</th>

        <td>{{ cert.organization }}</td>
      </tr>
      <tr>
        <th>Expiration date:</th>
        <td>{{ cert.endDate }}</td>
        <th>Organization name:</th>

        <td>{{ cert.organizationalUnitName }}</td>
      </tr>
      <tr>
        <th>Certificate status:</th>
        <td>{{ cert.status }}</td>
        <th>Issuer common name</th>

        <td>{{ cert.issuerCommonName }}</td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td>
          <button
            type="button"
            class="btn btn-primary"
            v-on:click="downloadCertificate(cert.id)"
          >
            Download certificate
          </button>
        </td>
        <td>
          <button
            type="button"
            class="btn btn-primary"
            v-on:click="showModal(cert.serialNumber)"
            v-if="cert.certificateType != 'END_ENTITY'"
          >
            Issue certificate
          </button>
        </td>
      </tr>
    </tbody>
  </table>
  <IssueCertificateModal
    v-show="isModalVisible"
    @close="closeModal"
    v-bind:issuerCertificateSerialNumber="issuerCertificateSerialNumber"
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
      issuerCertificateSerialNumber: ''
    }
  },
  mounted: function () {
    axios.defaults.headers.common.Authorization =
                'Bearer ' + window.sessionStorage.getItem('jwt')
    axios.get('http://localhost:8080/api/v1/certificate').then((response) => {
      this.certificates = response.data
    })
  },
  methods: {
    showModal (serialNumber) {
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
    }
  }
}
</script>
<style scoped src="@/css/Admin.css"></style>
<style scoped src="@/css/Login.css"></style>
