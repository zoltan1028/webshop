import actions from './actions.ts';
import mutations from './mutations.ts';
import getters from './getters.ts';

export interface Product {
    id: number
    name: string
    price: number
    stock: number
    description: string
    picture: string
}
export interface ProductFormDTO {
    token: string
    form: Product
}
export interface PageableProductsDTO {
    totalPages: number
    totalElements: number
    size: number
    content: Product[]
    number: number
    sort: {
        empty: boolean
        unsorted: boolean
        sorted: boolean
    }
    first: boolean
    last: boolean
    numberOfElements: number
    pageable: {
        pageNumber: number
        pageSize: number
        sort: {
            empty: boolean
            unsorted: boolean
            sorted: boolean
        }
        offset: number
        unpaged: boolean
        paged: boolean
    }
    empty: boolean
}
export default {
    namespaced: true,
    state() {
        return {
            products: null,
            isProductsEmpty: false,
            productForm: {},
        }
    },
    actions,
    mutations,
    getters
}