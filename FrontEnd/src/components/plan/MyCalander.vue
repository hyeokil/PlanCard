<template>
    <div class="card p-fluid">
        <h1>My calaneder</h1>
        <v-calendar
        :attributes='attribute.attributes'
        />
    </div>
</template>

<script setup>
import { ref } from "vue";

const attribute = ref({
    attributes: [
        // This is a single attribute
        {
            // An optional key can be used for retrieving this attribute later,
            // and will most likely be derived from your data object
            key: 'today',
            // Attribute type definitions
            highlight: true,  // Boolean, String, Object
            dot: true,        // Boolean, String, Object
            bar: true,        // Boolean, String, Object
            content: 'red',   // Boolean, String, Object
            popover: { }, // Only objects allowed
            // Your custom data object for later access, if needed
            customData: { },
            // We also need some dates to know where to display the attribute
            // We use a single date here, but it could also be an array of dates,
            //  a date range or a complex date pattern.
            dates: new Date(),
            // You can optionally provide dates to exclude
            excludeDates: null,
            // Think of `order` like `z-index`
            order: 0
        }
    ]
});
</script>

<style scoped>

</style>