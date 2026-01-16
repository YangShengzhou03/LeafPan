<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #1a1a1a;
  background-color: #fff;
  line-height: 1.5;
}

#app {
  width: 100%;
  min-height: 100vh;
  text-align: left;
}

a {
  color: #2563eb;
  text-decoration: none;
  transition: color 0.2s;
}

a:hover {
  color: #1d4ed8;
}

button {
  font-family: inherit;
}

input,
textarea,
select {
  font-family: inherit;
}
</style>
