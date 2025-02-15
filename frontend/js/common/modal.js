import $ from "jquery";
import '../../css/modal.css'

$(document).ready(function () {
    const modal = $('#update-modal');
    const closeModalButton = $('.close');

    closeModalButton.on('click', function () {
        modal.css('display', 'none');
    });

    $(window).on('click', function (event) {
        if (event.target === modal[0]) {
            modal.css('display', 'none');
        }
    });

    $(document).on('keydown', function (event) {
        if (event.key === 'Escape') {
            modal.css('display', 'none');
        }
    });

});