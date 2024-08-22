<template>
    <div class="basecard card-color" :class="background">
        <slot>

        </slot>
    </div>
</template>

<script>
import '../UI/Decoder.css';
export default {
  props: {
    background: {
      type: String,
      required: false,
      default: "#e7e7e7"
    }
  }
}

</script>

<style scoped>
.basecard {
  border: 1px solid rgb(179, 179, 179);
  border-radius: 2rem;
  margin: 1rem;
  backdrop-filter: blur(8px);   
  box-shadow: 0 20px 10px rgba(0, 0, 0, 0.10);
  padding: 2rem;
}
</style>