// let subscribeButton = document.getElementById('subscribe-btn');
// subscribeButton.addEventListener('click', function () {
//     console.log('Hello');
//     document.getElementById('.subscribe-btn').scrollIntoView({behavior: 'smooth'});
// });

document.getElementById('subscribe-form').addEventListener('submit', function (event) {
    event.preventDefault();  // Prevent the default form submission
    console.log('Hello');
    document.getElementById('subscribe-form').scrollIntoView({behavior: 'smooth'});
    // Optionally, you can submit the form programmatically after the scroll
    // event.target.submit();
});