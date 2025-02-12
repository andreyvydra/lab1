import $ from "jquery";
import {addObject, main} from "./objects";
import {redirectIfAuthenticated} from "./common/utils";

$(document).ready(function() {
    redirectIfAuthenticated();
    main("/coordinates");

    $('#form').on('submit', function (event) {
        event.preventDefault();

        const formData = {
            isChangeable: $('#form input[name="is-changeable-input"]').is(':checked'),
            x: $('#x-input').val(),
            y: $('#y-input').val(),
        };

        addObject("/coordinates", formData)
    });
})