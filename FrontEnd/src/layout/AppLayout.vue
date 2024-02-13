<template>
    <div class="layout-wrapper font-title" :class="containerClass" style="background-color: rgba(52, 152, 219, 0.15);">
        <app-topbar></app-topbar>
        <!-- </div> -->
        <!-- <div class="layout-sidebar">
            <app-sidebar></app-sidebar>
        </div> -->

        <div :class="{'layout-main-container':!planStore.isMeetingView&&accountsStore.isLogin, 'meeting-container':planStore.isMeetingView||!accountsStore.isLogin}">
            <div :class="{'layout-main':!planStore.isMeetingView&&accountsStore.isLogin, 'meeting-container':planStore.isMeetingView||!accountsStore.isLogin}">
                <router-view></router-view>
            </div>
            <div v-if="!planStore.isMeetingView">
                <app-footer></app-footer>
            </div>
        </div>
        
        <!-- <app-config></app-config> -->
        <div class="layout-mask"></div>
    </div>
</template>



<script setup>
    import { computed, watch, ref } from 'vue';
    import AppTopbar from './AppTopbar.vue';
    import AppFooter from './AppFooter.vue';
    import { useLayout } from '@/layout/composables/layout';
    import { useAccountsStore } from "@/stores/accountsStore";
    import { usePlanStore } from "@/stores/planStore";
    const { layoutConfig, layoutState, isSidebarActive } = useLayout();

    const accountsStore = useAccountsStore()
    const planStore = usePlanStore()
    const outsideClickListener = ref(null);
    watch(isSidebarActive, (newVal) => {
        if (newVal) {
            bindOutsideClickListener();
        } else {
            unbindOutsideClickListener();
        }
    });

    const containerClass = computed(() => {
        return {
            'layout-theme-light': layoutConfig.darkTheme.value === 'light',
            'layout-theme-dark': layoutConfig.darkTheme.value === 'dark',
            'layout-overlay': layoutConfig.menuMode.value === 'overlay',
            'layout-static': layoutConfig.menuMode.value === 'static',
            'layout-static-inactive': layoutState.staticMenuDesktopInactive.value && layoutConfig.menuMode.value === 'static',
            'layout-overlay-active': layoutState.overlayMenuActive.value,
            'layout-mobile-active': layoutState.staticMenuMobileActive.value,
            'p-input-filled': layoutConfig.inputStyle.value === 'filled',
            'p-ripple-disabled': !layoutConfig.ripple.value
        };
    });
    const bindOutsideClickListener = () => {
        if (!outsideClickListener.value) {
            outsideClickListener.value = (event) => {
                if (isOutsideClicked(event)) {
                    layoutState.overlayMenuActive.value = false;
                    layoutState.staticMenuMobileActive.value = false;
                    layoutState.menuHoverActive.value = false;
                }
            };
            document.addEventListener('click', outsideClickListener.value);
        }
    };
    const unbindOutsideClickListener = () => {
        if (outsideClickListener.value) {
            document.removeEventListener('click', outsideClickListener);
            outsideClickListener.value = null;
        }
    };
    const isOutsideClicked = (event) => {
        const sidebarEl = document.querySelector('.layout-sidebar');
        const topbarEl = document.querySelector('.layout-menu-button');
        return !(sidebarEl.isSameNode(event.target) || sidebarEl.contains(event.target) || topbarEl.isSameNode(event.target) || topbarEl.contains(event.target));
    };

</script>


<style scoped>
.meeting-container{
    padding: 0rem;
    /* padding-left: 4rem; */
    /* padding-right: 4rem; */
}
</style>
