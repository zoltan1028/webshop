<template>
  
    <button v-if="mode === 'button'" @click="$emit('onClick')" :class="css">
        <slot></slot>
    </button>

    <label v-else-if="mode === 'input'">
      <input type="file" :accept="accept" @change="$emit('onChange', $event)"/>
      <a :class="css">
        <slot></slot>
      </a>
    </label>

    <router-link v-else-if="mode === 'link'" :to="to" :class="css">
        <slot></slot>
    </router-link>

</template>

<script>
export default {
    props: {
        accept: {
          type: String,
          required: false,
          default: ""
        },
        mode: {
            type: String,
            required: false,
            default: "button"
        },
        to: {
            type: String,
            required: false,
            default: '/'
        },
        css: {
            type: String,
            required: false,
            default: null
        }
    },
}
</script>

<style scoped>
input[type="file"] {
  display: none;
}
label {
  border-radius: 2rem;
}
button,
a {
  text-align: center;
  text-decoration: none;
  padding: 0.75rem 0.75rem;
  font: inherit;
  border: none;
  color: white;
  cursor: pointer;
  display: inline-block;
  width: 10rem;
  transition: .5s;
  margin-right: 0.5rem;
  margin-left: 0.5rem;
  border-radius: 2rem;
}
button:active,
a:active {
  transform: scale(0.98);
}
.primary:hover:enabled {
  border: none;
}
.primary:enabled {
}
.primary {
  transition: .5s;
  border: none;
}
.primary_link:hover {
  border: none;
}
.primary_link {
  transition: .5s;
  border: none;
}
/*enabled pseudo doesnt exist for links*/
.secondary:hover {
  transition: .5s;
  border: none;
}
.secondary {
  border: none;
}
.delete:hover:enabled {
  transition: .5s;
}
.delete {
  border: none;
}
.delete:enabled {
}
.ready:hover:enabled {
  transition: .5s;
}
.ready {
  border: none;
}
</style>