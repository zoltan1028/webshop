import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import BaseCard from "./UI/BaseCard.vue";
import BaseButton from "./UI/BaseButton.vue";
import StoreCart from "./components/StoreCart.vue";
import BaseModal from "./UI/BaseModal.vue";
import StoreRegistration from "./components/StoreRegistration.vue";
import "./UI/Decoder.css";
//css for bootstrap
import "bootstrap/dist/css/bootstrap.min.css";
//js for bootstrap
import "bootstrap";
const webshop = createApp(App);

webshop.component("base-card", BaseCard);
webshop.component("base-button", BaseButton);
webshop.component("store-cart", StoreCart);
webshop.component("base-modal", BaseModal);
webshop.component("store-registration", StoreRegistration);
webshop.use(router);
webshop.use(store);
webshop.mount("#app");
