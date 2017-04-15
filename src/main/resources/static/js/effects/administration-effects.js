$document = $(document);
var taskList;

$document.ready(function () {

    $document.on('click', '.add-user-button', function () {

        taskList = $(this).closest('.user-list');

        taskList.removeClass("col-md-12");
        taskList.addClass("col-md-6");
        $('.userForm').fadeIn();
    });

    $document.on('click', '.close-user', function () {
        $.when($('.userForm').fadeOut()).done(function () {
            taskList.removeClass("col-md-6");
            taskList.addClass("col-md-12");
        });
    });
});
