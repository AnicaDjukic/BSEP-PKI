<template>
<link
    href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
    rel="stylesheet"
    id="bootstrap-css"
  />
  <ul>
    <li><a class="active" href="/all-certificates-page">Certificates</a></li>
    <li><a href="/create-certificate-page">Create certificate</a></li>
  </ul>
  <div class="wrapper fadeInDown" style="margin-top: 3em">
    <div id="formContent">
      <!-- Login Form -->
      <form style="margin-top: 3rem">
        <select
          class="browser-default custom-select"
          style="width: 20em"
          v-model="certificateType" disabled
        >
          <option selected hidden value="ROOT">ROOT</option>
        </select>
        <select
          class="browser-default custom-select"
          style="width: 20em; margin-top: 2em"
          v-model="issuer"
          v-if="
            certificateType == 'INTERMEDIATE' ||
            certificateType == 'END_ENTITY'
          "
        >
          <option selected hidden>Issuer</option>
          <option value="ROOT">ROOT</option>
          <option value="INTERMEDIATE">INTERMEDIATE</option>
          <option value="END_ENTITY">END ENTITY</option>
        </select>
        <select
          class="browser-default custom-select"
          style="width: 20em; margin-top: 2em"
          v-model="subject"
        >
          <option selected hidden>Subject</option>
          <option
            v-for="option in allSubjects"
            :value="option.id"
            :key="option.username"
          >
            {{ option.username }}
          </option>
        </select>
        <input
          style="width: 20em; margin-top: 2em"
          type="text"
          class="fadeIn third"
          name="login"
          placeholder="Oragnization"
          v-model="organization"
        />
        <input
          style="width: 20em; margin-top: 1em"
          type="text"
          class="fadeIn third"
          name="login"
          placeholder="Oragnization unit name"
          v-model="organizationUnitName"
        />
        <input
          style="width: 20em; margin-top: 1em"
          type="text"
          class="fadeIn third"
          name="orgEmail"
          placeholder="Organization email"
          v-model="organizationEmail"
        />
        <input
          style="width: 20em; margin-top: 1em"
          type="text"
          class="fadeIn third"
          name="orgEmail"
          placeholder="Country code"
          v-model="countryCode"
        />
        <Datepicker
          v-model="date"
          style="width: 20em; margin-top: 1em"
          class="fadeIn third"
          id="datePicker"
        ></Datepicker>
        <div style="text-align:left;margin-left:4em">
        Key usages:
        </div>
        <select
          class="browser-default custom-select"
          style="width: 20em"
          v-model="keyUsages"
          multiple
        >
          <option selected hidden>Certificate type</option>
          <option value="128">digitalSignature</option>
          <option value="64">nonRepudiation</option>
          <option value="32">keyEncipherment</option>
          <option value="16">dataEncipherment</option>
          <option value="8">keyAgreement</option>
          <option value="2">cRLSign</option>
          <option value="1">encipherOnly</option>
          <option value="32768">decipherOnly</option>
        </select>
        <input
          style="margin-top: 1em"
          type="button"
          class="fadeIn fourth"
          value="Create certificate"
          v-on:click="createCertificate()"
        />
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

export default {
  name: 'CreateCertficateView',
  components: { Datepicker },
  data: function () {
    return {
      certificateType: 'ROOT',
      subject: 'Subject',
      issuer: 'Issuer',
      organization: '',
      organizationUnitName: '',
      organizationEmail: '',
      countryCode: '',
      allSubjects: [],
      date: null,
      keyUsages: []
    }
  },
  mounted: function () {
    axios
      .get('http://localhost:8080/api/v1/users/getAllUsers')
      .then((response) => {
        this.allSubjects = response.data
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
    createCertificate: function () {
      const newCertificate = {
        subjectUID: Number(this.subject),
        organization: this.organization,
        organizationalUnitName: this.organizationUnitName,
        organizationEmail: this.organizationEmail,
        countryCode: this.countryCode,
        issuerCertificateId: this.issuer,
        endDate: this.formatDate(this.date),
        certificateType: this.certificateType,
        keyUsages: this.keyUsages
      }
      axios
        .post('http://localhost:8080/api/v1/certificate/create', newCertificate)
        .then((response) => {
          alert('Success')
        })
    }
  }
}
</script>
<style scoped src="@/css/Admin.css"></style>
<style scoped src="@/css/Login.css"></style>
