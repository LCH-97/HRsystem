<template>
    <div id="layoutAuthentication_footer">
        <footer class="py-4 bg-light mt-auto">
            <div class="container-fluid px-4">
                <div class="d-flex align-items-center justify-content-between small">
                    <div class="text-muted">Copyright &copy; Your Website 2023</div>
                    <div>
                        <a href="#">Privacy Policy</a>
                        &middot;
                        <a href="#">Terms &amp; Conditions</a>
                    </div>
                </div>
            </div>
        </footer>
    </div>

</template>

<script>


export default {
    name: 'SideBar',
    components: {

    }
}
</script>



<style scoped>
.sb-nav-fixed {
    margin: 0;
    font-family: var(--bs-body-font-family);
    font-size: var(--bs-body-font-size);
    font-weight: var(--bs-body-font-weight);
    line-height: var(--bs-body-line-height);
    color: var(--bs-body-color);
    text-align: var(--bs-body-text-align);
    background-color: var(--bs-body-bg);
    -webkit-text-size-adjust: 100%;
    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}


.fixed-top,
.sb-nav-fixed #layoutSidenav #layoutSidenav_nav,
.sb-nav-fixed .sb-topnav {
    position: fixed;
    top: 0;
    right: 0;
    left: 0;
    z-index: 1030;
}


#layoutSidenav {
    display: flex;
}

#layoutSidenav #layoutSidenav_nav {
    flex-basis: 225px;
    flex-shrink: 0;
    transition: transform 0.15s ease-in-out;
    z-index: 1038;
    transform: translateX(-225px);
}

#layoutSidenav #layoutSidenav_content {
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    min-width: 0;
    flex-grow: 1;
    min-height: calc(100vh - 56px);
    margin-left: -225px;
}

.sb-sidenav-toggled #layoutSidenav #layoutSidenav_nav {
    transform: translateX(0);
}

.sb-sidenav-toggled #layoutSidenav #layoutSidenav_content:before {
    content: "";
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #000;
    z-index: 1037;
    opacity: 0.5;
    transition: opacity 0.3s ease-in-out;
}

@media (min-width: 992px) {
    #layoutSidenav #layoutSidenav_nav {
        transform: translateX(0);
    }

    #layoutSidenav #layoutSidenav_content {
        margin-left: 0;
        transition: margin 0.15s ease-in-out;
    }

    .sb-sidenav-toggled #layoutSidenav #layoutSidenav_nav {
        transform: translateX(-225px);
    }

    .sb-sidenav-toggled #layoutSidenav #layoutSidenav_content {
        margin-left: -225px;
    }

    .sb-sidenav-toggled #layoutSidenav #layoutSidenav_content:before {
        display: none;
    }
}

.sb-nav-fixed .sb-topnav {
    z-index: 1039;
}

.sb-nav-fixed #layoutSidenav #layoutSidenav_nav {
    width: 225px;
    height: 100vh;
    z-index: 1038;
}

.sb-nav-fixed #layoutSidenav #layoutSidenav_nav .sb-sidenav {
    padding-top: 56px;
}

.sb-nav-fixed #layoutSidenav #layoutSidenav_nav .sb-sidenav .sb-sidenav-menu {
    overflow-y: auto;
}

.sb-nav-fixed #layoutSidenav #layoutSidenav_content {
    padding-left: 225px;
    top: 56px;
}

.sb-sidenav-dark {
    background-color: #212529;
    color: rgba(255, 255, 255, 0.5);
}

.sb-sidenav-dark .sb-sidenav-menu .sb-sidenav-menu-heading {
    color: rgba(255, 255, 255, 0.25);
}

.sb-sidenav-dark .sb-sidenav-menu .nav-link {
    color: rgba(255, 255, 255, 0.5);
}

.sb-sidenav-dark .sb-sidenav-menu .nav-link .sb-nav-link-icon {
    color: rgba(255, 255, 255, 0.25);
}

.sb-sidenav-dark .sb-sidenav-menu .nav-link .sb-sidenav-collapse-arrow {
    color: rgba(255, 255, 255, 0.25);
}

.sb-sidenav-dark .sb-sidenav-menu .nav-link:hover {
    color: #fff;
}

.sb-sidenav-dark .sb-sidenav-menu .nav-link.active {
    color: #fff;
}

.sb-sidenav-dark .sb-sidenav-menu .nav-link.active .sb-nav-link-icon {
    color: #fff;
}

.sb-sidenav-dark .sb-sidenav-footer {
    background-color: #343a40;
}

.accordion {
    --bs-accordion-color: #212529;
    --bs-accordion-bg: #fff;
    --bs-accordion-transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out, border-radius 0.15s ease;
    --bs-accordion-border-color: var(--bs-border-color);
    --bs-accordion-border-width: 1px;
    --bs-accordion-border-radius: 0.375rem;
    --bs-accordion-inner-border-radius: calc(0.375rem - 1px);
    --bs-accordion-btn-padding-x: 1.25rem;
    --bs-accordion-btn-padding-y: 1rem;
    --bs-accordion-btn-color: #212529;
    --bs-accordion-btn-bg: var(--bs-accordion-bg);
    --bs-accordion-btn-icon: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%23212529'%3e%3cpath fill-rule='evenodd' d='M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z'/%3e%3c/svg%3e");
    --bs-accordion-btn-icon-width: 1.25rem;
    --bs-accordion-btn-icon-transform: rotate(-180deg);
    --bs-accordion-btn-icon-transition: transform 0.2s ease-in-out;
    --bs-accordion-btn-active-icon: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%230c63e4'%3e%3cpath fill-rule='evenodd' d='M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z'/%3e%3c/svg%3e");
    --bs-accordion-btn-focus-border-color: #86b7fe;
    --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
    --bs-accordion-body-padding-x: 1.25rem;
    --bs-accordion-body-padding-y: 1rem;
    --bs-accordion-active-color: #0c63e4;
    --bs-accordion-active-bg: #e7f1ff;
}
</style>