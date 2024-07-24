let prevScrollPos = window.scrollY;

document.addEventListener('click', function(event) {
    let clickInside = document.getElementById('navbar').contains(event.target);
    let navbarToggle = document.querySelector('.navbar-toggler');
    let navbarCollapse = document.querySelector('.navbar-collapse');

    if (!clickInside && navbarCollapse.classList.contains('show')) {
        navbarToggle.click(); // Programmatically click the toggler to collapse the navbar
    }
});

window.addEventListener('scroll', function () {
    let currentScrollPos = window.scrollY;
    if (prevScrollPos > currentScrollPos) {
        document.getElementById("navbar").style.top = "0";
    } else {
        document.getElementById("navbar").style.top = "-120px";
    }
    prevScrollPos = currentScrollPos;
});