function toggleNavbar() {
    var navbar = document.getElementById("navbar");
    if (navbar.classList.contains("open")) {
        navbar.classList.remove("open");
    } else {
        navbar.classList.add("open");
    }
}