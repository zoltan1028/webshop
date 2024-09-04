import { createStore } from 'vuex'

import productsModule from './modules/products/index.ts';
import authModule from './modules/authentication/index.ts';
import ordersModule from './modules/orders/index.ts';

const store = createStore({
  modules: {
    products: productsModule,
    authentication: authModule,
    orders: ordersModule
  },
}) 

export default store;