let prevScrollPos = window.scrollY;

function collapseNavbar() {
    let navbarToggle = document.querySelector('.navbar-toggler');
    let navbarCollapse = document.querySelector('.navbar-collapse');

    if (navbarCollapse.classList.contains('show')) {
        navbarToggle.click(); // Programmatically click the toggle to collapse the navbar
    }
}

document.addEventListener('click', function (event) {
    let clickInside = document.getElementById('navbar').contains(event.target);
    if (!clickInside) {
        collapseNavbar();
    }
});

window.addEventListener('scroll', function () {
    let currentScrollPos = window.scrollY;
    if (prevScrollPos > currentScrollPos) {
        document.getElementById("navbar").style.top = "0";
    } else {
        collapseNavbar();
        document.getElementById("navbar").style.top = "-400px";
    }
    prevScrollPos = currentScrollPos;
});
