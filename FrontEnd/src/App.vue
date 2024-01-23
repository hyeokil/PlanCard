<template>
  <div>
    
    <v-app id="inspire">

      <!-- <TheHeaderNavbar /> -->

      <v-main class="bg-grey-lighten-3">
        <v-container>
          <v-row>
            <v-col>
              <v-sheet min-height="85vh" rounded="lg">

                <router-view />

              </v-sheet>
            </v-col>
          </v-row>
        </v-container>
      </v-main>

  </v-app>

  </div>
</template>



<script setup>
  import TheHeaderNavbar from './components/layout/TheHeaderNavbar.vue'

</script>



<style>

</style>