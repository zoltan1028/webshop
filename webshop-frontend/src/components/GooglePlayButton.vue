<!--
 Copyright 2020 Google LLC

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<template>
  <div class="examples">
    <div>
      <div class="example">
        <div class="demo">
          <google-pay-button 
            environment="TEST" 
            button-type="plain" 
            button-color="black"
            :paymentRequest.prop="staticObjOfpaymentRequestProp" 
            @loadpaymentdata="onLoadPaymentData"
            @error="onError"
            :onPaymentAuthorized.prop="onPaymentDataAuthorized">
          </google-pay-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import '@google-pay/button-element';
export default {
  computed: {
    staticObjOfpaymentRequestProp() {
      return JSON.parse(JSON.stringify(this.paymentRequest));
    }
  },
  name: 'StaticExample',
  props: ['paymentRequest'],
  methods: {
    onLoadPaymentData(event) {
      console.log('load payment data', event.detail);
      this.$emit('successful-payment', event);
    },
    onPaymentDataAuthorized: (paymentData) => {
      console.log("payment authorized", paymentData);
      return {
        transactionState: "SUCCESS",
      };
    },
    onError: event => {
      console.error('error', event.error);
      this.$emit('payment-failed', event);
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.example {
  margin: 5px;
  display: flex;
  flex-direction: row;
}

.example>.title {
  width: 250px;
  align-items: center;
  display: inherit;
}

.example>.demo {
  flex: 1 0 0;
}

.example>.demo>* {
  margin: 1px;
}
</style>