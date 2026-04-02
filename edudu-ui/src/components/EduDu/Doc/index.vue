<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'EduDuDoc',
  data() {
    return {
      url: 'http://doc.edudu.vip/edudu-vue'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>