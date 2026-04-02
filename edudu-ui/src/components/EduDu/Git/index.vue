<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'EduDuGit',
  data() {
    return {
      url: 'https://gitee.com/y_project/EduDu-Vue'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>