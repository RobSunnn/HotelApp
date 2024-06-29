let subscribeButton = document.getElementById('subscribe-btn');
subscribeButton.addEventListener('click', function () {
    console.log('Hello');
    document.getElementById('.subscribe-btn').scrollIntoView({behavior: 'smooth'});
});