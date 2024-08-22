<template>
  <!--teleport to="body"-->
  <div >
    <div v-if="show" class="backdrop"></div>
    <transition name="dialog">
      <dialog open v-if="show">
          <header>
              <slot name="header">
                  <h2>{{ title }}</h2>
              </slot>
          </header>
          <section>
              <slot></slot>
              <slot name="input"></slot>
          </section>
          <menu>
            <slot name="cancel"></slot>
            <slot name="actions"></slot>
          </menu>
      </dialog>
    </transition>
  </div>
</template>
<script>
export default {
    props: ["title", "show"]
}
</script>
<style scoped>
.backdrop {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.75);
  z-index: 10;
}

dialog {
  position: fixed;
  top: 20vh;
  left: 10%;
  width: 80%;
  z-index: 100;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
  border-radius: 2rem;
  padding: 0;
  margin: 0;
  overflow: hidden;
}

header {
  background-color: #0099ff;
  color: white;
  width: 100%;
  padding: 1rem;
}

header h2 {
  margin: 0;
}

section {
  padding: 1rem;
}

menu {
  padding: 0.5rem;
  display: flex;
  justify-content: flex-end;
  margin: 0;
}
.dialog-enter-from,
.dialog-leave-to {
  transform: scale(0.8);
}
.dialog-enter-active {
  transition: all 0.1s ease-out;
}
.dialog-leave-active {
  transition: all 0.1s ease-in;
}
.dialog-enter-to,
.dialog-leave-from {
  transform: scale(1);
}
@media (min-width: 768px) {
  dialog {
    left: calc(50% - 20rem);
    width: 40rem;
  }
}
</style>