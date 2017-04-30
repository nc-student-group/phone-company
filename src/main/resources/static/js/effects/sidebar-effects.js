$document = $(document);

$document.ready(function () {

    $document.on('click', '.close-sidebar', function () {
        $('.sidebar-wrapper').animate({
            left: "-260px"
        }, { duration: 500, queue: false });
        $('.sidebar').animate({
            left: "-260px"
        }, { duration: 500, queue: false });
        $('.sidebar-controls').fadeIn('slow');
        $('.main-panel').addClass('stretch-main-body');
        $('.services-cards').css('padding-left','60px');
    });

    $document.on('click', '.sidebar-controls', function () {
        $('.sidebar-wrapper').animate({
            left: "0px"
        }, { duration: 500, queue: false });
        $('.sidebar').animate({
            left: "0px"
        }, { duration: 500, queue: false });
        $('.main-panel').removeClass('stretch-main-body');
        $('.sidebar-controls').fadeOut('slow');
        $('.services-cards').css('padding-left','0');
    });
});