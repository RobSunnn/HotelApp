let prevScrollPos = window.scrollY;

window.addEventListener('scroll', function () {
    let currentScrollPos = window.scrollY;
    if (prevScrollPos > currentScrollPos) {
        document.getElementById("navbar").style.top = "0";
    } else {
        document.getElementById("navbar").style.top = "-120px";
    }
    prevScrollPos = currentScrollPos;
});