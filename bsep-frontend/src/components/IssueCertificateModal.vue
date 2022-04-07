<template>
  <transition name="modal-fade">
    <div centered class="modal-backdrop">
      <div class="modal">
        <header class="modal-header">
          <slot name="header"> Create certificate </slot>
          <button type="button" class="btn-close" v-on:click="close()">
            x
          </button>
        </header>

        <section class="modal-body">
          <slot name="body">
            <select
              class="form-select"
              aria-label="Default select example"
              v-model="certificateType"
              style="width: 70%; margin-left: 15%"
            >
              <option selected hidden>Certificate type</option>
              <option value="INTERMEDIATE">INTERMEDIATE</option>
              <option value="END_ENTITY">END ENTITY</option>
            </select>
            <select
              class="form-select"
              style="width: 70%; margin-top: 1em; margin-left: 15%"
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
              style="width: 70%; margin-top: 1em; margin-left: 15%"
              type="text"
              class="form-control"
              name="login"
              placeholder="Oragnization"
              v-model="organization"
            />
            <input
              style="width: 70%; margin-top: 1em; margin-left: 15%"
              type="text"
              class="form-control"
              name="login"
              placeholder="Oragnization unit name"
              v-model="organizationUnitName"
            />
            <input
              style="width: 70%; margin-top: 1em; margin-left: 15%"
              type="text"
              class="form-control"
              name="orgEmail"
              placeholder="Organization email"
              v-model="organizationEmail"
            />
            <input
              style="width: 70%; margin-top: 1em; margin-left: 15%"
              type="text"
              class="form-control"
              name="orgEmail"
              placeholder="Country code"
              v-model="countryCode"
            />
            <Datepicker
              :maxDate="issuerExpirationDate"
              v-model="date"
              style="width: 70%; margin-top: 1em; margin-left: 15%"
              class="fadeIn third"
              id="datePicker"
            ></Datepicker>
            <div style="text-align: left; margin-left: 15%; margin-top: 3%">
              Key usages:
            </div>
            <select
              class="form-select"
              style="width: 70%; margin-top: 1em; margin-left: 15%"
              v-model="keyUsages"
              multiple
            >
              <option selected hidden>Certificate type</option>
              <option value="128">Digital signature</option>
              <option value="64">Non repudiation</option>
              <option value="32">Key encipherment</option>
              <option value="16">Data encipherment</option>
              <option value="8">Key agreement</option>
              <option value="2">cRL sign</option>
              <option value="1">Encipher only</option>
              <option value="32768">Decipher only</option>
            </select>
            <input
              style="
                margin-top: 1em;
                margin-left: 34%;
                background-color: rgb(3, 20, 50);
                border-color: rgb(3, 20, 50);
              "
              type="button"
              class="btn btn-primary"
              value="Create certificate"
              v-on:click="createCertificate()"
            />
          </slot>
        </section>
      </div>
    </div>
  </transition>
</template>
<script>
import axios from 'axios'
import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
export default {
  name: 'IssueCertificateModal',
  props: ['issuerCertificateSerialNumber', 'issuerExpirationDate'],
  components: { Datepicker },
  data: function () {
    return {
      certificateType: 'Certificate type',
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
    axios.defaults.headers.common.Authorization =
      'Bearer ' + window.sessionStorage.getItem('jwt')
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
    close () {
      this.$emit('close')
    },
    createCertificate: function () {
      const newCertificate = {
        subjectUID: Number(this.subject),
        organization: this.organization,
        organizationalUnitName: this.organizationUnitName,
        organizationEmail: this.organizationEmail,
        countryCode: this.countryCode,
        issuerCertificateSerialNumber: this.issuerCertificateSerialNumber,
        endDate: this.formatDate(this.date),
        certificateType: this.certificateType,
        keyUsages: this.keyUsages
      }
      axios
        .post('http://localhost:8080/api/v1/certificate/create', newCertificate)
        .then((response) => {
          window.location.reload()
        })
    }
  }
}
</script>
<style>
.modal-backdrop {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  margin-top: 18%;
  background: #ffffff;
  box-shadow: 2px 2px 20px 1px;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
  height: 85%;
  width: 30%;
  position: relative;
  top: 25%;
  transform: translateY(-50%);
}
.modal-header,
.modal-footer {
  padding: 15px;
  display: flex;
}

.modal-header {
  position: relative;
  border-bottom: 1px solid #eeeeee;
  color: rgb(3, 20, 50);
  justify-content: space-between;
}

.modal-footer {
  border-top: 1px solid #eeeeee;
  flex-direction: column;
  justify-content: flex-end;
}

.modal-body {
  position: relative;
  padding: 20px 10px;
}

.btn-close {
  position: absolute;
  top: 0;
  right: 0;
  border: none;
  font-size: 20px;
  padding: 10px;
  cursor: pointer;
  font-weight: bold;
  color: #4aae9b;
  background: transparent;
}

.btn-green {
  color: white;
  background: #4aae9b;
  border: 1px solid #4aae9b;
  border-radius: 2px;
}
</style>
