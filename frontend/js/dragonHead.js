import $ from "jquery";
import {addObject, main} from "./objects";
import {redirectIfAuthenticated} from "./common/utils";

$(document).ready(function() {
    redirectIfAuthenticated();
    main("/dragonHead");

    $('#form').on('submit', function (event) {
        event.preventDefault();

        const formData = {
            isChangeable: $('#form input[name="is-changeable-input"]').is(':checked'),
            eyesCount: $('#eyes-count-input').val(),
            size: $('#size-input').val(),
        };

        addObject("/dragonHead", formData)
    });
})